/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClientUDP;

import events.ClientListener;
import events.ReceivePingEvent;
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
public class ReceivePacketsThread extends Thread {
    DatagramSocket socket;
    int buffer_size;
    int ID;
    
    public ReceivePacketsThread(DatagramSocket socket, int buffer_size, int ID){
        this.socket = socket;
        this.buffer_size = buffer_size;
        this.ID = ID;
    }

    @Override
    public void run() {
        while(!interrupted()){
            try {
                byte[] inputBuffer = new byte[buffer_size];
                DatagramPacket inputPacket = new DatagramPacket(inputBuffer, buffer_size);
                socket.receive(inputPacket);
                String mensaje = new String(inputPacket.getData());
                this.interpretar(mensaje);
            } catch (IOException ex) {
                Logger.getLogger(ReceivePacketsThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void interpretar(String mensaje){
        Map<String, String> info = Parse.convertMessageToInfo(mensaje);
        if(info.containsKey("TYPE")){
            if(info.get("TYPE").equals("PING")){
                ReceivePingEvent event = new ReceivePingEvent(this);
                this.fireReceivePingEvent(event);
            }
        }else this.interpretarMensaje(mensaje);
    }
    private void interpretarMensaje(String mensaje){
        Map<String, String> info = Parse.convertMessageToInfo(mensaje);
        int id_origen = Integer.parseInt(info.get("IDSOURCE"));
        int id_destino = Integer.parseInt(info.get("IDDEST"));
        String msj = info.get("MSJ");
        String nick = (info.containsKey("NICK"))? info.get("NICK") : "Servidor";
        if(this.ID == id_origen) System.out.println("[Yo] : "+ msj);
        else if(id_origen == 0) System.out.println("[Servidor] : "+ msj);
        else System.out.println(String.format("[%s] : %s", nick, msj));
    }
    /*------------------------------------------------------------------------*/
    protected EventListenerList listenerList = new EventListenerList();
    
    public void addClientListener(ClientListener listener){
        listenerList.add(ClientListener.class, listener);
    }
    public void removeClientListener(ClientListener listener){
        listenerList.remove(ClientListener.class, listener);
    }
    public void fireReceivePingEvent(ReceivePingEvent event){
        Object[] listener = listenerList.getListenerList();
        for(int i = 0; i < listener.length; i = i + 2){
            if(listener[i] == ClientListener.class){
                ((ClientListener) listener[i+1]).onReceivePing(event);
            }
        }
    }
}
