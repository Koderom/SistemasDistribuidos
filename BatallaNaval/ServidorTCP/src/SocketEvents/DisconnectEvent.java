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
    int key;
    public DisconnectEvent(Object source, int key) {
        super(source);
        this.key = key;
    }
    public int getKey() {
        return key;
    }
    @Override
    public Object getSource() {
        return source;
    }
}
