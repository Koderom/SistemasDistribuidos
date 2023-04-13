/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClienteSocket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author MIRKO
 */
public class ClienteTCP {
    public static void correrCliente(){
        BufferedReader consola_input = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream salida;
        DataInputStream entrada;
        Socket socket;
        try {
            socket = new Socket("127.0.0.1", 5000);
            salida = new DataOutputStream(socket.getOutputStream());
            HiloCliente cliente = new HiloCliente(socket);
            cliente.start();
            while (true) {
                String texto = consola_input.readLine();
                salida.writeUTF(texto);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
