/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClienteSocket;

import ClientEvents.ClientListener;
import ClientEvents.ConnectionEstablishedEvent;
import ClientEvents.TryConnectionEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author MIRKO
 */
public class ServerConnectionThread extends Thread {
    String serverAddress;
    int port;
    
    public ServerConnectionThread(String serverAddress, int puerto){
        this.serverAddress = serverAddress;
        this.port = puerto;
    }
    @Override
    public void run() {
        while(!interrupted()){
            try {
                System.out.println("intentando establecer conexion con el servidor...");
                Socket socket = new Socket(serverAddress, port);
                ConnectionEstablishedEvent event = new ConnectionEstablishedEvent(socket, this);
                notifyConnectionEvent(event);
                break;  
            } catch (IOException ex) {
                try {
                    TryConnectionEvent event = new TryConnectionEvent(this);
                    this.notifyTryConnectionEvent(event);
                    sleep(2000);
                    //Logger.getLogger(ServerConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex1) {
                    Logger.getLogger(ServerConnectionThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
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
    public void notifyConnectionEvent(ConnectionEstablishedEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ClientListener.class){
                ((ClientListener) listeners[i+1]).onConnectionEstablished(event);
            }
        }
    }
    public void notifyTryConnectionEvent(TryConnectionEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ClientListener.class){
                ((ClientListener) listeners[i+1]).onTryConnection(event);
            }
        }
    }
    
}
