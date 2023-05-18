/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TableroEvents;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface TableroListener extends EventListener{
    public void onDisparoEvent(DisparoEvent event);
    public void onEliminoBarco(EliminoBarcoEvent event);
    public void onPerdio(PerdioEvent event);
}
