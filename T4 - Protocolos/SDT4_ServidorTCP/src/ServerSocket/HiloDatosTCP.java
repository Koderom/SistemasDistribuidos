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

/**
 *
 * @author MIRKO
 */
public class HiloDatosTCP extends Thread{
    private Socket cliente;
    int id;
    
    public HiloDatosTCP(Socket cliente, int id){
        this.cliente = cliente;
        this.id = id;
    }
    @Override
    public void run(){
        DataInputStream entrada;
        try {
            Cliente nuevoCliente = getCliente(cliente, id);
            UserRegistrationEvent registrationEvent = new UserRegistrationEvent(this, nuevoCliente);
            this.notifyUserRegistrationEvent(registrationEvent);
            
            while (!isInterrupted()) {                    
                entrada = new DataInputStream(cliente.getInputStream());
                String mensaje = entrada.readUTF();
                DataEvent event = new DataEvent(this, mensaje, id);
                this.notifyDataEvent(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            System.out.println("desconectado: "+id);
            DisconnectEvent event = new DisconnectEvent(this, id);
            this.notifyDisconnectEvent(event);
        }
    }
    /*-----------------Metodos----------------*/
    private Cliente getCliente(Socket socket, int id){
        DataOutputStream salida;
        DataInputStream entrada;
        Cliente cliente;
        try {
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
            salida.writeUTF("<ID,"+id+">");
            String mensaje = entrada.readUTF();
            Map<String, String> datos = getInfoCabezera(mensaje);
            id = Integer.parseInt(datos.get("ID"));
            String nick = datos.get("NICK").length() == 0 ? "unnamed" : datos.get("NICK");
            cliente = new Cliente(id, nick, socket);
            return cliente;
        } catch (IOException ex) {
            Logger.getLogger(HiloConexiones.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error en el hilo de datos");
            return null;
        }
    }
    private Map<String, String> getInfoCabezera(String mensaje){
        Map<String, String> cabezera = new HashMap<>();
        int info_ini = mensaje.indexOf("<");
        int info_fin = mensaje.indexOf(">");
        if(info_ini != -1 && info_fin != -1){
            String info = mensaje.substring(info_ini+1, info_fin);
            String datos[] = info.split("/");
            for(String dato: datos){
                String campo_valor[] = dato.split(",");
                cabezera.put(campo_valor[0], campo_valor[1]);
            }
        }
        return cabezera;
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
