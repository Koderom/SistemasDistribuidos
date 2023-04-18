/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Events;

import Models.Cliente;
import java.util.EventObject;

/**
 *
 * @author MIRKO
 */
public class UserRegistrationEvent extends EventObject{
    Cliente cliente;

    public UserRegistrationEvent(Object source, Cliente cliente) {
        super(source);
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
