/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClienteSocket;

import Models.Mensaje;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIRKO
 */
public class HiloCliente extends Thread{
    
    private Socket socket;

    public HiloCliente(Socket socket) {
        this.socket = socket;
    }
    
    
    @Override
    public void run() {
        ObjectInputStream entrada;
        try {
            entrada = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Mensaje mensaje = (Mensaje)entrada.readObject();
                System.out.println("Cliente "+mensaje.getOrigen()+ " -> "+ mensaje.getMensaje());
            }           
        } catch (IOException ex) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
