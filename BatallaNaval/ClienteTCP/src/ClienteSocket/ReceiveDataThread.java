package ClienteSocket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class ReceiveDataThread extends Thread{
    private Socket socket;
    private int id;
    public ReceiveDataThread(int id, Socket socket) {
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
                Map<String, String> datos = Parse.convertMessageToInfo(mensaje);
                System.out.println("-> "+datos.get("MSJ"));
            }           
        } catch (IOException ex) {
            System.out.println("se cayo el servidor XD");
            //Logger.getLogger(ReceiveDataThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
