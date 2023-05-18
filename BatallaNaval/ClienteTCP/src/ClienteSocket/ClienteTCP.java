/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClienteSocket;

import BatallaEvents.BarcoEliminadoEvent;
import BatallaEvents.BatallaListener;
import BatallaEvents.GanadorEvent;
import BatallaEvents.JugadorPerdioEvent;
import BatallaEvents.SiguienteTurnoEvent;
import ClientEvents.ClientListener;
import ClientEvents.ConnectionEstablishedEvent;
import ClientEvents.LostConnectionEvent;
import ClientEvents.ReceiveMessageEvent;
import ClientEvents.TryConnectionEvent;
import CrearTableroEvents.CrearTableroListener;
import CrearTableroEvents.EntrarSalaEsperaEvent;
import SalaEsperaEvents.EmpezarBatallaEvent;
import SalaEsperaEvents.JugadorListoEvent;
import SalaEsperaEvents.PuedeComenzarEvent;
import SalaEsperaEvents.SalaEsperaListener;
import ViewEvents.ConnectedEvent;
import ViewEvents.CrearTableroEvent;
import ViewEvents.ReconnectedEvent;
import ViewEvents.ViewListener;
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
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class ClienteTCP implements ClientListener{
    int sesion_id;
    public String nick;
    
    boolean registrado = false;
    Socket socket;
    DataOutputStream output_stream;
    DataInputStream input_stream;
    int puerto;
    String address;
    
    public ClienteTCP(String address, int puerto){
        this.puerto = puerto;
        this.address = address;
        nick = "invitado";
        this.connectToServer();
    }
    public void connectToServer(){
        ServerConnectionThread serverConnection = new ServerConnectionThread(address, puerto);
        serverConnection.addSocketListener(this);
        serverConnection.start();
    }
    public void startReadingMessage(){
        ReceiveDataThread escuchar = new ReceiveDataThread(sesion_id, socket);
        escuchar.addSocketListener(this);
        escuchar.start();
    }
    public void setNick(String nick){
        this.registrado = true;
        this.nick = nick;
    }
    /*------------------------------------------------------------------------*/
    public String registrarUsuario(String nick, String password){
        String respuesta = "";
        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(sesion_id));
            info.put("NICK", nick);
            info.put("PASSWORD", password);
            info.put("TYPE", "REG");
            output_stream.writeUTF(Parse.convertInfoToMessage(info, ""));
            
            respuesta = input_stream.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }
    public String reconectar() {
        String respuesta = "";
        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(sesion_id));
            info.put("TYPE", "RECONT");
            info.put("NICK", nick);
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.output_stream.writeUTF(mensaje);
            
            respuesta = input_stream.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }
    public String loggearUsuario(String nick, String password){
        String respuesta = "";
        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(sesion_id));
            info.put("NICK", nick);
            info.put("PASSWORD", password);
            info.put("TYPE", "LOGIN");
            output_stream.writeUTF(Parse.convertInfoToMessage(info, ""));
            
            respuesta = input_stream.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }
    public void crearTablero(){
        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(this.sesion_id));
            info.put("TYPE", "CREAR_TABLERO");
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.output_stream.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void interpretarMensaje(HashMap<String, String> info){
        if(info.containsKey("TYPE") && info.get("TYPE").equals("CREAR_TABLERO")) empezarCrearTablero(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("ENTRAR_SALA")) notificarEntradaSala(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("CONTRINCANTE_EN_SALA")) notificarJugadorEnSala(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("JUGADOR_SALA_LISTO")) notificarJugadorEnSalaListo(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("PUEDE_COMENZAR")) notificarPuedeComenzar(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("EMPEZAR_BATALLA")) notificarInicioBatalla(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("RESULTADO_DISPARO")) notificarResultadoDisparo(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("BARCO_ELIMINADO")) notificarBarcoEliminado(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("JUGADOR_PERDIO")) notificarJugadorPerdio(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("GANADOR")) notificarGanador(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("SIGUIENTE_TURNO")) notificarSiguienteTurno(info);
        else System.out.println(info);
    }
    private void notificarGanador(Map<String, String> info){
        GanadorEvent event = new GanadorEvent(info.get("JUGADOR"), this);
        notifyGanadorEvent(event);
    }
    private void notificarJugadorPerdio(Map<String, String> info){
        JugadorPerdioEvent event = new JugadorPerdioEvent(info.get("JUGADOR"), this);
        notifyJugadorPerdioEvent(event);
    }
    private void notificarBarcoEliminado(Map<String, String> info){
        int fila = Integer.parseInt(info.get("FIL"));
        int columna = Integer.parseInt(info.get("COL"));
        int size = Integer.valueOf(info.get("SIZE"));
        char orientacion = info.get("ORIENTACION").charAt(0);
        BarcoEliminadoEvent event = new BarcoEliminadoEvent(fila, columna, size, orientacion, info.get("OBJETIVO"), info.get("AUTOR"), this);
        notifyBarcoEliminadoEvent(event);
    }
    private void notificarSiguienteTurno(Map<String, String> info){
        SiguienteTurnoEvent event = new SiguienteTurnoEvent(info.get("TURNO"), this);
        notifySiguienteTurnoEvent(event);
    }
    public void empezarBatalla(){
        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(this.sesion_id));
            info.put("TYPE", "EMPEZAR_BATALLA");
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.output_stream.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void notificarPuedeComenzar(Map<String, String> info){
        SalaEsperaEvents.PuedeComenzarEvent sala_event = new PuedeComenzarEvent(this);
        notifyPuedeComenzarEvent(sala_event);
    }
    private void notificarJugadorEnSalaListo(Map<String, String> info){
        String nick = info.get("NICK");
        SalaEsperaEvents.JugadorListoEvent sala_event = new JugadorListoEvent(nick, socket);
        this.notifyJugadorListoEvent(sala_event);
    }
    private void notificarJugadorEnSala(Map<String, String> info){
        int contrincante_session_id = Integer.parseInt(info.get("ID_CONTRINCANTE"));
        String contrincante_nick = info.get("NICK_CONTRINCANTE");
        String rol = info.get("ROL");
        boolean listo = (info.get("LISTO").equals("SI"));
        SalaEsperaEvents.JugadorEnSalaEvent sala_espera_event  = new SalaEsperaEvents.JugadorEnSalaEvent(contrincante_session_id, contrincante_nick, rol, listo, this);
        this.notifyJugadorEnSalaEvent(sala_espera_event);
    }
    private void notificarResultadoDisparo(Map<String, String> info){
        char resulatado = info.get("RESULTADO").charAt(0);
        int fila = Integer.parseInt(info.get("FIL"));
        int columna = Integer.parseInt(info.get("COL"));
        BatallaEvents.ResultadoDisparoEvent batalla_events = new BatallaEvents.ResultadoDisparoEvent(info.get("AUTOR"), info.get("OBJETIVO"), fila, columna, resulatado, this);
        this.notifyResultadoDisparoEvent(batalla_events);        
    }
    public void empezarCrearTablero(HashMap<String, String> info){
        int dimencion = Integer.parseInt(info.get("DIM"));
        this.notifyGameStartEvent(new CrearTableroEvent(dimencion, this));
    }
    public void jugadorEnSalaListo(){
        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(this.sesion_id));
            info.put("TYPE", "JUGADOR_SALA_LISTO");
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.output_stream.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void dispararA(String nick, int fila, int columna){
        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(this.sesion_id));
            info.put("TYPE", "DISPARAR");
            info.put("OBJETIVO", nick);
            info.put("FILA", String.valueOf(fila));
            info.put("COLUMNA", String.valueOf(columna));
            String mensaje = Parse.convertInfoToMessage(info, "");
            
            this.output_stream.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void colocarBarco(int fil, int col, int size, char orientacion){
        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(this.sesion_id));
            info.put("TYPE", "COLOCAR_BARCO");
            info.put("FIL", String.valueOf(fil));
            info.put("COL", String.valueOf(col));
            info.put("SIZE", String.valueOf(size));
            info.put("ORIENTACION", String.valueOf(orientacion));
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.output_stream.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void entrarSalaEspera(){
        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(this.sesion_id));
            info.put("TYPE", "SALA_ESPERA");
            String mensaje = Parse.convertInfoToMessage(info, "");
            this.output_stream.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void notificarInicioBatalla(HashMap<String, String> info){
        String primer_turno = info.get("PRIMER_TURNO");
        SalaEsperaEvents.EmpezarBatallaEvent sala_event = new EmpezarBatallaEvent(primer_turno, this);
        this.notifyEmpezarBatallaEvent(sala_event);
    }
    private void notificarEntradaSala(HashMap<String, String> info) {
        String nick = info.get("NICK");
        String rol = info.get("ROL");
        int session_id = Integer.parseInt(info.get("ID"));
        
        CrearTableroEvents.EntrarSalaEsperaEvent event = new EntrarSalaEsperaEvent(nick, session_id, rol, this);
        this.notifyEntrarSalaEsperaEvent(event);
    }
    @Override
    public void onConnectionEstablished(ConnectionEstablishedEvent event) {
        System.out.println("Conexion establecida");
        try {
            this.socket = event.getSocket();
            this.output_stream = new DataOutputStream(socket.getOutputStream());
            this.input_stream = new DataInputStream(socket.getInputStream());
            
            String mesaje = input_stream.readUTF();
            Map<String, String> info = Parse.convertMessageToInfo(mesaje);
            this.sesion_id = Integer.parseInt(info.get("ID"));
            
            if(!registrado){
                ConnectedEvent view_event = new ConnectedEvent(this);
                notifyConnectionEvent(view_event);
            }else{
                ReconnectedEvent recont_event = new ReconnectedEvent(this);
                notifyReconnectionEvent(recont_event);
            }
        } catch (IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void onReceiveMessage(ReceiveMessageEvent event) {
        HashMap<String, String> info = (HashMap<String,String>)Parse.convertMessageToInfo(event.getMensaje());
        this.interpretarMensaje(info);
    }

    @Override
    public void onLostConnection(LostConnectionEvent event) {
        this.connectToServer();
    }
    @Override
    public void onTryConnection(TryConnectionEvent event) {
        ViewEvents.TryConnectionEvent event_view = new ViewEvents.TryConnectionEvent(this);
        this.notifyTryConnectionEvent(event_view);
    }
    /*-----------------Events-----------------*/
    protected EventListenerList listenerList = new EventListenerList();
    
    public void addSocketListener(ViewListener listener){
        listenerList.add(ViewListener.class, listener);
    }
    public void removeSocketListener(ViewListener listener){
        listenerList.remove(ViewListener.class, listener);
    }
    public void notifyConnectionEvent(ConnectedEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ViewListener.class){
                ((ViewListener) listeners[i+1]).onConnected(event);
            }
        }
    }
    public void notifyReconnectionEvent(ReconnectedEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ViewListener.class){
                ((ViewListener) listeners[i+1]).onReconnected(event);
            }
        }
    }
    public void notifyTryConnectionEvent(ViewEvents.TryConnectionEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ViewListener.class){
                ((ViewListener) listeners[i+1]).onTryConnection(event);
            }
        }
    }
    public void notifyGameStartEvent(ViewEvents.CrearTableroEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ViewListener.class){
                ((ViewListener) listeners[i+1]).onCrearTablero(event);
            }
        }
    }
    public void notifyStartBatallaEvent(ViewEvents.EmpezarBatallaEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ViewListener.class){
                ((ViewListener) listeners[i+1]).onEmpezarBatalla(event);
            }
        }
    }
    public void notifySalaEsperaEvent(ViewEvents.SalaEsperaEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ViewListener.class){
                ((ViewListener) listeners[i+1]).onSalaEspera(event);
            }
        }
    }
    public void notifyResulatadoDisparoEvent(ViewEvents.ResultadoDisparoEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ViewListener.class){
                ((ViewListener) listeners[i+1]).onResultadoDisparo(event);
            }
        }
    }
    /*---------------------------SALA DE ESPERA-------------------------------*/
    public void addSalaEsperaListener(SalaEsperaEvents.SalaEsperaListener listener){
        listenerList.add(SalaEsperaListener.class, listener);
    }
    public void removeSalaEsperaListener(SalaEsperaEvents.SalaEsperaListener listener){
        listenerList.remove(SalaEsperaListener.class, listener);
    }
    public void notifyJugadorEnSalaEvent(SalaEsperaEvents.JugadorEnSalaEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == SalaEsperaListener.class){
                ((SalaEsperaListener) listeners[i+1]).onJugadorEnSala(event);
            }
        }
    }
    public void notifyJugadorListoEvent(SalaEsperaEvents.JugadorListoEvent event){
        Object[] listeners = listenerList .getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == SalaEsperaListener.class){
                ((SalaEsperaListener) listeners[i+1]).onJugadorListo(event);
            }
        }
    }
    public void notifyPuedeComenzarEvent(SalaEsperaEvents.PuedeComenzarEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == SalaEsperaListener.class){
                ((SalaEsperaListener) listeners[i+1]).onPuedeComenzar(event);
            }
        }
    }
    public void notifyEmpezarBatallaEvent(SalaEsperaEvents.EmpezarBatallaEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == SalaEsperaListener.class){
                ((SalaEsperaListener) listeners[i+1]).onEmpezarBatalla(event);
            }
        }
    }
    /*---------------------------CREAR TABLERO-------------------------------*/
    public void addCrearTableroListener(CrearTableroEvents.CrearTableroListener listener){
        listenerList.add(CrearTableroListener.class, listener);
    }
    public void removeCrearTableroListener(CrearTableroEvents.CrearTableroListener listener){
        listenerList.remove(CrearTableroListener.class, listener);
    }
    public void notifyEntrarSalaEsperaEvent(CrearTableroEvents.EntrarSalaEsperaEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == CrearTableroListener.class){
                ((CrearTableroListener) listeners[i+1]).onEntrarSalaEspera(event);
            }
        }
    }
    
    /*-----------------batalla events-----------------------------------------*/
    public void addBatallaListener(BatallaListener listener){
        listenerList.add(BatallaListener.class, listener);
    }
    public void removeBatallaListener(BatallaListener listener){
        listenerList.remove(BatallaListener.class, listener);
    }
    public void notifyResultadoDisparoEvent(BatallaEvents.ResultadoDisparoEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == BatallaListener.class){
                ((BatallaListener) listeners[i+1]).onResultadoDisparo(event);
            }
        }
    }
    public void notifySiguienteTurnoEvent(BatallaEvents.SiguienteTurnoEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == BatallaListener.class){
                ((BatallaListener) listeners[i+1]).onSiguienteTurno(event);
            }
        }
    }
    public void notifyBarcoEliminadoEvent(BatallaEvents.BarcoEliminadoEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == BatallaListener.class){
                ((BatallaListener) listeners[i+1]).onBarcoEliminado(event);
            }
        }
    }
    public void notifyJugadorPerdioEvent(BatallaEvents.JugadorPerdioEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == BatallaListener.class){
                ((BatallaListener) listeners[i+1]).onJugadorPerdio(event);
            }
        }
    }
    
    public void notifyGanadorEvent(BatallaEvents.GanadorEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == BatallaListener.class){
                ((BatallaListener) listeners[i+1]).onGanador(event);
            }
        }
    }
}
