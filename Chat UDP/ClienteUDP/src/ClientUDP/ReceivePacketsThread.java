/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClientUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIRKO
 */
public class ReceivePacketsThread extends Thread {
    DatagramSocket socket;
    int buffer_size;
    public ReceivePacketsThread(DatagramSocket socket, int buffer_size){
        this.socket = socket;
        this.buffer_size = buffer_size;
    }

    @Override
    public void run() {
        while(!interrupted()){
            try {
                byte[] inputBuffer = new byte[buffer_size];
                DatagramPacket inputPacket = new DatagramPacket(inputBuffer, buffer_size);
                socket.receive(inputPacket);
                String mensaje = new String(inputPacket.getData());
                System.out.println("recibido: "+mensaje);
            } catch (IOException ex) {
                Logger.getLogger(ReceivePacketsThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
