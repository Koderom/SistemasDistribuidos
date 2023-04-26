/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Events;

import java.net.DatagramPacket;

/**
 *
 * @author MIRKO
 */
public class RegisterClientEvent {
    DatagramPacket inputPacket;
    int ID;
    String nick;
    public RegisterClientEvent(DatagramPacket inputPacket, int ID, String nick) {
        this.inputPacket = inputPacket;
        this.ID = ID;
        this.nick = nick;
    }

    public int getID() {
        return ID;
    }

    public String getNick() {
        return nick;
    }

    public DatagramPacket getInputPacket() {
        return inputPacket;
    }
}
