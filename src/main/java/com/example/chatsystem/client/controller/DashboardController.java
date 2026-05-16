package com.example.chatsystem.client.controller;

import com.example.chatsystem.ChatClient;
import com.example.chatsystem.client.models.userModel;
import com.example.chatsystem.client.service.DashboardClientService;
import com.example.chatsystem.client.ui.DashboardScreen;
import com.example.chatsystem.client.util.Constants;
import com.example.chatsystem.client.websocket.clientWebSOcket;
import com.example.chatsystem.client.websocket.messageListener;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController implements messageListener {

    private final ChatClient app;
    private final DashboardClientService dashboardService;
    private DashboardScreen screen;

    /** Full cached list of contacts (for search filtering). */
    private List<userModel> allContacts = new ArrayList<>();

    public DashboardController(ChatClient app) {
        this.app = app;
        this.dashboardService = new DashboardClientService();
        clientWebSOcket client = clientWebSOcket.getInstance();
        if (client != null) {
            client.setListener(this);
        }
    }

    public void setScreen(DashboardScreen screen) {
        this.screen = screen;
    }

    public userModel getCurrentUser() {
        return app.getCurrentUser();
    }

    // ── Contact loading ───────────────────────────────────────────────────────

    /** Sends a GET_CONTACTS request to the server. */
    public void loadContacts() {
        userModel user = app.getCurrentUser();
        if (user == null) return;
        dashboardService.requestContacts(user.getId());
    }

    /** Live-filter the cached contacts by prefix (case-insensitive). */
    public void filterContacts(String query) {
        if (screen == null) return;
        if (query == null || query.isEmpty()) {
            screen.populateContacts(allContacts);
            return;
        }
        String lower = query.toLowerCase();
        List<userModel> filtered = allContacts.stream()
            .filter(u -> u.getUsername().toLowerCase().contains(lower))
            .collect(Collectors.toList());
        screen.populateContacts(filtered);
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    /** Open a P2P chat with the selected contact. */
    public void openChat(userModel peer) {
        Platform.runLater(() -> app.showPeerChatScreen(peer));
    }

    /**
     * Look up a user by username from the server, then open the chat.
     * Used by the "Start New Chat" dialog.
     */
    public void findUserAndOpenChat(String username) {
        dashboardService.findUserByUsername(username);
    }

    // ── WebSocket callbacks ───────────────────────────────────────────────────

    @Override
    public void onMessageReceived(JSONObject json) {
        String type = json.optString("type", "");

        if (Constants.MSG_TYPE_CONTACTS_RESPONSE.equals(type)) {
            JSONArray arr = json.optJSONArray("contacts");
            List<userModel> contacts = new ArrayList<>();
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    contacts.add(new userModel(obj.getInt("id"), obj.getString("username")));
                }
            }
            allContacts = contacts;
            if (screen != null) screen.populateContacts(contacts);

        } else if (Constants.MSG_TYPE_FIND_USER_RESPONSE.equals(type)) {
            boolean found = json.optBoolean("found", false);
            if (found) {
                int peerId = json.optInt("user_id");
                String peerName = json.optString("username");
                Platform.runLater(() -> app.showPeerChatScreen(new userModel(peerId, peerName)));
            } else {
                if (screen != null)
                    screen.showError("User \"" + json.optString("username") + "\" not found.");
            }
        }
    }

    @Override
    public void onConnectionOpened() {}

    @Override
    public void onConnectionClosed() {
        if (screen != null) screen.showError("Disconnected from server.");
    }

    @Override
    public void onError(Exception ex) {
        if (screen != null) screen.showError("Error: " + ex.getMessage());
    }
}
