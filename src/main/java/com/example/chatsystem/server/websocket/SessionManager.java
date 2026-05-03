package com.example.chatsystem.server.websocket;

import com.example.chatsystem.server.model.users;
import org.java_websocket.WebSocket;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    // Maps WebSocket connection to User
    private ConcurrentHashMap<WebSocket, users> activeSessions;

    public SessionManager() {
        this.activeSessions = new ConcurrentHashMap<>();
    }

    public void addSession(WebSocket conn, users user) {
        activeSessions.put(conn, user);
    }

    public void removeSession(WebSocket conn) {
        activeSessions.remove(conn);
    }

    public users getUser(WebSocket conn) {
        return activeSessions.get(conn);
    }

    public Collection<WebSocket> getAllConnections() {
        return activeSessions.keySet();
    }
    
    public Collection<users> getAllUsers() {
        return activeSessions.values();
    }
}
