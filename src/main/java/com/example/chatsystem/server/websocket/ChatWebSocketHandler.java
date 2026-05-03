package com.example.chatsystem.server.websocket;

import com.example.chatsystem.server.service.AuthService;
import com.example.chatsystem.server.service.ChatService;
import com.example.chatsystem.server.model.users;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONObject;

import java.net.InetSocketAddress;

public class ChatWebSocketHandler extends WebSocketServer {
    private SessionManager sessionManager;
    private MessageRouter messageRouter;
    private AuthService authService;
    private ChatService chatService;

    public ChatWebSocketHandler(int port) {
        super(new InetSocketAddress(port));
        this.sessionManager = new SessionManager();
        this.messageRouter = new MessageRouter(sessionManager);
        this.authService = new AuthService();
        this.chatService = new ChatService();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New WebSocket connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        users user = sessionManager.getUser(conn);
        if (user != null) {
            System.out.println("User disconnected: " + user.getUsername());
        }
        sessionManager.removeSession(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received: " + message);
        try {
            JSONObject json = new JSONObject(message);
            String type = json.optString("type", "");

            if ("LOGIN".equals(type) || "REGISTER".equals(type)) {
                handleAuth(conn, json, type);
            } else if ("CHAT".equals(type)) {
                handleChat(conn, json);
            }
        } catch (Exception e) {
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }

    private void handleAuth(WebSocket conn, JSONObject json, String type) {
        String username = json.optString("username");
        String password = json.optString("password");
        users user = null;

        if ("LOGIN".equals(type)) {
            user = authService.login(username, password);
        } else if ("REGISTER".equals(type)) {
            user = authService.register(username, password);
        }

        JSONObject response = new JSONObject();
        if (user != null) {
            sessionManager.addSession(conn, user);
            response.put("type", "AUTH_SUCCESS");
            response.put("user_id", user.getId());
            response.put("username", user.getUsername());
        } else {
            response.put("type", "AUTH_FAIL");
            response.put("message", "Invalid credentials or username taken.");
        }
        conn.send(response.toString());
    }

    private void handleChat(WebSocket conn, JSONObject json) {
        users sender = sessionManager.getUser(conn);
        if (sender == null) {
            conn.send(new JSONObject().put("type", "ERROR").put("message", "Not authenticated").toString());
            return;
        }

        int receiverId = json.optInt("receiver_id", -1);
        String content = json.optString("message", "");

        if (!content.isEmpty()) {
            boolean saved = chatService.saveMessage(sender.getId(), receiverId, content);
            if (saved) {
                // Broadcast to all (for now)
                json.put("sender_username", sender.getUsername());
                messageRouter.broadcast(json.toString());
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket Server started on port " + getPort());
    }
}
