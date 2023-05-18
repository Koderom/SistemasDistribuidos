/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerGame;

import Models.Usuario;
import ServerSocket.ServerTCP;
import java.util.ArrayList;
import java.util.HashMap;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class NotificarEntradaEnSalaTask extends Thread{
    ServerTCP server_socket;
    HashMap<Integer, Usuario> Usuarios;
    Usuario nuevo_en_sala;

    public NotificarEntradaEnSalaTask(ServerTCP server_socket, HashMap<Integer, Usuario> Usuarios, Usuario nuevo_en_sala) {
        this.server_socket = server_socket;
        this.Usuarios = Usuarios;
        this.nuevo_en_sala = nuevo_en_sala;
    }
    
    @Override
    public void run() {
        for(Usuario user : Usuarios.values()){
            if(user.getSesionId() == nuevo_en_sala.getSesionId()) continue;
            if(!user.isOnLine()) continue;
            if(!user.isEnSala()) continue;
            
            try {
                this.sleep(200);
            } catch (InterruptedException ex) {
                
            }
            
            HashMap<String, String> info = new HashMap<>();
            info.put("ID", String.valueOf(user.getSesionId()));
            info.put("TYPE", "CONTRINCANTE_EN_SALA");
            info.put("ID_CONTRINCANTE", String.valueOf(nuevo_en_sala.getSesionId()));
            info.put("NICK_CONTRINCANTE", nuevo_en_sala.getNick());
            info.put("ROL", "JUGADOR");
            info.put("LISTO", "NO");
            String mensaje = Parse.convertInfoToMessage(info, "");
            
            this.server_socket.sendMessage(mensaje, user.getSesionId());
        }
    }
    
}
