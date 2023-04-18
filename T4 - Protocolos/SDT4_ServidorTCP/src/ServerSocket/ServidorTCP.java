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
            HiloConexiones conexiones = new HiloConexiones(servidor);
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
        try {
            DataOutputStream salida = new DataOutputStream(clienteDestino.getSocket().getOutputStream());
            if(origen == 0) salida.writeUTF("<SOURCEID,"+0+"/SOURCENICK,servidor>" + mensaje);
            else salida.writeUTF("<SOURCEID,"+clienteOrigen.getKey()+"/SOURCENICK,"+clienteOrigen.getNick()+">" + mensaje);
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
        if(!verificando){
            VerifyConnectionThread verificando = new VerifyConnectionThread(Clientes);
            verificando.addSocketListener(this);
            verificando.start();
            this.verificando = true;
        }
    }
    
    /*------------------------------------------------------------------------*/
    @Override
    public void onClientConnected(ConnectionEvent event) {
        HiloDatosTCP cliente = new HiloDatosTCP(event.getCliente(), event.getId());
        cliente.addSocketListener(this);
        cliente.start();
    }
    
    @Override
    public void onReadMessage(DataEvent event) {
        Cliente origen = Clientes.get(event.getClienteKey());
        sendMessageBroadcast(origen.getKey(), event.getMesasge());
    }

    @Override
    public void onClientDisconnect(DisconnectEvent event) {
        Cliente cl = Clientes.get(event.getKey());
        System.out.println("se desconecto alguien "+event.getKey());
        try {
            cl.getSocket().close();
            Clientes.remove(cl.getKey());
            ((HiloDatosTCP)event.getSource()).interrupt();
            this.sendMessageBroadcast(0, cl.getNick() + " se ha desconectado de servidor");
        } catch (IOException ex) {
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error en desconectar evento");
        }
    }

    @Override
    public void onRegisteredUser(UserRegistrationEvent event) {
        Cliente nuevoCliente = event.getCliente();
        Clientes.put(nuevoCliente.getKey(), nuevoCliente);
        System.out.println("Cliente "+nuevoCliente.getKey()+" se unio al chat");
        this.startToVerify();
        this.sendMessageBroadcast(0, "Usuario "+nuevoCliente.getNick()+" se ha unido al chat");
    }

    @Override
    public void onConnectionInterrupted(DisconnectEvent event) {
        Cliente cl = Clientes.remove(event.getKey());
        this.sendMessageBroadcast(0, cl.getNick() + " ha perdido la conexion");
    }
}
