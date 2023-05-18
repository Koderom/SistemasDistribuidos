/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TableroEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class PerdioEvent extends EventObject{
    String nick;

    public PerdioEvent(String nick, Object source) {
        super(source);
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
    
    
}
