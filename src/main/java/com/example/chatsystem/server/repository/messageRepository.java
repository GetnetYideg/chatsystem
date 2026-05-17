package com.example.chatsystem.server.repository;

import com.example.chatsystem.server.database.DBConnection;
import com.example.chatsystem.server.model.messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Retrieve all messages exchanged between two users, ordered by sent time.
     */
    public List<messages> getMessagesBetween(int userId1, int userId2) {
        List<messages> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        String query =
            "SELECT id, sender_id, receiver_id, message, timestamp " +
            "FROM Messages " +
            "WHERE (sender_id = ? AND receiver_id = ?) " +
            "   OR (sender_id = ? AND receiver_id = ?) " +
            "ORDER BY timestamp ASC";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId1);
            pstmt.setInt(2, userId2);
            pstmt.setInt(3, userId2);
            pstmt.setInt(4, userId1);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    messages msg = new messages(
                        rs.getInt("id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("message"),
                        rs.getTimestamp("timestamp")
                    );
                    list.add(msg);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
