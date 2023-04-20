/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Events;

import Models.Cliente;
import java.net.Socket;
import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class UserRegistrationEvent extends EventObject{
    private int ID;
    private String nick;
    private Socket socket;

    public UserRegistrationEvent(int ID, String nick, Socket socket, Object source) {
        super(source);
        this.ID = ID;
        this.nick = nick;
        this.socket = socket;
    }

    public int getID() {
        return ID;
    }

    public String getNick() {
        return nick;
    }

    public Socket getSocket() {
        return socket;
    }

    
}
