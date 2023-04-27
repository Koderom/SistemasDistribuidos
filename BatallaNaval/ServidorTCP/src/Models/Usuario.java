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
    private int id;
    private String nick;
    private String password;

    public Usuario(int id, String nick, String password) {
        this.id = id;
        this.nick = nick;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }
    
    @Override
    public String toString(){
        return String.format("{ID: %s, NICK: %s, PASSWORD: %s}", this.id, this.nick, this.password);
    }
    
}
