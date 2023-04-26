package Models;

import java.io.Serializable;

/**
 *
 * @author MIRKO
 */
public class Mensaje implements Serializable{
    String mensaje;
    int origen;
    int destino;

    public Mensaje(String mensaje, int origen, int destino) {
        this.mensaje = mensaje;
        this.origen = origen;
        this.destino = destino;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getOrigen() {
        return origen;
    }

    public int getDestino() {
        return destino;
    }
    
    
}
