package ClienteSocket;

/**
 *
 * @author MIRKO
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClienteTCP cliente = new ClienteTCP("127.0.0.1", 5000);
    }
}
