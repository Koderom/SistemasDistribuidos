/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SocketEvents;

import java.net.Socket;
import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class ConnectionEvent extends EventObject {
    private Socket socket;
   
    
    public ConnectionEvent(Object source, Socket cliente) {
        super(source);
        this.socket = cliente;
    }

    public Socket getSocket() {
        return socket;
    }

}
