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

    public Usuario(int sesion_id, String nick, String password) {
        this.sesion_id  = sesion_id;
        this.nick = nick;
        this.password = password;
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

    public String getPassword() {
        return password;
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
