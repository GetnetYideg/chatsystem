package com.example.chatsystem.server.websocket;

import com.example.chatsystem.server.repository.userRepository;
import com.example.chatsystem.server.service.AuthService;
import com.example.chatsystem.server.service.ChatService;
import com.example.chatsystem.server.model.users;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.List;

public class ChatWebSocketHandler extends WebSocketServer {
    private SessionManager sessionManager;
    private MessageRouter messageRouter;
    private AuthService authService;
    private ChatService chatService;
    private userRepository userRepo;

    public ChatWebSocketHandler(int port) {
        super(new InetSocketAddress(port));
        this.sessionManager = new SessionManager();
        this.messageRouter = new MessageRouter(sessionManager);
        this.authService = new AuthService();
        this.chatService = new ChatService();
        this.userRepo = new userRepository();
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
            } else if ("GET_CONTACTS".equals(type)) {
                handleGetContacts(conn, json);
            } else if ("FIND_USER".equals(type)) {
                handleFindUser(conn, json);
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

    // ── GET_CONTACTS ──────────────────────────────────────────────────────────
    private void handleGetContacts(WebSocket conn, JSONObject json) {
        int userId = json.optInt("user_id", -1);
        if (userId == -1) return;

        List<users> contacts = userRepo.getChattedUsers(userId);
        JSONArray arr = new JSONArray();
        for (users u : contacts) {
            JSONObject obj = new JSONObject();
            obj.put("id", u.getId());
            obj.put("username", u.getUsername());
            arr.put(obj);
        }
        JSONObject response = new JSONObject();
        response.put("type", "CONTACTS_RESPONSE");
        response.put("contacts", arr);
        conn.send(response.toString());
    }

    // ── FIND_USER ─────────────────────────────────────────────────────────────
    private void handleFindUser(WebSocket conn, JSONObject json) {
        String username = json.optString("username", "");
        JSONObject response = new JSONObject();
        response.put("type", "FIND_USER_RESPONSE");
        response.put("username", username);

        if (username.isEmpty()) {
            response.put("found", false);
        } else {
            users found = userRepo.findByUsername(username);
            if (found != null) {
                response.put("found", true);
                response.put("user_id", found.getId());
                response.put("username", found.getUsername());
            } else {
                response.put("found", false);
            }
        }
        conn.send(response.toString());
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
