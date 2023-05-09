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
}
