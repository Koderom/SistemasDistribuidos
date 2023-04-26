/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerUDP;

import Events.ClientArriveEvent;
import Events.InactiveClientEvent;
import Events.ReceiveMessageEvent;
import Events.ReceivePingEvent;
import Events.SocketListener;
import Events.RegisterClientEvent;
import Models.Client;
import java.awt.image.PackedColorModel;
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
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class Server implements SocketListener{
    private final int BUFFER_SIZE = 1024;
    private DatagramSocket serverSocket;
    private boolean verificando = false;
    
    private Map<Integer, Client> Clientes = new HashMap<>();
    
    public Server(int port){
        runServer(port);
    }
    private void runServer(int port){
        try {
            serverSocket = new DatagramSocket(port);
            System.out.println("Servidor iniciado en el puerto " + port);
            ReceivePacketsThread recibirCliente = new ReceivePacketsThread(serverSocket, BUFFER_SIZE);
            recibirCliente.addSocketListener(this);
            recibirCliente.start();
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*------------------------------------------------------------------------*/
    private void sendMessage(Client destino,int ID_origen, String mensaje){
        HashMap<String, String> info = new HashMap<>();
        info.put("IDSOURCE", String.valueOf(ID_origen));
        info.put("IDDEST", String.valueOf(destino.getID()));
        if(ID_origen != 0){
            Client origen = this.Clientes.get(ID_origen);
            info.put("NICK", origen.getNick());
        }
        mensaje = Parse.convertInfoToMessage(info, mensaje);
        this.sendMessage(destino.getAddress(), destino.getPort(), mensaje);
    }
    private void sendMessage(String address, int port, String mensaje){
        try {
            InetAddress destino = InetAddress.getByName(address);
            byte buffer_output[] = mensaje.getBytes();
            DatagramPacket output_packet = new DatagramPacket(buffer_output, buffer_output.length, destino, port);
            this.serverSocket.send(output_packet);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void sendMessageBroadCast(String mensaje, int ID_origen) {
        for(Client cliente : Clientes.values()){
            sendMessage(cliente, ID_origen, mensaje);
        }
    }
    private void commenzarAVerificar(){
        VerifyClientThread verificador = new VerifyClientThread(Clientes, serverSocket);
        verificador.addSocketListener(this);
        verificador.start();
        verificando = true;
    }
    /*------------------------------------------------------------------------*/
    @Override
    public void onReceiveMessage(ReceiveMessageEvent event) {
        Map<String, String> info = Parse.convertMessageToInfo(event.getMensaje());
        this.sendMessageBroadCast(info.get("MSJ"), Integer.parseInt(info.get("ID")));
    }
    
    @Override
    public void onRegisterClient(RegisterClientEvent event) {
        String addressCliet = event.getInputPacket().getAddress().getCanonicalHostName();
        int portClient = event.getInputPacket().getPort();
        Client nuevoCliente = new Client(event.getID(), event.getNick(), addressCliet, portClient);
        Clientes.put(nuevoCliente.hashCode(), nuevoCliente);
        
        System.out.println(String.format("[Servidor]: cliente %s registrado", nuevoCliente.toString()));
        String mensaje = String.format("El cliente %s a ingresado al Chat", nuevoCliente.toString());
        sendMessageBroadCast(mensaje, 0);
        if(!verificando) this.commenzarAVerificar();
    }

    @Override
    public void onClientArrives(ClientArriveEvent event) {
        String address = event.getInputPacket().getAddress().getCanonicalHostName();
        int port = event.getInputPacket().getPort();
        
        HashMap<String, String> info = new HashMap<>();
        int ID = (address + String.valueOf(port)).hashCode();
        info.put("ID", String.valueOf(ID));
        String mensaje = Parse.convertInfoToMessage(info, "");
        
        this.sendMessage(address, port, mensaje);
    }

    @Override
    public void onReceivePing(ReceivePingEvent event) {
        System.out.println("ping recivido");
        Client cliente = Clientes.get(event.getID());
        cliente.setActivo(true);
        cliente.setIntertosDeConexion(0);
    }

    @Override
    public void onInactiveClient(InactiveClientEvent event) {
        Client cliente  = Clientes.remove(event.getID());
        System.out.println("se ha desconectado");
        this.sendMessageBroadCast(cliente.getNick()+" se ha desconectado del Chat", 0);;
    }

    

    

    
}
