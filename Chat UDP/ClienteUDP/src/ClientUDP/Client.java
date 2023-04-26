/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClientUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Parse;
import events.ReceivePingEvent;
import events.ClientListener;

/**
 *
 * @author MIRKO
 */
public class Client implements ClientListener{
    private final String server_address;
    private final int server_port;
    private final int buffer_size = 1024;
    
    private DatagramSocket socket;
    private InetAddress serverAddress;
    
    public int ID;
    
    public Client(String address, int port){
        this.server_address = address;
        this.server_port = port;
        this.iniciarServidor();
        this.runClient();
    }
    private void iniciarServidor(){
        try {
            this.socket = new DatagramSocket();
            this.serverAddress = InetAddress.getByName(server_address);
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void runClient(){
        System.out.println("Corriendo cliente...");
        Scanner consola = new Scanner(System.in);
        this.iniciarRegistro(consola);
        ReceivePacketsThread inputPacket = new ReceivePacketsThread(socket, buffer_size, this.ID);
        inputPacket.addClientListener(this);
        inputPacket.start();
        while(true){
            String mensaje = consola.nextLine();
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(ID));
            info.put("TYPE", "MSJ");
            mensaje = Parse.convertInfoToMessage(info, mensaje);
            this.sendMessage(mensaje);
        }
    }
    
    private void sendMessage(String mensaje){
        try {
            byte[] outputBuffer = mensaje.getBytes();
            DatagramPacket outputPacket = new DatagramPacket(outputBuffer, outputBuffer.length, serverAddress, server_port);
            socket.send(outputPacket);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String receiveMessage(){
        String mensaje = "";
        try {
            byte[] inputBuffer = new byte[buffer_size];
            DatagramPacket inputPacket = new DatagramPacket(inputBuffer, buffer_size);
            this.socket.receive(inputPacket);
            mensaje = new String(inputPacket.getData());
            return mensaje;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    private void iniciarRegistro(Scanner consola){
        // ----------> HI
        Map<String, String> info = new HashMap<>();
        info.put("TYPE", "HI");
        String mensaje = Parse.convertInfoToMessage(info, "");
        this.sendMessage(mensaje);
        // <---------- ID
        mensaje = this.receiveMessage();
        info = Parse.convertMessageToInfo(mensaje);
        this.ID = Integer.parseInt(info.get("ID"));
        System.out.println("Bienvenido, tu ID en el servidor es [" + ID + "] , Ingrese un nombre de usuario:");
        // ------------> ID, NICK
        info = new HashMap<>();
        info.put("ID", String.valueOf(ID));
        String nick = consola.nextLine();
        info.put("NICK", nick);
        mensaje = Parse.convertInfoToMessage(info, "");
        this.sendMessage(mensaje);
    }

    @Override
    public void onReceivePing(ReceivePingEvent event) {
        HashMap<String, String> info = new HashMap<>();
        info.put("TYPE", "PING");
        info.put("ID", String.valueOf(ID));
        String mensaje = Parse.convertInfoToMessage(info, "");
        this.sendMessage(mensaje);
    }
}
