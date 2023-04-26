package ServerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import Events.*;
import Models.Cliente;
import java.io.DataOutputStream;
import utils.MessageUtil;

/**
 *
 * @author MIRKO
 */
public class ServidorTCP implements SocketListener{
    public Map<Integer, Cliente> Clientes = new HashMap<>();
    ServerSocket servidor;
    boolean verificando = false;
    
    public ServidorTCP(int puerto){
        correrServidor(puerto);
    }
    
    public void correrServidor(int puerto){
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("Servidor TCP iniciado en el puerto "+puerto+"...");
            AttendConnectionTheread conexiones = new AttendConnectionTheread(servidor);
            conexiones.addSocketListener(this);
            conexiones.start();
        } catch (IOException ex) {
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*------------------------------------------------------------------------*/
    private void sendMessage(int origen, int destino, String mensaje){
        Cliente clienteDestino = Clientes.get(destino);
        Cliente clienteOrigen = origen == 0? null : Clientes.get(origen);
        Map<String, String> info = new HashMap<>();
        info.put("ID", String.valueOf(origen));
        if(clienteOrigen != null)info.put("NICK", clienteOrigen.getNick());
        else info.put("NICK", "servidor");
        
        try {
            DataOutputStream salida = new DataOutputStream(clienteDestino.getSocket().getOutputStream());
            String mensajeformateado = MessageUtil.convertInfoToMessage(info, mensaje);
            salida.writeUTF(mensajeformateado);
            System.out.println("enviado : " + mensajeformateado );
        } catch (IOException ex) {
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendMessageBroadcast(int origen, String mensaje){
        for(Integer destino: Clientes.keySet()){
            sendMessage(origen, destino, mensaje);
        }
    }
    private void startToVerify(){
        VerifyConnectionThread verificando = new VerifyConnectionThread(Clientes);
        verificando.addSocketListener(this);
        verificando.start();
        this.verificando = true;
    }
    private int generateId(int idRef){
        String user = "cliente" + idRef + Clientes.size();
        int ID = user.hashCode();
        if(!Clientes.containsKey(ID)) return ID;
        return generateId(ID);
    }
    /*------------------------------------------------------------------------*/
    @Override
    public void onClientConnected(ConnectionEvent event) {
        int ID = generateId(event.getId());
        AttendClientThread cliente = new AttendClientThread(event.getSocket(), ID);
        cliente.addSocketListener(this);
        cliente.start();
    }
    
    @Override
    public void onReadMessage(DataEvent event) {
        System.out.println("se esta intentando enviar mensaje");
        Cliente origen = Clientes.get(event.getClienteKey());
        sendMessageBroadcast(origen.getKey(), event.getMesasge());
    }

    @Override
    public void onClientDisconnect(DisconnectEvent event) {
        Cliente cl = Clientes.get(event.getKey());
        try {
            cl.getSocket().close();
            Clientes.remove(cl.getKey());
            //((AttendClientThread)event.getSource()).interrupt();
            this.sendMessageBroadcast(0, cl.getNick() + " se ha desconectado de servidor");
        } catch (IOException ex) {
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onRegisteredUser(UserRegistrationEvent event) {
        Cliente cliente = new Cliente(event.getID(), event.getNick(), event.getSocket());
        Clientes.put(cliente.getKey(), cliente);
        this.sendMessage(0, cliente.getKey(), "registrado correctamente");
        if(!verificando)this.startToVerify();
        this.sendMessageBroadcast(0, "Usuario "+cliente.getNick()+" se ha unido al chat");
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
}
