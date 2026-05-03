package com.example.chatsystem.client.controller;

import com.example.chatsystem.ChatClient;
import com.example.chatsystem.client.models.userModel;
import com.example.chatsystem.client.service.ChatClientService;
import com.example.chatsystem.client.ui.ChatScreen;
import com.example.chatsystem.client.util.Constants;
import com.example.chatsystem.client.websocket.clientWebSOcket;
import com.example.chatsystem.client.websocket.messageListener;
import org.json.JSONObject;

public class ChatController implements messageListener {
    private ChatClient app;
    private ChatClientService chatService;
    private ChatScreen screen;

    public ChatController(ChatClient app) {
        this.app = app;
        this.chatService = new ChatClientService();
        clientWebSOcket client = clientWebSOcket.getInstance();
        if (client != null) {
            client.setListener(this);
        }
    }

    public void setScreen(ChatScreen screen) {
        this.screen = screen;
    }

    public void sendMessage(String text) {
        userModel user = app.getCurrentUser();
        if (user != null) {
            chatService.sendMessage(user.getId(), user.getUsername(), -1, text);
        }
    }

    @Override
    public void onMessageReceived(JSONObject json) {
        String type = json.optString("type", "");
        if (Constants.MSG_TYPE_CHAT.equals(type)) {
            String sender = json.optString("sender_username", "Unknown");
            String msg = json.optString("message", "");
            if (screen != null) {
                screen.appendMessage(sender + ": " + msg);
            }
        }
    }

    @Override
    public void onConnectionOpened() {
        if (screen != null) screen.appendMessage("Connected.");
    }

    @Override
    public void onConnectionClosed() {
        if (screen != null) screen.appendMessage("Disconnected from server.");
    }

    @Override
    public void onError(Exception ex) {
        if (screen != null) screen.appendMessage("Error: " + ex.getMessage());
    }
}
