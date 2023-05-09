/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ViewEvents;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface ViewListener extends EventListener{
    public void onConnected(ConnectedEvent event);
    public void onReconnected(ReconnectedEvent event);
    public void onTryConnection(TryConnectionEvent event);
    public void onCrearTablero(CrearTableroEvent event);
    public void onStartBatalla(StartBatallaEvent event);
    public void onSalaEspera(SalaEsperaEvent event);
    public void onResultadoDisparo(ResultadoDisparoEvent event);
}
