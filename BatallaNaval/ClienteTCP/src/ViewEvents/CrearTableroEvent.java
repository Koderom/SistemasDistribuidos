/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ViewEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class CrearTableroEvent extends EventObject{
    int dimencion;

    public CrearTableroEvent(int dimencion, Object source) {
        super(source);
        this.dimencion = dimencion;
    }

    public int getDimencion() {
        return dimencion;
    }
    
    
}
