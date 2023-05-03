package Models;

import java.util.ArrayList;

/**
 *
 * @author MIRKO
 */
public class Tablero {
    final int id;
    final int dimencion;
    Usuario usuario;
    ArrayList<Barco> barcos;
    char casillas[][]; // O -> oculto, A-> agua, I -> impacto
    
    public Tablero(int dimencion){
        this.casillas = new char[dimencion][dimencion];
        this.dimencion = dimencion;
        this.id = (int)(Math.random() * 10);
        for(int i = 0; i < this.dimencion; i++)
            for(char c : casillas[i]) c = 'O';
    }
    public void colocarBarco(){
            
    }
    public void verificarDisparo(int x, int y ){
        
    }
    public boolean perdio(){
        return false;
    }
}
