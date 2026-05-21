package com.example.chatsystem.client.ui;

import com.example.chatsystem.client.controller.LoginController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.example.chatsystem.client.util.ThemeManager;
import javafx.scene.layout.VBox;

public class LoginScreen {
    private LoginController controller;

    public LoginScreen(LoginController controller) {
        this.controller = controller;
    }

    public Scene getScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + ThemeManager.getRootBackground() + "; -fx-padding: 20px;");

        Label title = new Label("Chat App Login");
        title.setStyle("-fx-text-fill: " + ThemeManager.getAccentColor() + "; -fx-font-size: 24px; -fx-font-weight: bold;");

        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setMaxWidth(250);
        userField.setStyle("-fx-background-color: " + ThemeManager.getTextFieldBackground() + "; -fx-text-fill: " + ThemeManager.getPrimaryText() + "; -fx-font-size: 14px; -fx-padding: 10px;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(250);
        passField.setStyle("-fx-background-color: " + ThemeManager.getTextFieldBackground() + "; -fx-text-fill: " + ThemeManager.getPrimaryText() + "; -fx-font-size: 14px; -fx-padding: 10px;");

        Button loginBtn = new Button("Login");
        styleButton(loginBtn);

        Button registerBtn = new Button("Register");
        styleButton(registerBtn);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        loginBtn.setOnAction(e -> {
            controller.handleLogin(userField.getText(), passField.getText(), errorLabel);
        });

        registerBtn.setOnAction(e -> {
            controller.handleRegister(userField.getText(), passField.getText(), errorLabel);
        });

        root.getChildren().addAll(title, userField, passField, loginBtn, registerBtn, errorLabel);

        return new Scene(root, 400, 500);
    }

    private void styleButton(Button btn) {
        btn.setMaxWidth(250);
        btn.setStyle(
            "-fx-background-color: " + ThemeManager.getAccentColor() + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10px;" +
            "-fx-background-radius: 5px;" +
            "-fx-cursor: hand;"
        );
    }
}
