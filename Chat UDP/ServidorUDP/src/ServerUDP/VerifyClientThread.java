/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerUDP;

import Events.InactiveClientEvent;
import Events.SocketListener;
import Models.Client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class VerifyClientThread extends Thread{
    Map<Integer, Client> Clientes;
    DatagramSocket socket;

    public VerifyClientThread(Map<Integer, Client> Clientes, DatagramSocket socket) {
        this.Clientes = Clientes;
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!interrupted()) {            
            for(Client cliente : Clientes.values()){
                try {
                    if(cliente.getIntertosDeConexion() > 5){
                        InactiveClientEvent event = new InactiveClientEvent(cliente.getID(), this);
                        this.fireInactiveClientEvent(event);
                    }else{
                        this.enviarPing(cliente);
                        int intentos = cliente.getIntertosDeConexion()+1;
                        cliente.setIntertosDeConexion(intentos);
                        System.out.println("intentos : " + intentos);
                    }
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VerifyClientThread.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(VerifyClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    private void enviarPing(Client cliente) throws UnknownHostException, IOException{
        InetAddress address_cli = InetAddress.getByName(cliente.getAddress());
        HashMap<String, String> info = new HashMap<>();
        info.put("TYPE", "PING");
        String mensaje = Parse.convertInfoToMessage(info, "");
        byte output_buffer[] = mensaje.getBytes();
        DatagramPacket output_packet = new DatagramPacket(output_buffer, output_buffer.length, address_cli, cliente.getPort());
        this.socket.send(output_packet);
    }
    
    /*------------------------------------------------------------------------*/
    protected EventListenerList listenerList = new EventListenerList();
    
    public void addSocketListener(SocketListener listener){
        listenerList.add(SocketListener.class, listener);
    }
    public void removeSocketListener(SocketListener listener){
        listenerList.remove(SocketListener.class, listener);
    }
    public void fireInactiveClientEvent(InactiveClientEvent event){
        Object[] listener = listenerList.getListenerList();
        for(int i = 0; i < listener.length; i = i + 2){
            if(listener[i] == SocketListener.class){
                ((SocketListener) listener[i+1]).onInactiveClient(event);
            }
        }
    }
}
