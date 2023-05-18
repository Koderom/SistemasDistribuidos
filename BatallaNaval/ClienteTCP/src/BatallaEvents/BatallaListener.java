/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package BatallaEvents;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface BatallaListener extends EventListener{
    public void onResultadoDisparo(ResultadoDisparoEvent event);
    public void onSiguienteTurno(SiguienteTurnoEvent event);
    public void onBarcoEliminado(BarcoEliminadoEvent event);
    public void onJugadorPerdio(JugadorPerdioEvent event);
    public void onGanador(GanadorEvent event);
}
