package ServerSocket;

import Events.DataEvent;
import Events.DisconnectEvent;
import Events.SocketListener;
import Models.Cliente;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.ListenerNotFoundException;
import javax.swing.event.EventListenerList;

/**
 *
 * @author MIRKO
 */
public class VerifyConnectionThread extends Thread{
    public Map<Integer, Cliente> clientes = new HashMap<>();
    
    public VerifyConnectionThread(Map<Integer, Cliente> clientes){
        this.clientes = clientes;
    }
    private int c = 0;
    @Override
    public void run() {
        while (true) {            
            for (Integer key : clientes.keySet()) {
                try {
                    Cliente cl = clientes.get(key);
                    if(!cl.getSocket().getInetAddress().isReachable(5000)){
                        DisconnectEvent event = new DisconnectEvent(this, key);
                        this.notifyDisconnectEvent(event);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(VerifyConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    /*------------------------------------------------------------------------*/
    protected EventListenerList listenerList = new EventListenerList();
    
    public void addSocketListener(SocketListener listener){
        listenerList.add(SocketListener.class, listener);
    }
    public void removeSocketListener(SocketListener listener){
        listenerList.remove(SocketListener.class, listener);
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
