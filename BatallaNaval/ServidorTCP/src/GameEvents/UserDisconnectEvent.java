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
public class UserDisconnectEvent extends EventObject{
    int session_id;

    public UserDisconnectEvent(int session_id, Object source) {
        super(source);
        this.session_id = session_id;
    }
    public int getSessionId(){
        return this.session_id;
    }
}
