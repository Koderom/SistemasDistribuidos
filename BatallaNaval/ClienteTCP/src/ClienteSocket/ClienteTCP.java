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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class ClienteTCP {
    int ID;
    Socket socket;
    DataOutputStream output_stream;
    DataInputStream input_stream;
    
    public ClienteTCP(String ip, int puerto){
        try {
            this.socket = new Socket(ip, puerto);
            this.output_stream = new DataOutputStream(socket.getOutputStream());
            this.input_stream = new DataInputStream(socket.getInputStream());
            correrCliente(ip, puerto);
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    Scanner consola = new Scanner(System.in);
    public  void correrCliente(String ip, int puerto){
        registrarCliente();
        ReceiveDataThread cliente = new ReceiveDataThread(ID, socket);
        cliente.start();
        while (true) {
            try {
                String texto = consola.nextLine();
                Map<String, String> info = new HashMap<>();
                info.put("ID", String.valueOf(this.ID));
                String mensajeFormateado = Parse.convertInfoToMessage( info, texto);
                output_stream.writeUTF(mensajeFormateado);
            } catch (IOException ex) {
                Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    /*------------------------------------------------------------------------*/
    private void registrarCliente(){
        try {
            String mensaje = input_stream.readUTF();
            String tipo = "";
            Map<String, String> datos = Parse.convertMessageToInfo(mensaje);
            this.ID = Integer.parseInt(datos.get("ID"));
            System.out.println("conectado con el servidor");
            
            Map<String, String> info = new HashMap<>();
            while(true){
                System.out.println("para iniciar sesion digite [L], para registrarse [R]");
                String operacion = consola.nextLine();
                if("L".equals(operacion)){
                     tipo = "LOGIN";
                     break;
                }else if("R".equals(operacion)){
                    tipo = "REG";
                    break;
                }else System.out.println("Opcion no valida");
            }
            
            while(true){
                System.out.print("nick: ");
                String nick = consola.nextLine();
                nick = nick.length() == 0 ? "" : nick;
                System.out.print("password: ");
                String password = consola.nextLine();
                password = password.length() == 0 ? "" : password;

                info.put("ID", String.valueOf(ID));
                info.put("NICK", nick);
                info.put("PASSWORD", password);
                info.put("TYPE", tipo);
                output_stream.writeUTF(Parse.convertInfoToMessage(info, ""));
                
                mensaje = input_stream.readUTF();
                info = Parse.convertMessageToInfo(mensaje);
                System.out.println(info.get("MSJ"));
                System.out.println("-> "+mensaje);
                if(info.containsKey("COD") && info.get("COD").equals("200")) return;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
