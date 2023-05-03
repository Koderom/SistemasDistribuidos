package Models;

/**
 *
 * @author MIRKO
 */
public class Barco {
    int largo, posx, posy;
    char orientacion;
    int vidas;
    
    public Barco(){
        this.largo = 3;
        this.vidas = largo;
    }
    public boolean acerto(){
        return true;
    }
    public boolean estaHundido(){
        return vidas == 0;
    }
    
}
