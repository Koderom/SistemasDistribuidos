package ClienteSocket;

import ClientEvents.ClientListener;
import ClientEvents.LostConnectionEvent;
import ClientEvents.ReceiveMessageEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import javax.swing.event.EventListenerList;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class ReceiveDataThread extends Thread{
    private Socket socket;
    private int id;
    public ReceiveDataThread(int id, Socket socket) {
        this.socket = socket;
        this.id = id;
    }
    @Override
    public void run() {
        DataInputStream entrada;
        try {
            entrada = new DataInputStream(socket.getInputStream());
            while (true) {
                String mensaje = entrada.readUTF();
                Map<String, String> datos = Parse.convertMessageToInfo(mensaje);
                ReceiveMessageEvent event = new ReceiveMessageEvent(mensaje, this);
                this.notifyConnectionEvent(event);
            }
        } catch (IOException ex) {
            System.out.println("HiloReceiveData:  se perdio la conexion con el servidor");
            LostConnectionEvent LC_event = new LostConnectionEvent(this);
            this.notifyLostConnectionEvent(LC_event);
            //Logger.getLogger(ReceiveDataThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*-----------------Events-----------------*/
    protected EventListenerList listenerList = new EventListenerList();
    
    public void addSocketListener(ClientListener listener){
        listenerList.add(ClientListener.class, listener);
    }
    public void removeSocketListener(ClientListener listener){
        listenerList.remove(ClientListener.class, listener);
    }
    public void notifyConnectionEvent(ReceiveMessageEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ClientListener.class){
                ((ClientListener) listeners[i+1]).onReceiveMessage(event);
            }
        }
    }
    public void notifyLostConnectionEvent(LostConnectionEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ClientListener.class){
                ((ClientListener) listeners[i+1]).onLostConnection(event);
            }
        }
    }
}
