/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class ReceiveMessageEvent extends EventObject {
    String mensaje;
    int ID;
    
    public ReceiveMessageEvent(int ID, String mensaje, Object source) {
        super(source);
        this.ID = ID;
        this.mensaje = mensaje;
    }

    public int getID() {
        return ID;
    }
    
    public String getMensaje(){
        return this.mensaje;
    }
}
