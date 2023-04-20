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
    private String address;
    private int port;
    
    public Client(String address, int port){
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
    
    public boolean isSame(String address, int port){
        return (this.address == address && this.port == port);
    }

    @Override
    public int hashCode() {
        return (address + String.valueOf(port)).hashCode();
    }
    
    
}
