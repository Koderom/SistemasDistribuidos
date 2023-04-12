/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServidorSocket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

import Events.*;

/**
 *
 * @author MIRKO
 */
public class HiloServidorTCP extends Thread{
    Socket socket;
    DataInputStream entrada;
    
    public HiloServidorTCP(String name, Socket socket){
        super(name);
        this.socket = socket;
    }
    @Override
    public void run(){
        try {    
            while (true) {                    
                entrada = new DataInputStream(socket.getInputStream());
                String mensaje = entrada.readUTF();
                DataEvent event = new DataEvent(this, mensaje, Integer.valueOf(getName()));
                this.notifyDataEvent(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
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
    public void notifyDataEvent(DataEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == SocketListener.class){
                ((SocketListener) listeners[i+1]).onReadMessage(event);
            }
        }
    }
}
