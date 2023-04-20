/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author MIRKO
 */

import ServerUDP.*;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            Server servidor = new Server(Integer.parseInt(args[0]));
        }catch(Exception ex){
            Server servidor = new Server(6000);
        }
    }
    
}
