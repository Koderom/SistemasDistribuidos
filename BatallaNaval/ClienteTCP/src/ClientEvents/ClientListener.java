/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ClientEvents;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface ClientListener extends EventListener{
    public void onConnectionEstablished(ConnectionEstablishedEvent event);
    public void onReceiveMessage(ReceiveMessageEvent event);
    public void onLostConnection(LostConnectionEvent event);
    public void onTryConnection(TryConnectionEvent event);
}
