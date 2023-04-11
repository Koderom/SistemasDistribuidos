/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Events;

import java.net.Socket;
import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class ConnectionEvent extends EventObject {
    private Socket socket;
    public ConnectionEvent(Object source,Socket socket) {
        super(source);
        this.socket = socket;
    }
    public Socket getClient(){
        return socket;
    }
}
