
import ServerGame.ServerGame;
import ServerSocket.ServerTCP;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

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
