package Models;

import java.util.ArrayList;

/**
 *
 * @author MIRKO
 */

public class Partida {
    ArrayList<Tablero> tableros;
    ArrayList<Movimiento> movimientos;
    int siguienteTurno; 
    public Partida(){
        this.tableros = new ArrayList<>();
        this.movimientos = new  ArrayList<>();
    }
    public void iniciarPartida(){
        
    }
    public void adicionarAPartida(Usuario usuario){
        
    }
    public void solicitarMovimiento(int siguiente){
        
    }
    public boolean existeGanador(){
        return false;
    }    
}


class Movimiento {
    int tablero_id;
    int user_id;
    int x,y;
    String resultado; // impacto, agua
}
