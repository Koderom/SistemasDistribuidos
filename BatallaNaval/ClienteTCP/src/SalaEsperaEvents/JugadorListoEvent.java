/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SalaEsperaEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class JugadorListoEvent extends EventObject {
    String nick;

    public JugadorListoEvent(String nick, Object source) {
        super(source);
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
    
    
}
