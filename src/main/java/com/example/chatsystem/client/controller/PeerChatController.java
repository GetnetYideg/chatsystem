package com.example.chatsystem.client.controller;

import com.example.chatsystem.ChatClient;
import com.example.chatsystem.client.models.userModel;
import com.example.chatsystem.client.service.ChatClientService;
import com.example.chatsystem.client.ui.PeerChatScreen;
import com.example.chatsystem.client.util.Constants;
import com.example.chatsystem.client.websocket.clientWebSOcket;
import com.example.chatsystem.client.websocket.messageListener;
import org.json.JSONObject;

public class PeerChatController implements messageListener {

    private final ChatClient app;
    private final userModel peer;
    private final ChatClientService chatService;
    private PeerChatScreen screen;

    public PeerChatController(ChatClient app, userModel peer) {
        this.app = app;
        this.peer = peer;
        this.chatService = new ChatClientService();
        clientWebSOcket client = clientWebSOcket.getInstance();
        if (client != null) {
            client.setListener(this);
        }
    }

    public void setScreen(PeerChatScreen screen) {
        this.screen = screen;
        userModel me = app.getCurrentUser();
        if (me != null && peer != null) {
            chatService.requestChatHistory(me.getId(), peer.getId());
        }
    }

    public userModel getPeer() {
        return peer;
    }

    public userModel getCurrentUser() {
        return app.getCurrentUser();
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    /** Send a P2P message to the peer. */
    public void sendMessage(String text) {
        userModel me = app.getCurrentUser();
        if (me == null || peer == null) return;
        chatService.sendMessage(me.getId(), me.getUsername(), peer.getId(), text);
        // Optimistically show our own bubble immediately
        if (screen != null) screen.appendSentMessage(text);
    }

    /** Navigate back to the dashboard. */
    public void goBack() {
        javafx.application.Platform.runLater(() -> app.showDashboard());
    }

    // ── WebSocket callbacks ───────────────────────────────────────────────────

    @Override
    public void onMessageReceived(JSONObject json) {
        String type = json.optString("type", "");

        if (Constants.MSG_TYPE_HISTORY_RESPONSE.equals(type)) {
            int respPeerId = json.optInt("peer_id", -1);
            if (peer != null && respPeerId == peer.getId()) {
                org.json.JSONArray messagesArr = json.optJSONArray("messages");
                if (messagesArr != null && screen != null) {
                    userModel me = app.getCurrentUser();
                    for (int i = 0; i < messagesArr.length(); i++) {
                        JSONObject msgObj = messagesArr.getJSONObject(i);
                        int senderId = msgObj.optInt("sender_id", -1);
                        String message = msgObj.optString("message", "");
                        
                        if (me != null && senderId == me.getId()) {
                            screen.appendSentMessage(message);
                        } else {
                            screen.appendReceivedMessage(peer.getUsername(), message);
                        }
                    }
                }
            }
        } else if (Constants.MSG_TYPE_CHAT.equals(type)) {
            int senderId = json.optInt("sender_id", -1);
            String senderUsername = json.optString("sender_username", "Unknown");
            String message = json.optString("message", "");

            // Only display messages that belong to THIS conversation
            userModel me = app.getCurrentUser();
            boolean fromPeer   = (senderId == peer.getId());
            boolean fromMe     = (me != null && senderId == me.getId());

            int receiverId = json.optInt("receiver_id", -1);
            boolean toMe   = (me != null && receiverId == me.getId());
            boolean toPeer = (receiverId == peer.getId());

            if (screen != null) {
                if (fromPeer && toMe) {
                    screen.appendReceivedMessage(senderUsername, message);
                }
                // "fromMe && toPeer" is already shown optimistically on send
            }
        }
    }

    @Override
    public void onConnectionOpened() {
        if (screen != null) screen.showStatus("Connected");
    }

    @Override
    public void onConnectionClosed() {
        if (screen != null) screen.showStatus("Disconnected from server.");
    }

    @Override
    public void onError(Exception ex) {
        if (screen != null) screen.showStatus("Error: " + ex.getMessage());
    }
}
