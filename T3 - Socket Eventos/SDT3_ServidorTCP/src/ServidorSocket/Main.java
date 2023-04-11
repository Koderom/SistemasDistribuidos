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
        if(args.length == 0)ServidorTCP.correrServidor(5000);
        else ServidorTCP.correrServidor(Integer.parseInt(args[0]));
    }
    
}
