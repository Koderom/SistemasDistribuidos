package ServerSocket;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import Events.*;
import Models.Cliente;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import utils.MessageUtil;

/**
 *
 * @author MIRKO
 */
public class AttendClientThread extends Thread{
    private Socket socket;
    private int ID;
    public AttendClientThread(Socket cliente, int ID){
        this.ID = ID;
        this.socket = cliente;
    }
    @Override
    public void run(){
        DataInputStream entrada;
        try {
            iniciarRegistro();
            while (!isInterrupted()) {                    
                entrada = new DataInputStream(socket.getInputStream());
                String mensaje = entrada.readUTF();
                
                Map<String, String> info = MessageUtil.convertMessageToInfo(mensaje);
                if(info.containsKey("ID") && info.containsKey("NICK")) this.registrarCliente(socket, info);
                else if(info.containsKey("ID")) this.clientSendMenssage(info);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            DisconnectEvent event = new DisconnectEvent(this, this.ID);
            this.notifyDisconnectEvent(event);
        }
    }
    private void iniciarRegistro(){
        try {
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            Map<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(ID));
            String mensaje = MessageUtil.convertInfoToMessage(info, "");
            salida.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(AttendClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void registrarCliente(Socket socket, Map<String, String> info){
        UserRegistrationEvent event = new UserRegistrationEvent(this.ID, info.get("NICK"), socket, this);
        this.notifyUserRegistrationEvent(event);
    }
    private void clientSendMenssage(Map<String, String> info){
        DataEvent event = new DataEvent(this, info.get("MSJ"), this.ID);
        this.notifyDataEvent(event);
    }
    /*-----------------Events-----------------*/
    protected EventListenerList listenerList = new EventListenerList();
    
    public void addSocketListener(SocketListener listener){
        listenerList.add(SocketListener.class, listener);
    }
    public void removeSocketListener(SocketListener listener){
        listenerList.remove(SocketListener.class, listener);
    }
    public void notifyDataEvent(DataEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == SocketListener.class){
                ((SocketListener) listeners[i+1]).onReadMessage(event);
            }
        }
    }
    public void notifyUserRegistrationEvent(UserRegistrationEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == SocketListener.class){
                ((SocketListener) listeners[i+1]).onRegisteredUser(event);
            }
        }
    }
    public void notifyDisconnectEvent(DisconnectEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == SocketListener.class){
                ((SocketListener) listeners[i+1]).onClientDisconnect(event);
            }
        }
    }
}
