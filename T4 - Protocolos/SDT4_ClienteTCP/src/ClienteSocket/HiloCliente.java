package ClienteSocket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                Map<String, String> datos = getInfoCabezera(mensaje);
                int sourceid = Integer.parseInt(datos.get("SOURCEID"));
                if(sourceid != id){
                    String origen = datos.get("SOURCENICK");
                    String MSJ = datos.get("MSJ");
                    System.out.println("["+origen+"]:"+MSJ);
                }
            }           
        } catch (IOException ex) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*------------------------------------------------------------------------*/
    private Map<String, String> getInfoCabezera(String mensaje){
        Map<String, String> cabezera = new HashMap<>();
        int info_ini = mensaje.indexOf("<");
        int info_fin = mensaje.indexOf(">");
        if(info_ini != -1 && info_fin != -1){
            String info = mensaje.substring(info_ini+1, info_fin);
            String datos[] = info.split("/");
            for(String dato: datos){
                String campo_valor[] = dato.split(",");
                cabezera.put(campo_valor[0], campo_valor[1]);
            }
        }
        String MSJ = mensaje.substring(info_fin+1, mensaje.length());
        cabezera.put("MSJ",MSJ);
        return cabezera;
    }
}
