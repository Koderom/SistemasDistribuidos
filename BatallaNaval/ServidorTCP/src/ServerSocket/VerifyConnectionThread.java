package ServerSocket;

import SocketEvents.ReceiveDataEvent;
import SocketEvents.DisconnectEvent;
import SocketEvents.SocketListener;
import Models.Usuario;
import Models.Sesion;
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
    public Map<Integer, Sesion> sesiones = new HashMap<>();
    
    public VerifyConnectionThread(Map<Integer, Sesion> sesiones){
        this.sesiones = sesiones;
    }
    private int c = 0;
    @Override
    public void run() {
        while (true) {            
            for (Sesion sesion : sesiones.values()) {
                try {
                    if(!sesion.getSocket().getInetAddress().isReachable(500)){
                        DisconnectEvent event = new DisconnectEvent(this, sesion.getID());
                        this.notifyDisconnectEvent(event);
                    }
                } catch (IOException ex) {
                    //Logger.getLogger(VerifyConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
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
                ((SocketListener) listeners[i+1]).onSessionDisconnect(event);
            }
        }
    }
}
