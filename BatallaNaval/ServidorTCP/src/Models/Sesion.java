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
public class Sesion {
    int ID;
    Socket socket;
    
    public Sesion(int ID, Socket socket){
        this.ID = ID;
        this.socket = socket;
    }

    public int getID() {
        return ID;
    }

    public Socket getSocket() {
        return socket;
    }
}
