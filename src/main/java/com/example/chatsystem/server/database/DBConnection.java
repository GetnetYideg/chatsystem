package com.example.chatsystem.server.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public static Connection getConnection() {
        if (connection == null) {
            try {
                String url = dotenv.get("DB_URL", "jdbc:mysql://localhost:3306/chatsystem_db?createDatabaseIfNotExist=true");
                String user = dotenv.get("DB_USER", "root");
                String password = dotenv.get("DB_PASSWORD"); 
                
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
