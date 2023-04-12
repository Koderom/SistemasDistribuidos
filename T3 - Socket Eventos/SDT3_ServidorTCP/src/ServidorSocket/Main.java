package ServidorSocket;

/**
 *
 * @author MIRKO
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServidorTCP servido = new ServidorTCP();
        servido.correrServidor(5000);
    }
    
}
