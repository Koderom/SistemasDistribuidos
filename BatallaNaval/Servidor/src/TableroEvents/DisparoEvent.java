/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TableroEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class DisparoEvent extends EventObject{
    int fila;
    int columna;
    char resultado;
    String nick_autor;
    String nick_objetivo;

    public DisparoEvent(int fila, int columna, char resultado, String nick_autor, String nick_objetivo, Object source) {
        super(source);
        this.fila = fila;
        this.columna = columna;
        this.resultado = resultado;
        this.nick_autor = nick_autor;
        this.nick_objetivo = nick_objetivo;
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

    public String getNick_autor() {
        return nick_autor;
    }

    public String getNick_objetivo() {
        return nick_objetivo;
    }
    
}
