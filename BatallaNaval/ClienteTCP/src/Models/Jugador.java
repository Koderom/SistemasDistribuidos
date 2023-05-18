/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author MIRKO
 */
public class Jugador{
    public int id_session;
    public String nick;
    public String rol;
    public boolean estaListo;
    public boolean eliminado;

    public Jugador(int id_session, String nick, String rol) {
        this.id_session = id_session;
        this.nick = nick;
        this.rol = rol;
        this.estaListo = false;
        this.eliminado = false;
    }
    public Jugador(int id_session, String nick, String rol, boolean listo){
        this(id_session, nick, rol);
        this.estaListo = listo;
    }
    @Override
    public String toString(){
        String tostring = nick;
        if(rol.equals("MASTER")) tostring += "(master)";
        if(!estaListo) tostring += "___listo : NO";
        else tostring += "___listo : SI";
        return tostring;
    }
}
