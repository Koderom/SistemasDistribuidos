import Data.Datos;
import Models.Barco;
import Models.Tablero;
import ServerGame.ServerGame;
import ServerSocket.ServerTCP;

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
            ServerGame game = new ServerGame(Integer.parseInt(args[0]));
        }catch(Exception ex){
            ServerGame game = new ServerGame(5000);
        }
    }
    
}
