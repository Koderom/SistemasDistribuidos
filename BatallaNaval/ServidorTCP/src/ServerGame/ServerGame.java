/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerGame;

import Data.Datos;
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
        //
        Datos database = new Datos();
        this.Usuarios = database.getUsuarios();
        System.out.println("Usuarios cargados de la BD");
        //
    }
    
    public void interpretar(String mensaje, int id){
        System.out.println("recivido: " + mensaje);
        Map<String, String> info = Parse.convertMessageToInfo(mensaje);
        if(info.containsKey("TYPE") && info.get("TYPE").equals("REG")) this.registrarUsuario(info);
        else if (info.containsKey("TYPE") && info.get("TYPE").equals("LOGIN")) this.loginUsuario(info);
        else this.enviarEco(mensaje, id);
    }
    public boolean existeUsuario(String nombre){
        for(Usuario usuario : Usuarios.values()){
            if(usuario.getNick().equals(nombre)) return true;
        }
        return false;
    }
    public void registrarUsuario(Map<String, String> info){
        int id = Integer.parseInt(info.get("ID"));
        String nick = info.get("NICK");
        String password = info.get("PASSWORD");
        Usuario nuevoUsuario = new Usuario(id, nick, password);
        if(nick.length() == 0) this.enviarMensaje(id, 300, "Nombre de usuario demasiado corto");
        else if(password.length() == 0) this.enviarMensaje(id, 300, "No se ha ingresado el password");
        else if(existeUsuario(nick)) this.enviarMensaje(id, 300, "Nombre de usuario no disponible");
        else{
            this.Usuarios.put(nuevoUsuario.hashCode(), nuevoUsuario);
            this.enviarMensaje(id, 200, "Registrado correctamente");
            //
            Datos database = new Datos();
            database.updateUsers(Usuarios);
            //
            System.out.println(String.format("El usuario %s ha sido registrado", nuevoUsuario));
            System.out.println(this.Usuarios);
        }
    }
    
    public void loginUsuario(Map<String, String> info){
        int id = Integer.parseInt(info.get("ID"));
        for(Usuario usuario : this.Usuarios.values()){
            if(info.get("NICK").equals(usuario.getNick())){
                if(info.get("PASSWORD").equals(usuario.getPassword())){
                    int sesion_id = Integer.parseInt(info.get("ID"));
                    usuario.setSesionId(sesion_id);
                    this.enviarMensaje(id, 200, "Logeado exitosamente");
                    //
                    Datos database = new Datos();
                    database.updateUsers(Usuarios);
                    //
                    System.out.println(String.format("usuario %s logeado exitosamente", usuario));
                }else{
                    this.enviarMensaje(id, 300, "La clave y la contraseña no coinciden");
                }
                return;
            }
        }
        this.enviarMensaje(id, 300, "La clave y la contraseña no coinciden");
    }
    
    public void enviarEco(String mensaje, int id){
        this.server_socket.sendMessage(mensaje, id);
    }
    public void enviarMensaje(int id, int code, String mensaje){
        HashMap<String, String> info = new HashMap<>();
        info.put("COD", String.valueOf(code));
        String msj = Parse.convertInfoToMessage(info, mensaje);
        this.server_socket.sendMessage(msj, id);
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
