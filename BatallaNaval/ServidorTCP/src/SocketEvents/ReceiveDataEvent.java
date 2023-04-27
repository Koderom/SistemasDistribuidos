/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SocketEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class ReceiveDataEvent extends EventObject{
    String mensaje;
    int id;
    
    public ReceiveDataEvent(Object source, String mensaje, int id) {
        super(source);
        this.mensaje = mensaje;
        this.id = id;
    }
    public String getMesasge(){
        return mensaje;
    }
    public int getId(){
        return id;
    }
}
