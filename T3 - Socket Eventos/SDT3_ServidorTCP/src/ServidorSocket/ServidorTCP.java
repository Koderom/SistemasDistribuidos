/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServidorSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import Events.*;
/**
 *
 * @author MIRKO
 */
public class ServidorTCP implements SocketListener{
    public Map<Integer, Socket> Clientes = new HashMap<Integer, Socket>();

    @Override
    public void onClientConnected(ConnectionEvent event) {
        int key = Clientes.size()+1;
        Clientes.put(key, event.getClient());
        HiloServidorTCP cliente = new HiloServidorTCP(String.valueOf(key), event.getClient());
        cliente.addSocketListener(this);
        cliente.start();
        System.out.println("Cliente "+key+" añadido al la lista");
    }

    @Override
    public void onReadMessage(DataEvent event) {
        System.out.println("Cliente "+event.getClienteKey()+"-> "+event.getMesasge());
    }
    public void correrServidor(int puerto){
        ServerSocket servidor;
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("Servidor TCP iniciado en el puerto "+puerto+"...");
            //
            HiloConexiones conexiones = new HiloConexiones(servidor);
            conexiones.addSocketListener(this);
            conexiones.start();
            //
        } catch (IOException ex) {
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
