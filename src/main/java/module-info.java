
module com.example.chatsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.java_websocket;
    requires org.json;
    requires io.github.cdimascio.dotenv.java;

    opens com.example.chatsystem to javafx.fxml;
    opens com.example.chatsystem.client.controller to javafx.fxml;
    opens com.example.chatsystem.client.ui to javafx.fxml;

    exports com.example.chatsystem;
    exports com.example.chatsystem.client.controller;
    exports com.example.chatsystem.client.ui;
    exports com.example.chatsystem.client.models;
    exports com.example.chatsystem.client.service;
    exports com.example.chatsystem.client.websocket;
}