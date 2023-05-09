/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClientEvents;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class ConnectionEstablishedEvent extends EventObject {
    Socket socket;

    public ConnectionEstablishedEvent(Socket socket, Object source) {
        super(source);
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
    
    
}
