/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package CrearTableroEvents;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface CrearTableroListener extends EventListener{
    public void onEntrarSalaEspera(EntrarSalaEsperaEvent event);
}
