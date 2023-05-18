/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerGame;

import Models.Usuario;
import ServerSocket.ServerTCP;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Parse;

/**
 *
 * @author MIRKO
 */
public class EnviarListaDeSalaTask extends Thread{
    ServerTCP server_socket;
    HashMap<Integer, Usuario> Usuarios;
    Usuario nuevo_en_sala;

    public EnviarListaDeSalaTask(ServerTCP server_socket, HashMap<Integer, Usuario> Usuarios, Usuario nuevo_en_sala) {
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
            info.put("ID", String.valueOf(nuevo_en_sala.getSesionId()));
            info.put("TYPE", "CONTRINCANTE_EN_SALA");
            info.put("ID_CONTRINCANTE", String.valueOf(user.getSesionId()));
            info.put("NICK_CONTRINCANTE", user.getNick());
            if(user.isListo()) info.put("LISTO", "SI");
            else info.put("LISTO", "NO");
            if(user.rol.equals("master"))info.put("ROL", "MASTER");
            else info.put("ROL", "JUGADOR");
            String mensaje = Parse.convertInfoToMessage(info, "");
            
            this.server_socket.sendMessage(mensaje, nuevo_en_sala.getSesionId());
        }
    }
    
}
