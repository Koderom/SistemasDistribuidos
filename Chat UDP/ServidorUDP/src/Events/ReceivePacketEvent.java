/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Events;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class ReceivePacketEvent extends EventObject {
    private DatagramPacket inputPacket;
    private InetAddress address;
    private int port;
    private String mensaje;

    public ReceivePacketEvent(DatagramPacket inputPacket, InetAddress address, int port, String mensaje, Object source) {
        super(source);
        this.inputPacket = inputPacket;
        this.address = address;
        this.port = port;
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
    

    public InetAddress getAddress() {
        return address;
    }
    
    public int getPort() {
        return port;
    }

    public DatagramPacket getInputPacket() {
        return inputPacket;
    }
    
}
