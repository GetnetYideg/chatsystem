package com.example.chatsystem.server.websocket;

import org.java_websocket.WebSocket;
import java.util.Collection;

public class MessageRouter {
    private SessionManager sessionManager;

    public MessageRouter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void broadcast(String message) {
        Collection<WebSocket> connections = sessionManager.getAllConnections();
        for (WebSocket conn : connections) {
            conn.send(message);
        }
    }

    public void routeToUser(int receiverId, String message) {
        WebSocket conn = sessionManager.getConnectionByUserId(receiverId);
        if (conn != null) {
            conn.send(message);
        }
    }
}
