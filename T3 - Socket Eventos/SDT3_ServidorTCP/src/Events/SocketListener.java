/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Events;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface SocketListener extends EventListener{
    public void onClientConnected(ConnectionEvent event);
    public void onReadMessage(DataEvent event);
}
