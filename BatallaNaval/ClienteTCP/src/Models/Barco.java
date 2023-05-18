package Models;

/**
 *
 * @author MIRKO
 */
public class Barco{
        public int x, y, size;
        public char orientacion;
        public int vidas;
        
        public Barco(int x, int y, int size, char orientacion) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.vidas = size;
            this.orientacion = orientacion;
        }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public char getOrientacion() {
        return orientacion;
    }

    public int getVidas() {
        return vidas;
    }
        
        
        
        @Override
        public boolean equals(Object o){
            if(!(o instanceof Barco)) return false;
            Barco temp = (Barco)o; 
            return (this.x == temp.x && this.y == temp.y && this.orientacion == temp.orientacion && this.size == temp.size);
        }
        @Override
        public String toString(){
            return String.format("%s:%s:%s:%s", orientacion, size, x, y);
        }
    }
