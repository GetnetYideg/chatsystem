package com.example.chatsystem.server.repository;

import com.example.chatsystem.server.database.DBConnection;
import com.example.chatsystem.server.model.messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class messageRepository {

    public boolean saveMessage(messages msg) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        
        String query = "INSERT INTO Messages (sender_id, receiver_id, message) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, msg.getSenderId());
            if (msg.getReceiverId() == -1) {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(2, msg.getReceiverId());
            }
            pstmt.setString(3, msg.getMessage());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
