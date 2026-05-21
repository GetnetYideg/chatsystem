package com.example.chatsystem.server.repository;

import com.example.chatsystem.server.database.DBConnection;
import com.example.chatsystem.server.model.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userRepository {
    
    public users registerUser(String username, String password) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return null;
        
        String query = "INSERT INTO Users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new users(rs.getInt(1), username, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    public users authenticateUser(String username, String password) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return null;
        
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new users(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public users findByUsername(String username) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return null;
        
        String query = "SELECT * FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new users(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.util.List<users> getChattedUsers(int userId) {
        java.util.List<users> list = new java.util.ArrayList<>();
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;

        String query = "SELECT DISTINCT u.id, u.username, u.password FROM Users u " +
                       "JOIN Messages m ON (u.id = m.sender_id OR u.id = m.receiver_id) " +
                       "WHERE (m.sender_id = ? OR m.receiver_id = ?) AND u.id != ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new users(rs.getInt("id"), rs.getString("username"), rs.getString("password")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean updateUsername(int userId, String newUsername) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        
        String query = "UPDATE Users SET username = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newUsername);
            pstmt.setInt(2, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPassword) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        
        String query = "UPDATE Users SET password = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
