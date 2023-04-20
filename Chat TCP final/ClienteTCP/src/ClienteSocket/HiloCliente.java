package ClienteSocket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.MessageUtil;

/**
 *
 * @author MIRKO
 */
public class HiloCliente extends Thread{
    private Socket socket;
    private int id;
    public HiloCliente(int id, Socket socket) {
        this.socket = socket;
        this.id = id;
    }
    @Override
    public void run() {
        DataInputStream entrada;
        try {
            entrada = new DataInputStream(socket.getInputStream());
            while (true) {
                String mensaje = entrada.readUTF();
                Map<String, String> datos = MessageUtil.convertMessageToInfo(mensaje);
                int sourceid = Integer.parseInt(datos.get("ID"));
                if(sourceid != id){
                    String origen = datos.get("NICK");
                    String MSJ = datos.get("MSJ");
                    System.out.println("["+origen+"]:"+MSJ);
                }
            }           
        } catch (IOException ex) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
