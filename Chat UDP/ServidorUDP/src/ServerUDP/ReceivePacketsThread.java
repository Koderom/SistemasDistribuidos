/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerUDP;

import Events.ReceivePacketEvent;
import Events.SocketListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author MIRKO
 */
public class ReceivePacketsThread extends Thread{
    private DatagramSocket serverSocket;
    private int buffer_size;
    
    public ReceivePacketsThread(DatagramSocket serverSocket, int buffer_size){
        this.serverSocket = serverSocket;
        this.buffer_size = buffer_size;
    }

    @Override
    public void run() {
        while(!interrupted()){
            try {
                byte[] inputBuffer = new byte[buffer_size];
                DatagramPacket inputPacket = new DatagramPacket(inputBuffer, buffer_size);
                serverSocket.receive(inputPacket);
                String mensaje = new String(inputPacket.getData());
                ReceivePacketEvent clienteConectado = new ReceivePacketEvent(inputPacket, inputPacket.getAddress(), inputPacket.getPort(), mensaje, this);
                fireClientConnectedEvent(clienteConectado);
            } catch (IOException ex) {
                Logger.getLogger(ReceivePacketsThread.class.getName()).log(Level.SEVERE, null, ex);
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
    public void fireClientConnectedEvent(ReceivePacketEvent event){
        Object[] listener = listenerList.getListenerList();
        for(int i = 0; i < listener.length; i = i + 2){
            if(listener[i] == SocketListener.class){
                ((SocketListener) listener[i+1]).onReceivePacket(event);
            }
        }
    }
}
