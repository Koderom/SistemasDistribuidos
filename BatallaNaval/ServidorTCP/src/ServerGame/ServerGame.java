/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerGame;

import GameEvents.NewSessionEvent;
import GameEvents.ReceiveMessageEvent;
import GameEvents.ServerGameListener;
import Models.Usuario;
import ServerSocket.ServerTCP;
import java.util.HashMap;
import java.util.Map;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class ServerGame implements ServerGameListener{
    ServerTCP server_socket;
    HashMap<Integer, Usuario> Usuarios;
    
    public ServerGame(int puerto){
        Usuarios = new HashMap<>();
        this.server_socket = new ServerTCP(puerto);
        server_socket.addServerGameListener(this);
    }
    
    public void interpretar(String mensaje, int id){
        Map<String, String> info = Parse.convertMessageToInfo(mensaje);
        if(info.containsKey("NICK") && info.containsKey("PASSWORD")) this.registrarUsuario(info);
        else this.enviarEco(mensaje, id);
    }
    
    public void registrarUsuario(Map<String, String> info){
        int id = Integer.parseInt(info.get("ID"));
        String nick = info.get("NICK");
        String password = info.get("PASSWORD");
        Usuario nuevoUsuario = new Usuario(id, nick, password);
        this.Usuarios.put(id, nuevoUsuario);
        
        System.out.println(String.format("El usuario %s ha sido registrado", nuevoUsuario));
    }
    
    public void enviarEco(String mensaje, int id){
        this.server_socket.sendMessage(mensaje, id);
    }
    
    public void iniciarRegistro(int id){
        HashMap<String, String> info = new HashMap<>();
        info.put("ID", String.valueOf(id));
        String mensaje = Parse.convertInfoToMessage(info, "");
        this.server_socket.sendMessage(mensaje, id);
    }
    
/*----------------------------------------------------------------------------*/
    @Override
    public void onReceiveMessage(ReceiveMessageEvent event) {
        this.interpretar(event.getMensaje(), event.getID());
    }

    @Override
    public void onNewSession(NewSessionEvent event) {
        this.iniciarRegistro(event.getID());
    }
    
}
