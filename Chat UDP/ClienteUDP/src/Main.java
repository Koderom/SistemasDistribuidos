/**
 *
 * @author MIRKO
 */
import ClientUDP.*;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            Client cliente = new Client(args[0], Integer.parseInt(args[1]) );
        }catch(Exception ex){
            Client cliente = new Client("localhost", 6000 );
        }
    }
    
}
