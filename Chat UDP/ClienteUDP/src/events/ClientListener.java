/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package events;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface ClientListener extends EventListener{
    public void onReceivePing(ReceivePingEvent event);
}
