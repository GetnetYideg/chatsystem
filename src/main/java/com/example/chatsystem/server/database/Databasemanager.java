package com.example.chatsystem.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Databasemanager {
    
    public static void initializeDatabase() {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;

        String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(255) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL" +
                ")";

        String createMessagesTable = "CREATE TABLE IF NOT EXISTS Messages (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "sender_id INT," +
                "receiver_id INT," +
                "message TEXT," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try {
            try (PreparedStatement stmt = conn.prepareStatement(createUsersTable)) {
                stmt.execute();
            }
            try (PreparedStatement stmt = conn.prepareStatement(createMessagesTable)) {
                stmt.execute();
            }
            System.out.println("Database tables verified/created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
