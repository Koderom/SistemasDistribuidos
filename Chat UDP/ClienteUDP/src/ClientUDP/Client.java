/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClientUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIRKO
 */
public class Client {
    private String server_address;
    private int server_port;
    private int buffer_size = 1024;
    private DatagramSocket socket;

    public Client(String address, int port){
        this.server_address = address;
        this.server_port = port;
        runClient();
    }
    private void runClient(){
        Scanner consola = new Scanner(System.in);
        
        try {
            InetAddress serverAddress = Inet4Address.getByName(server_address);
            socket = new DatagramSocket();
            DatagramPacket outputPacket;
            
            ReceivePacketsThread inputPacket = new ReceivePacketsThread(socket, buffer_size);
            inputPacket.start();
            
            while(true){
                String mensaje = consola.nextLine();
                byte[] outputBuffer = mensaje.getBytes();
                outputPacket = new DatagramPacket(outputBuffer, outputBuffer.length, serverAddress, server_port);
                socket.send(outputPacket);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }catch (SocketException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
