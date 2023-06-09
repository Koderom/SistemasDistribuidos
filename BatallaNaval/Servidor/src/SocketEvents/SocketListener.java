package SocketEvents;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface SocketListener extends EventListener{
    public void onClientConnected(ConnectionEvent event);
    public void onReceiveData(ReceiveDataEvent event);
    public void onSessionDisconnect(DisconnectEvent event);
    public void onRegisteredUser(UserRegistrationEvent event);
    //public void onConnectionInterrupted(DisconnectEvent event);
}
