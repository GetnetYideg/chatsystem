package com.example.chatsystem.client.ui;

import com.example.chatsystem.client.controller.ChatController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ChatScreen {
    private ChatController controller;
    private TextArea chatArea;

    public ChatScreen(ChatController controller) {
        this.controller = controller;
        this.controller.setScreen(this);
    }

    public Scene getScene() {
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #121212; -fx-padding: 15px;");

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        VBox.setVgrow(chatArea, Priority.ALWAYS);
        chatArea.setStyle(
            "-fx-control-inner-background: #1e1e1e;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-border-color: #333;" +
            "-fx-border-radius: 5px;"
        );

        TextField inputField = new TextField();
        inputField.setPromptText("Type a message...");
        inputField.setStyle("-fx-background-color: #2d2d2d; -fx-text-fill: white; -fx-padding: 10px;");

        Button sendBtn = new Button("Send");
        sendBtn.setStyle(
            "-fx-background-color: #00b4d8;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10px 20px;" +
            "-fx-cursor: hand;"
        );

        sendBtn.setOnAction(e -> {
            String text = inputField.getText();
            if (!text.isEmpty()) {
                controller.sendMessage(text);
                inputField.clear();
            }
        });

        root.getChildren().addAll(chatArea, inputField, sendBtn);

        return new Scene(root, 500, 700);
    }

    public void appendMessage(String msg) {
        Platform.runLater(() -> chatArea.appendText(msg + "\n"));
    }
}
