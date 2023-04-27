package ServerSocket;

import Models.Sesion;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIRKO
 */
public class SendMessageTask extends Thread{
    Sesion sesion;
    String mensaje;

    public SendMessageTask(Sesion sesion, String mensaje) {
        this.sesion = sesion;
        this.mensaje = mensaje;
    }
    
    @Override
    public void run() {
        try {
            DataOutputStream salida = new DataOutputStream(sesion.getSocket().getOutputStream());
            salida.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(SendMessageTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
