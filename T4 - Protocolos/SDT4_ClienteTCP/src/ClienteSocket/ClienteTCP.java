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
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author MIRKO
 */
public class ClienteTCP {
    Socket socket;
    int id;
    public ClienteTCP(String ip, int puerto){
        correrCliente(ip, puerto);
    }
    public  void correrCliente(String ip, int puerto){
        
        BufferedReader consola_input = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream salida;
        DataInputStream entrada;
        try {
            socket = new Socket(ip, puerto);
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
            
            // Registrar usuario
            String mensaje = entrada.readUTF();
            Map<String, String> datos = getInfoCabezera(mensaje);
            this.id = Integer.parseInt(datos.get("ID"));
            System.out.println("Tu ID en el servidor es: "+ this.id);
            System.out.println("Introduce un nombre de usuario");
            String nick = consola_input.readLine();
            nick = nick.length() == 0 ? "unnamed_"+id : nick;
            salida.writeUTF("<ID,"+this.id+"/NICK,"+nick+">");
            //
            HiloCliente cliente = new HiloCliente(id, socket);
            cliente.start();
            
            while (true) {
                String texto = consola_input.readLine();
                salida.writeUTF(texto);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /*------------------------------------------------------------------------*/
    private Map<String, String> getInfoCabezera(String mensaje){
        Map<String, String> cabezera = new HashMap<>();
        int info_ini = mensaje.indexOf("<");
        int info_fin = mensaje.indexOf(">");
        if(info_ini != -1 && info_fin != -1){
            String info = mensaje.substring(info_ini+1, info_fin);
            String datos[] = info.split("/");
            for(String dato: datos){
                String campo_valor[] = dato.split(",");
                cabezera.put(campo_valor[0], campo_valor[1]);
            }
        }
        return cabezera;
    }
}
