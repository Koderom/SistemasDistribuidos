/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClientEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class ReceiveMessageEvent extends EventObject{
    String mensaje;

    public ReceiveMessageEvent(String mensaje, Object source) {
        super(source);
        this.mensaje = mensaje;
    }


    public String getMensaje() {
        return mensaje;
    }
    
}
