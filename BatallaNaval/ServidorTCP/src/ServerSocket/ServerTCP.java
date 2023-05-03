package ServerSocket;

import GameEvents.NewSessionEvent;
import GameEvents.ReceiveMessageEvent;
import GameEvents.ServerGameListener;
import SocketEvents.DisconnectEvent;
import SocketEvents.ConnectionEvent;
import SocketEvents.SocketListener;
import SocketEvents.ReceiveDataEvent;
import SocketEvents.UserRegistrationEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import Models.Usuario;
import Models.Sesion;
import java.io.DataOutputStream;
import java.util.Random;
import javax.swing.event.EventListenerList;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class ServerTCP implements SocketListener{
    public HashMap<Integer, Sesion> Sesiones = new HashMap<>();
    ServerSocket servidor;
    int puerto;
    
    public ServerTCP(int puerto){
        this.puerto = puerto;
        this.correrServidor();
    }
    
    public void correrServidor() {
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("Servidor TCP iniciado en el puerto "+puerto+"...");
            AttendConnectionTheread conexiones = new AttendConnectionTheread(servidor);
            conexiones.addSocketListener(this);
            conexiones.start();
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*------------------------------------------------------------------------*/
    boolean verificando = false;
    private void startToVerify(){
        VerifyConnectionThread verificando = new VerifyConnectionThread(Sesiones);
        verificando.addSocketListener(this);
        verificando.start();
        this.verificando = true;
    }
    public int generarId(){
        int ID = (int)(Math.random()*1000+1);
        if(!this.Sesiones.containsKey(ID))  return ID;
        return generarId();
    }
    public void sendMessage(String mensaje, int id_destino){
        Sesion destino = Sesiones.get(id_destino);
        SendMessageTask task = new SendMessageTask(destino, mensaje);
        task.start();
    }
    
    /*------------------------------------------------------------------------*/
    @Override
    public void onClientConnected(ConnectionEvent event) {
        int ID = this.generarId();
        Sesion nuevaSesion = new Sesion(ID, event.getSocket());
        this.Sesiones.put(ID, nuevaSesion);
        
        NewSessionEvent gameEvent = new NewSessionEvent(ID, this);
        this.notifyNewSessionEvent(gameEvent);  
        
        AttendSessionThread cliente = new AttendSessionThread(event.getSocket(), ID);
        cliente.addSocketListener(this);
        cliente.start();
        
        this.startToVerify();
    }
    
    @Override
    public void onReceiveData(ReceiveDataEvent event) {
        int id = event.getId();
        String mensaje = event.getMesasge();
        ReceiveMessageEvent gameEvent = new ReceiveMessageEvent(id, mensaje, this);
        this.notifyReceiveMessageEvent(gameEvent);
    }

    @Override
    public void onClientDisconnect(DisconnectEvent event) {
//        Cliente cl = Clientes.get(event.getKey());
//        try {
//            cl.getSocket().close();
//            Clientes.remove(cl.getKey());
//            //((AttendClientThread)event.getSource()).interrupt();
//            this.sendMessageBroadcast(0, cl.getNick() + " se ha desconectado de servidor");
//        } catch (IOException ex) {
//            Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void onRegisteredUser(UserRegistrationEvent event) {
//        Cliente cliente = new Cliente(event.getID(), event.getNick(), event.getSocket());
//        Clientes.put(cliente.getKey(), cliente);
//        this.sendMessage(0, cliente.getKey(), "registrado correctamente");
//        if(!verificando)this.startToVerify();
//        this.sendMessageBroadcast(0, "Usuario "+cliente.getNick()+" se ha unido al chat");
    }
    /*
    @Override
    public void onConnectionInterrupted(DisconnectEvent event) {
        try {
            Cliente cl = Clientes.remove(event.getKey());
            cl.getSocket().close();
            this.sendMessageBroadcast(0, cl.getNick() + " ha perdido la conexion");
        } catch (IOException ex) {
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    /*------------------------------------------------------------------------*/
    protected EventListenerList listenerList = new EventListenerList();
    
    public void addServerGameListener(ServerGameListener listener){
        listenerList.add(ServerGameListener.class, listener);
    }
    public void removeServerGameListener(ServerGameListener listener){
        listenerList.remove(ServerGameListener.class, listener);
    }
    public void notifyReceiveMessageEvent(ReceiveMessageEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ServerGameListener.class){
                ((ServerGameListener) listeners[i+1]).onReceiveMessage(event);
            }
        }
    }
    public void notifyNewSessionEvent(NewSessionEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if(listeners[i] == ServerGameListener.class){
                ((ServerGameListener) listeners[i+1]).onNewSession(event);
            }
        }
    }

    
}
