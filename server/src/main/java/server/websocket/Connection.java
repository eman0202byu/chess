package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    public Integer lobbyId;

    public Connection(String visitorName, Session session, Integer lobbyId) {
        this.visitorName = visitorName;
        this.session = session;
        this.lobbyId = lobbyId;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}