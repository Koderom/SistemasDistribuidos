/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Events;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class ClientArriveEvent extends EventObject{
    DatagramPacket inputPacket;
    DatagramSocket socket;

    public ClientArriveEvent(DatagramPacket inputPacket, DatagramSocket socket, Object source) {
        super(source);
        this.inputPacket = inputPacket;
        this.socket = socket;
    }

   
    public DatagramPacket getInputPacket() {
        return inputPacket;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
    
    
    
}
