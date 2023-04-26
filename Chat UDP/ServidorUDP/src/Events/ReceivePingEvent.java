/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Events;

import java.net.DatagramPacket;
import java.util.EventObject;

/**
 *
 * @author MIRKO
 */

public class ReceivePingEvent extends EventObject{
    DatagramPacket input_packet;
    int ID;

    public ReceivePingEvent(DatagramPacket input_packet, int ID, Object source) {
        super(source);
        this.input_packet = input_packet;
        this.ID = ID;
    }

    public DatagramPacket getInput_packet() {
        return input_packet;
    }

    public int getID() {
        return ID;
    }
    
}