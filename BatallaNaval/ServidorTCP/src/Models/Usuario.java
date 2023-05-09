/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.net.Socket;

/**
 *
 * @author MIRKO
 */
public class Usuario {
    private int sesion_id;
    private String nick;
    private String password;
    private boolean onLine;
    private Tablero tablero;
    public String estado; // inactivo, enSala, enPartida

    public Usuario(int sesion_id, String nick, String password) {
        this.sesion_id  = sesion_id;
        this.nick = nick;
        this.password = password;
        this.tablero = new Tablero();
        this.onLine = false;
        estado = "inactivo";
    }

    public int getSesionId() {
        return this.sesion_id;
    }
    public void setSesionId(int sesion_id){
        this.sesion_id = sesion_id;
    }
    public String getNick() {
        return nick;
    }
    public Tablero getTablero(){
        return this.tablero;
    }
    public String getPassword() {
        return password;
    }
    

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }
    
    @Override
    public int hashCode() {
        return (this.nick + this.password).hashCode();
    }
    
    @Override
    public String toString(){
        return String.format("{sesion_id: %s, NICK: %s, PASSWORD: %s}", this.sesion_id, this.nick, this.password);
    }
    
}
