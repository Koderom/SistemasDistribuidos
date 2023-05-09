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
public class StartBatallaEvent extends EventObject{
    int contrincante_session_id;
    String contrincante_nick;

    public StartBatallaEvent(int contrincante_session_id, String contrincante_nick, Object source) {
        super(source);
        this.contrincante_session_id = contrincante_session_id;
        this.contrincante_nick = contrincante_nick;
    }

    public int getContrincante_session_id() {
        return contrincante_session_id;
    }

    public String getContrincante_nick() {
        return contrincante_nick;
    }

    
    
}
