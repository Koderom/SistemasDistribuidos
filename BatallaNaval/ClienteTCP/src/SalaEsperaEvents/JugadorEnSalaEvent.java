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
public class JugadorEnSalaEvent extends EventObject{
    int session_id;
    String nick;
    String rol;
    boolean listo;

    public JugadorEnSalaEvent(int session_id, String nick, String rol, boolean listo, Object source) {
        super(source);
        this.session_id = session_id;
        this.nick = nick;
        this.rol = rol;
        this.listo = listo;
    }

    public boolean isListo() {
        return listo;
    }

    public String getRol() {
        return rol;
    }

    

    public String getNick() {
        return nick;
    }

    public int getSession_id() {
        return session_id;
    }
    
    
}
