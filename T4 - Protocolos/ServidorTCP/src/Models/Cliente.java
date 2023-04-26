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
public class Cliente {
    private int key;
    private String nick;
    private Socket socket;
    
    
    public Cliente(){
    }
    public Cliente(int key, String nick, Socket socket){
        this.key = key;
        this.nick = nick;
        this.socket = socket;
    }

    public int getKey() {
        return key;
    }

    public String getNick() {
        return nick;
    }

    public Socket getSocket() {
        return socket;
    }
    
    @Override
    public String toString(){
        return "ID: " + this.key+ " nick: " + this.nick;
    }
}
