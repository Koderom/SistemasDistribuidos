/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerUDP;

import Events.ClientArriveEvent;
import Events.ReceiveMessageEvent;
import Events.ReceivePingEvent;
import Events.RegisterClientEvent;
import Events.SocketListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import utils.Parse;

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
                this.InterpretarMensaje(inputPacket, mensaje);
            } catch (IOException ex) {
                Logger.getLogger(ReceivePacketsThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void InterpretarMensaje(DatagramPacket inputPacket, String mensaje){
        Map<String, String> info = Parse.convertMessageToInfo(mensaje);
        
        if(info.containsKey("TYPE") && info.get("TYPE").equals("HI")){
            ClientArriveEvent event = new ClientArriveEvent(inputPacket, serverSocket, this);
            this.fireClientArriveEvent(event);
        }else if(info.containsKey("TYPE") && info.get("TYPE").equals("MSJ")){
            ReceiveMessageEvent clienteConectado = new ReceiveMessageEvent(inputPacket, inputPacket.getAddress(), inputPacket.getPort(), mensaje, this);
            this.fireReceiveMessageEvent(clienteConectado);
        }else if(info.containsKey("TYPE") && info.get("TYPE").equals("PING")){
            ReceivePingEvent event = new ReceivePingEvent(inputPacket, Integer.parseInt(info.get("ID")), this);
            this.fireReceivePingEvent(event);
        }else if(info.containsKey("NICK")){
            int id = Integer.parseInt(info.get("ID"));
            String nick = info.get("NICK");
            RegisterClientEvent event = new RegisterClientEvent(inputPacket, id , nick);
            this.fireRegisterClientEvent(event);
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
    public void fireReceiveMessageEvent(ReceiveMessageEvent event){
        Object[] listener = listenerList.getListenerList();
        for(int i = 0; i < listener.length; i = i + 2){
            if(listener[i] == SocketListener.class){
                ((SocketListener) listener[i+1]).onReceiveMessage(event);
            }
        }
    }
    public void fireClientArriveEvent(ClientArriveEvent event){
        Object[] listener = listenerList.getListenerList();
        for(int i = 0; i < listener.length; i = i + 2){
            if(listener[i] == SocketListener.class){
                ((SocketListener) listener[i+1]).onClientArrives(event);
            }
        }
    }
    public void fireRegisterClientEvent(RegisterClientEvent event){
        Object[] listener = listenerList.getListenerList();
        for(int i = 0; i < listener.length; i = i + 2){
            if(listener[i] == SocketListener.class){
                ((SocketListener) listener[i+1]).onRegisterClient(event);
            }
        }
    }
    public void fireReceivePingEvent(ReceivePingEvent event){
        Object[] listener = listenerList.getListenerList();
        for(int i = 0; i < listener.length; i = i + 2){
            if(listener[i] == SocketListener.class){
                ((SocketListener) listener[i+1]).onReceivePing(event);
            }
        }
    }
}
