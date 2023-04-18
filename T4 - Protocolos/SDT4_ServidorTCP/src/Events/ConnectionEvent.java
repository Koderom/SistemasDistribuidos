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
public class ConnectionEvent extends EventObject {
    private Socket cliente;
    private int id;
    
    public ConnectionEvent(Object source, Socket cliente, int id) {
        super(source);
        this.cliente = cliente;
        this.id = id;
    }

    public Socket getCliente() {
        return cliente;
    }

    public int getId() {
        return id;
    }
    
}
