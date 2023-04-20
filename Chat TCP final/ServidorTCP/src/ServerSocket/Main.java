package ServerSocket;

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
            ServidorTCP servidor = new ServidorTCP(Integer.parseInt(args[0]));
        }catch(Exception ex){
            ServidorTCP servidor = new ServidorTCP(5000);
        }
        
    }
    
}
