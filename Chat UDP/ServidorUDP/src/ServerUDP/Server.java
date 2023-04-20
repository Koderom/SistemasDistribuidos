/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerUDP;

import Events.ReceivePacketEvent;
import Events.SocketListener;
import Models.Client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIRKO
 */
public class Server implements SocketListener{
    private final int BUFFER_SIZE = 1024;
    private DatagramSocket serverSocket;
    private Map<Integer, Client> Clientes = new HashMap<>();
    
    public Server(int port){
        runServer(port);
    }
    private void runServer(int port){
        try {
            serverSocket = new DatagramSocket(port);
            System.out.println("servidor iniciado");
            ReceivePacketsThread recibirCliente = new ReceivePacketsThread(serverSocket, BUFFER_SIZE);
            recibirCliente.addSocketListener(this);
            recibirCliente.start();
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*------------------------------------------------------------------------*/
    private void sendMessageBroadCast(String mensaje) {
        for(Client cliente : Clientes.values()){
            try {
                byte[] outputBuffer = mensaje.getBytes();
                InetAddress address = InetAddress.getByName(cliente.getAddress());
                DatagramPacket outputPacket = new DatagramPacket(outputBuffer, BUFFER_SIZE, address, cliente.getPort());
                serverSocket.send(outputPacket);
                System.out.println("enviado : " + mensaje);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /*------------------------------------------------------------------------*/
    @Override
    public void onReceivePacket(ReceivePacketEvent event) {
        String client_address = event.getAddress().getCanonicalHostName();
        int client_port = event.getPort();
        for(Client cliente: Clientes.values()){
            if(cliente.isSame(client_address, client_port)){
                sendMessageBroadCast(event.getMensaje());
                return;
            }
        }
        Client cliente = new Client(client_address, client_port);
        Clientes.put(cliente.hashCode(), cliente);
        sendMessageBroadCast(event.getMensaje());
    }

    

    
}
