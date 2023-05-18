/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BatallaEvents;

import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class BarcoEliminadoEvent extends EventObject {
    int fila;
    int columna;
    int size;
    char orientacion;
    String nick_objetivo;
    String nick_autor;

    public BarcoEliminadoEvent(int fila, int columna, int size, char orientacion, String nick_objetivo, String nick_autor, Object source) {
        super(source);
        this.fila = fila;
        this.columna = columna;
        this.size = size;
        this.orientacion = orientacion;
        this.nick_objetivo = nick_objetivo;
        this.nick_autor = nick_autor;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getSize() {
        return size;
    }

    public char getOrientacion() {
        return orientacion;
    }

    public String getNick_objetivo() {
        return nick_objetivo;
    }

    public String getNick_autor() {
        return nick_autor;
    }
    
    
}
