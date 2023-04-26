/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Events;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class DataEvent extends EventObject{
    String mensaje;
    int clienteKey;
    public DataEvent(Object source, String mensaje, int clienteKey) {
        super(source);
        this.mensaje = mensaje;
        this.clienteKey = clienteKey;
    }
    public String getMesasge(){
        return mensaje;
    }
    public int getClienteKey(){
        return clienteKey;
    }
}
