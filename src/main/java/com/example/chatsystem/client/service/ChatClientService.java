package com.example.chatsystem.client.service;

import com.example.chatsystem.client.util.jsonParser;
import com.example.chatsystem.client.websocket.clientWebSOcket;

public class ChatClientService {
    public void sendMessage(int senderId, String senderUsername, int receiverId, String message) {
        String json = jsonParser.createChatMessage(senderId, senderUsername, receiverId, message);
        clientWebSOcket client = clientWebSOcket.getInstance();
        if (client != null && client.isOpen()) {
            client.send(json);
        }
    }
}
