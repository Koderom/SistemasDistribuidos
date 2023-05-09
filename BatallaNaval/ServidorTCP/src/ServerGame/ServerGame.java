/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerGame;

import Data.Datos;
import GameEvents.NewSessionEvent;
import GameEvents.ReceiveMessageEvent;
import GameEvents.ServerGameListener;
import GameEvents.UserDisconnectEvent;
import Models.Tablero;
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
        else if (info.containsKey("TYPE") && info.get("TYPE").equals("RECONT")) this.reconectarUsuario(info);
        else if (info.containsKey("TYPE") && info.get("TYPE").equals("CREAR_TABLERO")) this.crearTablero(info);
        else if (info.containsKey("TYPE") && info.get("TYPE").equals("COLOCAR_BARCO")) this.colocarBarco(info);
        else if (info.containsKey("TYPE") && info.get("TYPE").equals("SALA_ESPERA")) this.entrarSalaEspera(info);
        else if (info.containsKey("TYPE") && info.get("TYPE").equals("DISPARAR_A")) this.dispararA(info);
        else this.enviarMensaje(mensaje, id);
    }
    public void dispararA(Map<String, String> info){
        int session_id = Integer.parseInt(info.get("ID"));
        int contrincante_session_id = Integer.parseInt(info.get("CONTRINCANTE"));
        int fila = Integer.parseInt(info.get("FILA"));
        int columna = Integer.parseInt(info.get("COLUMNA"));
        Usuario user = this.getUser(contrincante_session_id);
        String resultado = user.getTablero().ejecutarDisparo(fila, fila);
        System.out.println(user.getTablero().toString());
        this.informarResultadoDisparo(resultado, fila, columna, session_id, contrincante_session_id);
    }
    private void informarResultadoDisparo(String resulatdo,int fila, int columna, int session_id, int contrincante_session_id){
        HashMap<String, String> info = new HashMap<>();
        info.put("ID", String.valueOf(session_id));
        info.put("CONTRINCANTE", String.valueOf(contrincante_session_id));
        info.put("TYPE", "RESULTADO_DISPARO");
        info.put("RESULTADO", resulatdo);
        info.put("FILA", String.valueOf(fila));
        info.put("COLUMNA", String.valueOf(columna));
        String mensaje = Parse.convertInfoToMessage(info, "");
        this.server_socket.sendMessage(mensaje, session_id);
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
        if(nick.length() == 0) this.enviarMensajeSimple(id, 300, "Nombre de usuario demasiado corto");
        else if(password.length() == 0) this.enviarMensajeSimple(id, 300, "No se ha ingresado el password");
        else if(existeUsuario(nick)) this.enviarMensajeSimple(id, 300, "Nombre de usuario no disponible");
        else{
            Usuario nuevoUsuario = new Usuario(id, nick, password);
            nuevoUsuario.setOnLine(true);
            this.Usuarios.put(nuevoUsuario.hashCode(), nuevoUsuario);
            this.enviarMensajeSimple(id, 200, "Registrado correctamente");
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
                    usuario.setOnLine(true);
                    this.enviarMensajeSimple(id, 200, "Logeado exitosamente");
                    //
                    Datos database = new Datos();
                    database.updateUsers(Usuarios);
                    //
                    System.out.println(String.format("usuario %s logeado exitosamente", usuario));
                }else{
                    this.enviarMensajeSimple(id, 300, "La clave y la contraseña no coinciden");
                }
                return;
            }
        }
        this.enviarMensajeSimple(id, 300, "La clave y la contraseña no coinciden");
    }
    public void reconectarUsuario(Map<String, String> info){
        String nick = info.get("NICK");
        int sesion_id = Integer.parseInt(info.get("ID"));
        for(Usuario usuario : this.Usuarios.values()){
            if(info.get("NICK").equals(usuario.getNick())){
                usuario.setSesionId(sesion_id);
                usuario.setOnLine(true);
                this.enviarMensajeSimple(sesion_id, 201, "reconectado");
            }
        }
        this.enviarMensajeSimple(sesion_id, 300, "Usuario desconocido");
    }
    public void crearTablero(Map<String, String> info){
        int sesion_id = Integer.parseInt(info.get("ID"));
        empezarCrearTablero(sesion_id);
    }
    public void empezarCrearTablero(int session_id){
        HashMap<String, String> info = new HashMap<>();
        info.put("ID", String.valueOf(session_id));
        info.put("TYPE", "CREAR_TABLERO");
        info.put("DIM", String.valueOf(Tablero.dimencion));
        String mensaje = Parse.convertInfoToMessage(info, "");
        this.server_socket.sendMessage(mensaje, session_id);
        
    }
    public void colocarBarco(Map<String, String> info){
        int id_session = Integer.parseInt(info.get("ID"));
        
        int fil = Integer.parseInt(info.get("FIL"));
        int col = Integer.parseInt(info.get("COL"));
        int size = Integer.parseInt(info.get("SIZE"));
        char orientacion = info.get("ORIENTACION").charAt(0);
        
        Usuario user = this.getUser(id_session);
        if(user == null) return;// de momento
        user.getTablero().colocarBarco(fil, col, size, orientacion);
        System.out.println(user.getTablero().toString());
    }
    public void enviarMensaje(String mensaje, int id){
        this.server_socket.sendMessage(mensaje, id);
    }
    //solo con el parametro COD y un mensaje
    public void enviarMensajeSimple(int id, int code, String mensaje){
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
    public Usuario getUser(int id_session){
        for(Usuario user : this.Usuarios.values()){
            if(user.getSesionId() == id_session) return user;
        }
        return null;
    }
    public int cantidadUsuarioEnlinea(){
        int res = 0;
        for(Usuario user : this.Usuarios.values()) 
            if(user.isOnLine()) res++;
        return res;
    }
    private void entrarSalaEspera(Map<String, String> info) {
        int session_id = Integer.parseInt(info.get("ID"));
        Usuario user = this.getUser(session_id);
        user.estado = "enSala";
        if(usuariosEnSala()>= 2) empezarBatalla();
        else notificarEspera(session_id);
    }
    private void notificarEspera(int session_id){
        HashMap<String, String> info = new HashMap<>();
        info.put("ID", String.valueOf(session_id));
        info.put("TYPE", "SALA_ESPERA");
        String mensaje = Parse.convertInfoToMessage(info, "");
        this.server_socket.sendMessage(mensaje, session_id);
    }
    private void empezarBatalla(){
        for(Usuario user : Usuarios.values()){
            if(user.estado.equals("enSala")){
                for(Usuario contrincante : Usuarios.values()){
                    if(!contrincante.equals(user)){
                        asignarContrincante(user, contrincante);
                        asignarContrincante(contrincante, user);
                        user.estado = "enPartida";
                        contrincante.estado = "enPartida";
                        break;
                    } 
                }
            }
        }
    }
    private int usuariosEnSala(){
        int count = 0;
        for(Usuario user : Usuarios.values())
            if(user.estado.equals("enSala")) count++;
        return count;
    }
    private void asignarContrincante(Usuario usuario, Usuario contrincante){
        HashMap<String, String> info = new HashMap<>();
        info.put("ID", String.valueOf(usuario.getSesionId()));
        info.put("TYPE", "BATALLA");
        info.put("CONTRINCANTE_ID", String.valueOf(contrincante.getSesionId()));
        info.put("CONTRINCANTE_NICK", contrincante.getNick());
        String mensaje = Parse.convertInfoToMessage(info, "");
        this.server_socket.sendMessage(mensaje, usuario.getSesionId());
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

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        Usuario user = this.getUser(event.getSessionId());
        user.setOnLine(false);
        System.out.println("usuario desconectado");
    }

    
    
}
