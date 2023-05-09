/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClienteSocket;

import ClientEvents.ClientListener;
import ClientEvents.ConnectionEstablishedEvent;
import ClientEvents.LostConnectionEvent;
import ClientEvents.ReceiveMessageEvent;
import ClientEvents.TryConnectionEvent;
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
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("SALA_ESPERA")) notificarEntradaSala(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("BATALLA")) notificarInicioBatalla(info);
        else if(info.containsKey("TYPE") && info.get("TYPE").equals("RESULTADO_DISPARO")) notificarResultadoDisparo(info);
        else System.out.println(info);
    }
    private void notificarResultadoDisparo(Map<String, String> info){
        String resulatado = info.get("RESULTADO");
        int fila = Integer.parseInt(info.get("FILA"));
        int columna = Integer.parseInt(info.get("COLUMNA"));
        ViewEvents.ResultadoDisparoEvent view_event = new ViewEvents.ResultadoDisparoEvent(resulatado  , fila, columna, this);
        this.notifyResulatadoDisparoEvent(view_event);
    }
    public void empezarCrearTablero(HashMap<String, String> info){
        int dimencion = Integer.parseInt(info.get("DIM"));
        this.notifyGameStartEvent(new CrearTableroEvent(dimencion, this));
    }
    public void dispararA(int contrincante_session_id, int fila, int columna){
        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(this.sesion_id));
            info.put("TYPE", "DISPARAR_A");
            info.put("CONTRINCANTE", String.valueOf(contrincante_session_id));
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
        int contrincante_session_id = Integer.parseInt(info.get("CONTRINCANTE_ID"));
        String contrincante_nick = info.get("CONTRINCANTE_NICK");
        ViewEvents.StartBatallaEvent view_event = new ViewEvents.StartBatallaEvent(contrincante_session_id, contrincante_nick, this);
        this.notifyStartBatallaEvent(view_event);
    }
    private void notificarEntradaSala(HashMap<String, String> info) {
        ViewEvents.SalaEsperaEvent view_event = new ViewEvents.SalaEsperaEvent(this);
        this.notifySalaEsperaEvent(view_event);
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
    public void notifyStartBatallaEvent(ViewEvents.StartBatallaEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ViewListener.class){
                ((ViewListener) listeners[i+1]).onStartBatalla(event);
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

    
    
}
