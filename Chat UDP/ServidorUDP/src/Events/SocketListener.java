/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Events;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface SocketListener extends EventListener {
    public void onReceiveMessage(ReceiveMessageEvent event);
    public void onClientArrives(ClientArriveEvent event);
    public void onRegisterClient(RegisterClientEvent event);
    public void onReceivePing(ReceivePingEvent event);
    public void onInactiveClient(InactiveClientEvent event);
}
