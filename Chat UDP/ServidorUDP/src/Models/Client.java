/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.net.DatagramSocket;

/**
 *
 * @author MIRKO
 */
public class Client {
    int ID;
    String nick;
    private String address;
    private int port;
    private boolean activo;
    private int intertosDeConexion;

    public Client(int ID, String nick, String address, int port) {
        this.ID = ID;
        this.nick = nick;
        this.address = address;
        this.port = port;
        this.activo = true;
        this.intertosDeConexion = 0;
    }
    
    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public int getID() {
        return ID;
    }

    public String getNick() {
        return nick;
    }
    
    public boolean isSame(String address, int port){
        return (this.address == address && this.port == port);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIntertosDeConexion() {
        return intertosDeConexion;
    }

    public void setIntertosDeConexion(int intertosDeConexion) {
        this.intertosDeConexion = intertosDeConexion;
    }
    
    @Override
    public int hashCode() {
        return (address + String.valueOf(port)).hashCode();
    }
    
    @Override
    public String toString(){
        return this.nick;
    }
    
    
}
