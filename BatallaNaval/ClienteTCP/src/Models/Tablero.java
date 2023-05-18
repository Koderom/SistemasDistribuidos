package Models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author MIRKO
 */

public class Tablero {
    public static final int dimencion = 10;
    HashMap<Character, Barco>  barcos;
    char casillas[][]; // O -> oculto, B -> barco
    
    public Tablero(){
        this.casillas = new char[dimencion][dimencion];
        barcos = new HashMap<>();
        
        for(int i = 0; i < this.dimencion; i++)
            for(int j = 0; j < this.dimencion; j++)
                casillas[i][j] = 'O';
    }

    public HashMap<Character, Barco> getBarcos() {
        return barcos;
    }
    
    public int getDimencion() {
        return dimencion;
    }
    
    public void verificarDisparo(int x, int y){
    
    }

    public char[][] getCasillas() {
        return casillas;
    }
    final String piezas = "ABCDEFGHI";
    public boolean colocarBarco(int fil, int col, int size, char orientacion){
        if(barcos.size() == piezas.length()) return false;
        
        char tabla[][] = this.casillas.clone();
        
        if(fil < 0 || col < 0 || fil >= dimencion || col >= dimencion) return false;
        switch (orientacion) {
            case 'V':
                for(int i = 0; i < size; i++){
                    if(casillas[fil+i][col] != 'O'){
                        this.casillas = tabla;
                        return false;
                    }
                    casillas[fil+i][col] = piezas.charAt(barcos.size());
                }
                break;
            case 'H':
                for(int j = 0; j < size; j++){
                    if(casillas[fil][col+j] != 'O'){
                        this.casillas = tabla;
                        return false;
                    }
                    casillas[fil][col+j] = piezas.charAt(barcos.size());
                } 
                break;
        }
        barcos.put(piezas.charAt(barcos.size()), new Barco(fil, col, size, orientacion));
        
        return true;
    }
    
    public boolean acertoDisparo(int x, int y ){
        return casillas[x][y] != 'O';
    }
    
//    public String ejecutarDisparo(int x, int y){
//        if(!acertoDisparo(x, y)) return "fallo";
//        else{
//            Barco nave = getBarco(x, y);
//            this.destruirBarco(x, y);
//            return nave.toString();
//        }
//    }
    
//    public Barco getBarco(int fil, int col){
//        for(Barco barco : barcos.values()){
//            if(barco.orientacion == 'H'){
//                if(fil != barco.x) break;
//                for(int i = barco.x ; i < barco.x + barco.size; i++)
//                    if(i == col) return barco;
//            }else if(barco.orientacion == 'V'){
//                if(col != barco.y) break;
//                for(int j = barco.y; j < barco.y + barco.size; j++)
//                    if(j == fil) return barco;
//            }
//        }
//        return null;
//    }
    
//    public void destruirBarco(int fil, int col){
//        Barco barco = this.getBarco(fil, col);
//        if(barco == null) return;
//        if(barco.orientacion == 'V')
//            for(int i = barco.x ; i < barco.x + barco.size; i++)
//                this.casillas[i][col] = 'X';
//        if(barco.orientacion == 'H')
//            for(int j = barco.x ; j < barco.y + barco.size; j++)
//                this.casillas[fil][j] = 'X';
//        this.barcos.remove(barco);
//    }
    
//    public boolean perdio(){
//        return barcos.isEmpty();
//    }
    
    @Override
    public String toString(){
        String s = "";
        for(int i = 0; i < dimencion; i++){
            for(int j = 0; j < dimencion; j++)
                s += String.valueOf(casillas[i][j])+"  ";
            s += "\n";
        }
        return s;
    }
    
    
    
    
}
