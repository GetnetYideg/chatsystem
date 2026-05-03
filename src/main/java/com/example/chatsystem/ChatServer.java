package com.example.chatsystem;

import com.example.chatsystem.server.database.Databasemanager;
import com.example.chatsystem.server.websocket.ChatWebSocketHandler;

public class ChatServer {

    public static void main(String[] args) {
        System.out.println("Starting Chat System Server...");
        
        // Initialize Database
        Databasemanager.initializeDatabase();

        // Start WebSocket Server
        int port = 1235;
        ChatWebSocketHandler server = new ChatWebSocketHandler(port);
        server.start();
        
        System.out.println("ChatServer is running. Waiting for connections...");
    }
}