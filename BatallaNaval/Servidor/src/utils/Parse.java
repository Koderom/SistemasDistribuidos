/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MIRKO
 */
public class Parse {
    public static  Map<String, String> convertMessageToInfo(String mensaje){
        Map<String, String> contenido = new HashMap<>();
        try{
            int info_ini = mensaje.indexOf("<");
            int info_fin = mensaje.indexOf(">");
            if(info_ini != -1 && info_fin != -1){
                String info = mensaje.substring(info_ini+1, info_fin);
                String datos[] = info.split("/");
                if(datos.length == 0) throw new Exception("formato del mensaje no valido");
                for(String dato: datos){
                    String campo_valor[] = dato.split(",");
                    if(campo_valor.length == 0) continue;
                    if(campo_valor.length == 2 && campo_valor[0].length() == 0) throw new Exception("formato del mensaje no valido");
                    if(campo_valor.length > 2) throw new Exception("formato del mensaje no valido");
                    if(campo_valor.length == 1)contenido.put(campo_valor[0], "");
                    else contenido.put(campo_valor[0], campo_valor[1]);
                }
            }
            String MSJ = mensaje.substring(info_fin+1, mensaje.length());
            contenido.put("MSJ",MSJ);
        }catch(Exception ex){
            contenido.put("ERROR", ex.toString());
        }finally{
            return contenido;
        }
    }
    public static  String convertInfoToMessage(Map<String, String> info, String msj){
        String mensaje = "<";
        for(String key : info.keySet()){
            mensaje = mensaje + key + "," + info.get(key) + "/";
        }
        mensaje = mensaje.substring(0, mensaje.length() - 1);
        mensaje = mensaje + ">";
        return mensaje + msj;
    }
}
