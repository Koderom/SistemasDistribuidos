package Events;

import java.util.EventListener;

/**
 *
 * @author MIRKO
 */
public interface SocketListener extends EventListener{
    public void onClientConnected(ConnectionEvent event);
    public void onReadMessage(DataEvent event);
    public void onClientDisconnect(DisconnectEvent event);
    public void onRegisteredUser(UserRegistrationEvent event);
    public void onConnectionInterrupted(DisconnectEvent event);
}
