/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BatallaEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class SiguienteTurnoEvent extends EventObject{
    String turno;

    public SiguienteTurnoEvent(String turno, Object source) {
        super(source);
        this.turno = turno;
    }

    public String getTurno() {
        return turno;
    }
    
    
}
