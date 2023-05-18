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
    String nick_autor;
    String nick_objetivo;
    int fila;
    int columna;
    char resultado;

    public ResultadoDisparoEvent(String nick_autor, String nick_objetivo, int fila, int columna, char resultado, Object source) {
        super(source);
        this.nick_autor = nick_autor;
        this.nick_objetivo = nick_objetivo;
        this.fila = fila;
        this.columna = columna;
        this.resultado = resultado;
    }

    public String getNick_autor() {
        return nick_autor;
    }

    public String getNick_objetivo() {
        return nick_objetivo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public char getResultado() {
        return resultado;
    }
          
    
        
    
}
