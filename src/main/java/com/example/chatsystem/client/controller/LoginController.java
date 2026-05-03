package com.example.chatsystem.client.controller;

import com.example.chatsystem.client.service.AuthClientService;
import com.example.chatsystem.client.util.Constants;
import com.example.chatsystem.client.websocket.clientWebSOcket;
import com.example.chatsystem.client.websocket.messageListener;
import com.example.chatsystem.ChatClient;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.json.JSONObject;

public class LoginController implements messageListener {
    private AuthClientService authService;
    private ChatClient app;
    private Label errorLabel;

    public LoginController(ChatClient app) {
        this.app = app;
        this.authService = new AuthClientService();
        clientWebSOcket.initialize(this);
    }

    public void handleLogin(String username, String password, Label errorLabel) {
        this.errorLabel = errorLabel;
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }
        authService.login(username, password);
    }

    public void handleRegister(String username, String password, Label errorLabel) {
        this.errorLabel = errorLabel;
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }
        authService.register(username, password);
    }

    @Override
    public void onMessageReceived(JSONObject json) {
        String type = json.optString("type", "");
        if (Constants.MSG_TYPE_AUTH_SUCCESS.equals(type)) {
            int userId = json.optInt("user_id");
            String username = json.optString("username");
            Platform.runLater(() -> {
                app.setCurrentUser(userId, username);
                app.showChatScreen();
            });
        } else if (Constants.MSG_TYPE_AUTH_FAIL.equals(type)) {
            Platform.runLater(() -> {
                if (errorLabel != null) errorLabel.setText(json.optString("message", "Authentication failed."));
            });
        }
    }

    @Override
    public void onConnectionOpened() {
        System.out.println("Connected to server.");
    }

    @Override
    public void onConnectionClosed() {
        Platform.runLater(() -> {
            if (errorLabel != null) errorLabel.setText("Disconnected from server.");
        });
    }

    @Override
    public void onError(Exception ex) {
        Platform.runLater(() -> {
            if (errorLabel != null) errorLabel.setText("Error: " + ex.getMessage());
        });
    }
}
