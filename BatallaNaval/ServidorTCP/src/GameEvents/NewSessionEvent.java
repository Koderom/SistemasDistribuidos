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
public class NewSessionEvent extends EventObject{
    int ID;
    
    public NewSessionEvent(int ID, Object source) {
        super(source);
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
    
}
