/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package GameEvents;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface ServerGameListener extends EventListener{
    public void onReceiveMessage(ReceiveMessageEvent event);
    public void onNewSession(NewSessionEvent event);
}
