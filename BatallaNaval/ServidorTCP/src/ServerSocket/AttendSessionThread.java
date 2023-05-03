package ServerSocket;

import SocketEvents.DisconnectEvent;
import SocketEvents.SocketListener;
import SocketEvents.ReceiveDataEvent;
import SocketEvents.UserRegistrationEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import Models.Usuario;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class AttendSessionThread extends Thread{
    private Socket socket;
    private int ID;
    public AttendSessionThread(Socket cliente, int ID){
        this.ID = ID;
        this.socket = cliente;
    }
    @Override
    public void run(){
        DataInputStream entrada;
        try {
            entrada = new DataInputStream(socket.getInputStream());
            while (!isInterrupted()) {                    
                String mensaje = entrada.readUTF();
                ReceiveDataEvent event = new ReceiveDataEvent(this, mensaje, this.ID);
                this.notifyReceiveDataEvent(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerTCP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            DisconnectEvent event = new DisconnectEvent(this, this.ID);
            this.notifyDisconnectEvent(event);
        }
    }
    
    /*-----------------Events-----------------*/
    protected EventListenerList listenerList = new EventListenerList();
    
    public void addSocketListener(SocketListener listener){
        listenerList.add(SocketListener.class, listener);
    }
    public void removeSocketListener(SocketListener listener){
        listenerList.remove(SocketListener.class, listener);
    }
    public void notifyReceiveDataEvent(ReceiveDataEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == SocketListener.class){
                ((SocketListener) listeners[i+1]).onReceiveData(event);
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
