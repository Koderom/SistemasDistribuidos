package Models;

import java.util.ArrayList;

/**
 *
 * @author MIRKO
 */

public class Tablero {
    public static final int dimencion = 10;
    ArrayList<Nave> barcos;
    char casillas[][]; // O -> oculto, B -> barco
    
    public Tablero(){
        this.casillas = new char[dimencion][dimencion];
        barcos = new ArrayList<>();
        
        for(int i = 0; i < this.dimencion; i++)
            for(int j = 0; j < this.dimencion; j++)
                casillas[i][j] = 'O';
    }

    public int getDimencion() {
        return dimencion;
    }

    public ArrayList<Nave> getBarcos() {
        return barcos;
    }

    public char[][] getCasillas() {
        return casillas;
    }
    
    public boolean colocarBarco(int fil, int col, int size, char orientacion){
        char tabla[][] = this.casillas.clone();
        if(fil < 0 || col < 0 || fil >= dimencion || col >= dimencion) return false;
        switch (orientacion) {
            case 'V':
                for(int i = 0; i < size; i++){
                    if(casillas[fil+i][col] == 'B'){
                        this.casillas = tabla;
                        return false;
                    }
                    casillas[fil+i][col] = 'B';
                }
                break;
            case 'H':
                for(int j = 0; j < size; j++){
                    if(casillas[fil][col+j] == 'B'){
                        this.casillas = tabla;
                        return false;
                    }
                    casillas[fil][col+j] = 'B';
                } 
                break;
            default:
                for(int i = 0; i < size; i++) casillas[fil][col+i] = 'B';
        }
        barcos.add(new Nave(fil, col, size, orientacion));
        return true;
    }
    
    public boolean acertoDisparo(int x, int y ){
        return casillas[x][y] == 'B';
    }
    
    public void ejecutarDisparo(int x, int y){
        if(!acertoDisparo(x, y)) return;
        this.eliminarBarco(x, y);
    }
    
    public Nave getNave(int fil, int col){
        for(Nave barco : barcos){
            if(barco.orientacion == 'H'){
                if(fil != barco.x) break;
                for(int i = barco.x ; i < barco.x + barco.size; i++)
                    if(i == col) return barco;
            }else if(barco.orientacion == 'V'){
                if(col != barco.y) break;
                for(int j = barco.y; j < barco.y + barco.size; j++)
                    if(j == fil) return barco;
            }
        }
        return null;
    }
    
    public void eliminarBarco(int fil, int col){
        Nave barco = this.getNave(fil, col);
        if(barco == null) return;
        if(barco.orientacion == 'V')
            for(int i = barco.x ; i < barco.x + barco.size; i++)
                this.casillas[i][col] = 'O';
        if(barco.orientacion == 'H')
            for(int j = barco.x ; j < barco.y + barco.size; j++)
                this.casillas[fil][j] = 'O';
        this.barcos.remove(barco);
    }
    
    public boolean perdio(){
        return barcos.isEmpty();
    }
    
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
    
    
    
    class Nave{
        public int x, y, size;
        public char orientacion;

        public Nave(int x, int y, int size, char orientacion) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.orientacion = orientacion;
        }
        @Override
        public boolean equals(Object o){
            if(!(o instanceof Nave)) return false;
            Nave temp = (Nave)o; 
            return (this.x == temp.x && this.y == temp.y && this.orientacion == temp.orientacion && this.size == temp.size);
        }
        @Override
        public String toString(){
            return String.format("%s%s(%s,%s)", orientacion, size, x, y);
        }
    }
}
