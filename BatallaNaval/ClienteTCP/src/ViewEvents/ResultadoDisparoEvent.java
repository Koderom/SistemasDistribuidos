/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ViewEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class ResultadoDisparoEvent extends EventObject{
    String resultado;
    int x,y;

    public ResultadoDisparoEvent(String resultado, int x, int y, Object source) {
        super(source);
        this.resultado = resultado;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public String getResultado() {
        return resultado;
    }
    
    
}
