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
}
