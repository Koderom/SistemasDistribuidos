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
        try{
            ClienteTCP cliente = new ClienteTCP(args[0], Integer.parseInt(args[1]));
        }catch(Exception ex){
            ClienteTCP cliente = new ClienteTCP("127.0.0.1", 5000);
        }
        
    }
}
