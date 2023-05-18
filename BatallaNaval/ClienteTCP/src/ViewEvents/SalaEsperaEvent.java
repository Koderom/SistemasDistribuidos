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
public class SalaEsperaEvent extends EventObject{
    String nick;
    int session_id;
    String rol;

    public SalaEsperaEvent(String nick, int session_id, String rol, Object source) {
        super(source);
        this.nick = nick;
        this.session_id = session_id;
        this.rol = rol;
    }

    public String getNick() {
        return nick;
    }

    public String getRol() {
        return rol;
    }

    public int getSession_id() {
        return session_id;
    }
    
    
    
    
}
