package com.example.chatsystem.client.service;

import com.example.chatsystem.client.util.Constants;
import com.example.chatsystem.client.util.jsonParser;
import com.example.chatsystem.client.websocket.clientWebSOcket;

public class AuthClientService {
    public void login(String username, String password) {
        sendAuth(Constants.MSG_TYPE_LOGIN, username, password);
    }

    public void register(String username, String password) {
        sendAuth(Constants.MSG_TYPE_REGISTER, username, password);
    }

    private void sendAuth(String type, String username, String password) {
        String json = jsonParser.createAuthMessage(type, username, password);
        clientWebSOcket client = clientWebSOcket.getInstance();
        if (client != null && client.isOpen()) {
            client.send(json);
        } else {
            System.err.println("WebSocket is not connected.");
        }
    }
}
