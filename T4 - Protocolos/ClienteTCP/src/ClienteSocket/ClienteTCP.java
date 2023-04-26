/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClienteSocket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.MessageUtil;

/**
 *
 * @author MIRKO
 */
public class ClienteTCP {
    Socket socket;
    int ID;
    
    public ClienteTCP(String ip, int puerto){
        correrCliente(ip, puerto);
    }
    
    public  void correrCliente(String ip, int puerto){
        
        BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream salida;
        DataInputStream entrada;
        try {
            socket = new Socket(ip, puerto);
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
            registrarCliente(entrada, salida, consola);
            HiloCliente cliente = new HiloCliente(ID, socket);
            cliente.start();
            while (true) {
                String texto = consola.readLine();
                Map<String, String> info = new HashMap<>();
                info.put("ID", String.valueOf(this.ID));
                String mensajeFormateado = MessageUtil.convertInfoToMessage( info, texto);
                salida.writeUTF(mensajeFormateado);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /*------------------------------------------------------------------------*/
    private void registrarCliente(DataInputStream entrada, DataOutputStream salida, BufferedReader consola){
        try {
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
            String mensaje = entrada.readUTF();
            Map<String, String> datos = MessageUtil.convertMessageToInfo(mensaje);
            this.ID = Integer.parseInt(datos.get("ID"));
            System.out.println("Tu ID en el servidor es: "+ this.ID+", Ingrese un nombre de usuario");
            String nick = consola.readLine();
            nick = nick.length() == 0 ? "" : nick;
            Map<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(ID));
            info.put("NICK", nick);
            salida.writeUTF(MessageUtil.convertInfoToMessage(info, ""));
            mensaje = entrada.readUTF();
            datos = MessageUtil.convertMessageToInfo(mensaje);
            if(datos.containsKey("ERROR")){
                System.out.println("ERROR : "+datos.get("ERROR"));
                registrarCliente(entrada, salida, consola);
            }else {
                String origen = datos.get("NICK");
                String MSJ = datos.get("MSJ");
                System.out.println("["+origen+"]:"+MSJ);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
