package ServerSocket;

import Events.ConnectionEvent;
import Events.SocketListener;
import Models.Cliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author MIRKO
 */
public class AttendConnectionTheread extends Thread {
    Socket socket;
    ServerSocket servidor;
    int keyCounter;
    
    public AttendConnectionTheread(ServerSocket servidor){
       this.servidor = servidor;
       keyCounter = 1;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                socket = servidor.accept();
                ConnectionEvent event = new ConnectionEvent(this, socket, keyCounter);
                this.notifyConnectionEvent(event);
                keyCounter++;
            } catch (IOException ex) {
                Logger.getLogger(AttendConnectionTheread.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    public void notifyConnectionEvent(ConnectionEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == SocketListener.class){
                ((SocketListener) listeners[i+1]).onClientConnected(event);
            }
        }
    }
}
