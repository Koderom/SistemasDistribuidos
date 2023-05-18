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
import TableroEvents.DisparoEvent;
import TableroEvents.EliminoBarcoEvent;
import TableroEvents.PerdioEvent;
import TableroEvents.TableroListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class ServerGame implements ServerGameListener, TableroListener{
    ServerTCP server_socket;
    HashMap<Integer, Usuario> Usuarios;
    LinkedList<String> turnos;
    
    public ServerGame(int puerto){
        Usuarios = new HashMap<>();
        this.server_socket = new ServerTCP(puerto);
        server_socket.addServerGameListener(this);
        turnos = new LinkedList<>();
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
        else if (info.containsKey("TYPE") && info.get("TYPE").equals("JUGADOR_SALA_LISTO")) this.jugadorEnSalaListo(info);
        else if (info.containsKey("TYPE") && info.get("TYPE").equals("EMPEZAR_BATALLA")) this.empezarBatalla(info);
        else if (info.containsKey("TYPE") && info.get("TYPE").equals("DISPARAR")) this.disparar(info);
        else this.enviarMensaje(mensaje, id);
    }
    private void empezarBatalla(Map<String, String> info){
        String primero = turnos.remove();
        turnos.add(primero);
        
        for(Usuario user : Usuarios.values()){
            if(!user.isEnSala()) continue;
            info = new HashMap<>();
            info.put("ID", String.valueOf(user.getSesionId()));
            info.put("TYPE", "EMPEZAR_BATALLA");
            info.put("PRIMER_TURNO", primero);
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.server_socket.sendMessage(mensaje, user.getSesionId());
        }
    }
    private void jugadorEnSalaListo(Map<String, String> info){
        int session_id = Integer.parseInt(info.get("ID"));
        Usuario user = getUser(session_id);
        user.setListo(true);
        user.getTablero().addTableroListener(this);
        turnos.add(user.getNick());
        
        notificarJugadorEnSalaListo(session_id);
        if(sePuedeEmpezar()) permitirEmpezar();
    }
    private void permitirEmpezar(){
        Usuario master = null;
        for(Usuario user : Usuarios.values())
            if(user.rol.equals("master")) master = user;
        HashMap<String, String> info = new HashMap<>();
        info.put("ID", String.valueOf(master.getSesionId()));
        info.put("TYPE", "PUEDE_COMENZAR");
        String mensaje = Parse.convertInfoToMessage(info, "");
        this.server_socket.sendMessage(mensaje, master.getSesionId());
    }
    private boolean sePuedeEmpezar(){
        int cantidad_jugadores_listos = 0;
        for(Usuario user : Usuarios.values()){
            if(!user.isEnSala()) continue;
            if(user.isListo()) cantidad_jugadores_listos++;
        }
        return (cantidad_jugadores_listos == usuariosEnSala() && cantidad_jugadores_listos > 1);
    }
    private void notificarJugadorEnSalaListo(int session_id){
        Usuario usaurio_listo = getUser(session_id);
        for(Usuario user : Usuarios.values()){
            if(!user.isOnLine()) continue;
            if(!user.isEnSala()) continue;
            
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(user.getSesionId()));
            info.put("TYPE", "JUGADOR_SALA_LISTO");
            info.put("NICK", usaurio_listo.getNick());
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.server_socket.sendMessage(mensaje, user.getSesionId());
        }
    }
     public void disparar(Map<String, String> info){
        int session_id = Integer.parseInt(info.get("ID"));
        int fila = Integer.parseInt(info.get("FILA"));
        int columna = Integer.parseInt(info.get("COLUMNA"));
        String objetivo = info.get("OBJETIVO");
        Usuario autor = getUser(session_id);
        
        Usuario usuario = getUser(objetivo);
        usuario.getTablero().ejecutarDisparo(autor.getNick(), objetivo, fila, columna);
    }
    private void notificarSiguienteTurno(){
        String turno = this.turnos.remove();
        this.turnos.add(turno);
        System.out.println("siguiente turno: " + turno);
        for(Usuario u : Usuarios.values()){
            if(!u.isEnSala()) continue;
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(u.getSesionId()));
            info.put("TYPE", "SIGUIENTE_TURNO");
            info.put("TURNO", turno);
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.server_socket.sendMessage(mensaje, u.getSesionId());
        }
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
        String password = info.get("PASSWORD");
        String nick = info.get("NICK");
        Usuario user = this.getUser(nick);
        
        if(password.length() == 0)this.enviarMensajeError(id, 300, "La contraseña es requerida");
        else if(nick.length() == 0) this.enviarMensajeError(id, 300, "La nombre de usario es requerido");
        else if(user == null) this.enviarMensajeError(id, 300, "La contraseña y el nombre de usuario no coinsiden");
        else if(!user.getPassword().equals(password)) this.enviarMensajeError(id, 300, "La contraseña y el nombre de usuario no coisiden");
        else{
            int sesion_id = Integer.parseInt(info.get("ID"));
            user.setSesionId(sesion_id);
            user.setOnLine(true);
            this.enviarMensajeSimple(id, 200, "Logeado exitosamente");
            Datos database = new Datos();
            database.updateUsers(Usuarios);
        }
    }
    public void enviarMensajeError(int destino_session_id, int code, String mensajeError){
        HashMap<String, String> info = new HashMap<>();
        info.put("COD", String.valueOf(code));
        info.put("TYPE", "ERROR");
        info.put("ERRO_MESSAGE", mensajeError);
        String msj = Parse.convertInfoToMessage(info, "");
        this.server_socket.sendMessage(msj, destino_session_id);
    }
    private Usuario getUser(String nick){
        for(Usuario user : Usuarios.values()){
            if(user.getNick().equals(nick)) return user;
        }
        return null;
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
        user.setEnSala(true);
        System.out.println(usuariosEnSala());
        if(usuariosEnSala() == 1){
            agregarASalaComoMaster(session_id);
            user.rol = "master";
        }else{
            agregarASala(session_id);
            user.rol = "jugador";
            notificarEntrada(session_id);
        }
    }
    private void agregarASalaComoMaster(int session_id){
        Usuario user = getUser(session_id);
        
        HashMap<String, String> info = new HashMap<>();
        info.put("ID", String.valueOf(session_id));
        info.put("NICK", user.getNick());
        info.put("TYPE", "ENTRAR_SALA");
        info.put("ROL", "MASTER");
        String mensaje = Parse.convertInfoToMessage(info, "");
        
        this.server_socket.sendMessage(mensaje, session_id);
    }
    private void agregarASala(int session_id){
        Usuario user = getUser(session_id);
        
        HashMap<String, String> info = new HashMap<>();
        info.put("ID", String.valueOf(session_id));
        info.put("NICK", user.getNick());
        info.put("TYPE", "ENTRAR_SALA");
        info.put("ROL", "JUGADOR");
        String mensaje = Parse.convertInfoToMessage(info, "");
        
        this.server_socket.sendMessage(mensaje, session_id);
        enviarListaDeConectadosA(session_id);
    }
    private void enviarListaDeConectadosA(int session_id){
        Usuario nuevo_en_sala = getUser(session_id);
        EnviarListaDeSalaTask task = new EnviarListaDeSalaTask(server_socket, Usuarios, nuevo_en_sala);
        task.start();
    }
    private void notificarEntrada(int session_id){
        Usuario nueno_en_sala = getUser(session_id);
        NotificarEntradaEnSalaTask task = new NotificarEntradaEnSalaTask(server_socket, Usuarios, nueno_en_sala);
        task.start();
    }
    private void notificarEspera(int session_id){
        HashMap<String, String> info = new HashMap<>();
        info.put("ID", String.valueOf(session_id));
        info.put("TYPE", "SALA_ESPERA");
        String mensaje = Parse.convertInfoToMessage(info, "");
        this.server_socket.sendMessage(mensaje, session_id);
    }
    
    private int usuariosEnSala(){
        int count = 0;
        for(Usuario user : Usuarios.values())
            if(user.isEnSala()) count++;
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
    private void notificarJugadorPerdio(String nick){
        
        for(Usuario usuario : Usuarios.values()){
            if(!usuario.isEnSala()) continue;
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(usuario.getSesionId()));
            info.put("TYPE", "JUGADOR_PERDIO");
            info.put("JUGADOR", nick);
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.server_socket.sendMessage(mensaje, usuario.getSesionId());
        }
    }
    
    private void notificarGanador(){
        String nick = turnos.remove();
        for(Usuario usuario : Usuarios.values()){
            if(!usuario.isEnSala()) continue;
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(usuario.getSesionId()));
            info.put("TYPE", "GANADOR");
            info.put("JUGADOR", nick);
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.server_socket.sendMessage(mensaje, usuario.getSesionId());
        }
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

    @Override
    public void onDisparoEvent(DisparoEvent event) {
        for(Usuario usuario : Usuarios.values()){
            if(!usuario.isEnSala()) continue;
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(usuario.getSesionId()));
            info.put("TYPE", "RESULTADO_DISPARO");
            info.put("FIL", String.valueOf(event.getFila()));
            info.put("COL", String.valueOf(event.getColumna()));
            info.put("RESULTADO", String.valueOf(event.getResultado()));
            info.put("OBJETIVO", event.getNick_objetivo());
            info.put("AUTOR", event.getNick_autor());
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.server_socket.sendMessage(mensaje, usuario.getSesionId());
        }
        notificarSiguienteTurno();
    }

    @Override
    public void onEliminoBarco(EliminoBarcoEvent event) {
        for(Usuario usuario : Usuarios.values()){
            if(!usuario.isEnSala()) continue;
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(usuario.getSesionId()));
            info.put("TYPE", "BARCO_ELIMINADO");
            info.put("FIL", String.valueOf(event.getFila()));
            info.put("COL", String.valueOf(event.getColumna()));
            info.put("SIZE", String.valueOf(event.getSize()));
            info.put("ORIENTACION", String.valueOf(event.getOrientacion()));
            info.put("OBJETIVO", event.getNick_objetivo());
            info.put("AUTOR", event.getNick_autor());
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.server_socket.sendMessage(mensaje, usuario.getSesionId());
        }
        Usuario objetivo = getUser(event.getNick_objetivo());
        if(objetivo.getTablero().perdio()){
            this.turnos.remove(objetivo.getNick());
            notificarJugadorPerdio(objetivo.getNick());
            if(turnos.size() == 1){
                notificarGanador();
            }
        }
    }
    
    @Override
    public void onPerdio(PerdioEvent event) {
        this.turnos.remove(event.getNick());
        for(Usuario usuario : Usuarios.values()){
            if(!usuario.isEnSala()) continue;
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(usuario.getSesionId()));
            info.put("TYPE", "JUGADOR_PERDIO");
            info.put("JUGADOR", event.getNick());
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.server_socket.sendMessage(mensaje, usuario.getSesionId());
        }
    }

    
    
}
