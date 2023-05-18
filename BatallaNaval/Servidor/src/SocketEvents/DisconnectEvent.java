/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SocketEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class DisconnectEvent extends EventObject {
    int session_id
            ;
    public DisconnectEvent(Object source, int session_id) {
        super(source);
        this.session_id = session_id;
    }
    public int getSession_id() {
        return session_id;
    }
    @Override
    public Object getSource() {
        return source;
    }
}
