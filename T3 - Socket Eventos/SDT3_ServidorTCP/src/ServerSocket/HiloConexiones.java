/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerSocket;

import Events.ConnectionEvent;
import Events.SocketListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author MIRKO
 */
public class HiloConexiones extends Thread {
    Socket socket;
    ServerSocket servidor;
    
    public HiloConexiones(ServerSocket servidor){
       this.servidor = servidor;
       
    }
    public void run(){
        while(true){
            try {
                socket = servidor.accept();
                ConnectionEvent event = new ConnectionEvent(this, socket);
                this.notifyConnectionEvent(event);
            } catch (IOException ex) {
                Logger.getLogger(HiloConexiones.class.getName()).log(Level.SEVERE, null, ex);
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
