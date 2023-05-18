/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package SalaEsperaEvents;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface SalaEsperaListener extends EventListener{
    public void onEmpezarBatalla(EmpezarBatallaEvent event);
    public void onJugadorEnSala(JugadorEnSalaEvent event);
    public void onJugadorListo(JugadorListoEvent event);
    public void onPuedeComenzar(PuedeComenzarEvent event);
}
