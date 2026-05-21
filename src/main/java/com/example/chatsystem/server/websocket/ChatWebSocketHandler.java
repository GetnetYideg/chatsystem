package com.example.chatsystem.server.websocket;

import com.example.chatsystem.server.repository.userRepository;
import com.example.chatsystem.server.service.AuthService;
import com.example.chatsystem.server.service.ChatService;
import com.example.chatsystem.server.model.messages;
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
            } else if ("GET_HISTORY".equals(type)) {
                handleGetHistory(conn, json);
            } else if ("DELETE_HISTORY".equals(type)) {
                handleDeleteHistory(conn, json);
            } else if ("CHANGE_USERNAME".equals(type) || "CHANGE_PASSWORD".equals(type)) {
                handleUpdateProfile(conn, json, type);
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

    // ── GET_HISTORY ───────────────────────────────────────────────────────────
    private void handleGetHistory(WebSocket conn, JSONObject json) {
        int userId1 = json.optInt("user_id", -1);
        int userId2 = json.optInt("peer_id", -1);
        if (userId1 == -1 || userId2 == -1) return;

        List<messages> history = chatService.getChatHistory(userId1, userId2);
        JSONArray arr = new JSONArray();
        for (messages msg : history) {
            JSONObject obj = new JSONObject();
            obj.put("sender_id", msg.getSenderId());
            obj.put("receiver_id", msg.getReceiverId());
            obj.put("message", msg.getMessage());
            obj.put("timestamp", msg.getTimestamp() != null ? msg.getTimestamp().toString() : "");
            arr.put(obj);
        }
        JSONObject response = new JSONObject();
        response.put("type", "HISTORY_RESPONSE");
        response.put("peer_id", userId2);
        response.put("messages", arr);
        conn.send(response.toString());
    }

    // ── DELETE_HISTORY ────────────────────────────────────────────────────────
    private void handleDeleteHistory(WebSocket conn, JSONObject json) {
        int userId1 = json.optInt("user_id", -1);
        int userId2 = json.optInt("peer_id", -1);
        if (userId1 == -1 || userId2 == -1) return;

        boolean success = chatService.deleteChatHistory(userId1, userId2);
        JSONObject response = new JSONObject();
        response.put("type", "DELETE_HISTORY_RESPONSE");
        response.put("success", success);
        conn.send(response.toString());
    }

    // ── UPDATE_PROFILE ────────────────────────────────────────────────────────
    private void handleUpdateProfile(WebSocket conn, JSONObject json, String type) {
        users user = sessionManager.getUser(conn);
        JSONObject response = new JSONObject();
        response.put("type", "UPDATE_PROFILE_RESPONSE");
        response.put("action", type);

        if (user == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            conn.send(response.toString());
            return;
        }

        boolean success = false;
        String message = "";

        if ("CHANGE_USERNAME".equals(type)) {
            String newUsername = json.optString("new_username", "").trim();
            if (newUsername.isEmpty()) {
                message = "Username cannot be empty";
            } else if (userRepo.findByUsername(newUsername) != null) {
                message = "Username is already taken";
            } else {
                success = userRepo.updateUsername(user.getId(), newUsername);
                if (success) {
                    user.setUsername(newUsername); // Update in session manager memory
                    message = "Username updated successfully";
                    response.put("new_username", newUsername);
                } else {
                    message = "Failed to update username in database";
                }
            }
        } else if ("CHANGE_PASSWORD".equals(type)) {
            String newPassword = json.optString("new_password", "");
            if (newPassword.isEmpty()) {
                message = "Password cannot be empty";
            } else {
                success = userRepo.updatePassword(user.getId(), newPassword);
                if (success) {
                    message = "Password updated successfully";
                } else {
                    message = "Failed to update password in database";
                }
            }
        }

        response.put("success", success);
        response.put("message", message);
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
 