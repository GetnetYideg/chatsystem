package com.example.chatsystem.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/chatsystem_db?createDatabaseIfNotExist=true";
                String user = "root";
                String password = ""; 
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connected successfully!");
            } catch (SQLException e) {
                System.err.println("Failed to connect to MySQL Database.");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
