package com.example.chatsystem.client.ui;

import com.example.chatsystem.client.controller.PeerChatController;
import com.example.chatsystem.client.models.userModel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.example.chatsystem.client.util.ThemeManager;

public class PeerChatScreen {

    private PeerChatController controller;
    private VBox messagesBox;
    private ScrollPane scrollPane;

    public PeerChatScreen(PeerChatController controller) {
        this.controller = controller;
        this.controller.setScreen(this);
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + ThemeManager.getRootBackground() + ";");

        // TOP BAR
        HBox topBar = new HBox(14);
        topBar.setPadding(new Insets(14, 20, 14, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle(
            "-fx-background-color: " + ThemeManager.getSidebarBackground() + ";" +
            "-fx-border-color: " + ThemeManager.getSidebarBorder() + ";" +
            "-fx-border-width: 0 0 1 0;"
        );

        // Back button
        Button backBtn = new Button("←");
        backBtn.setStyle(
            "-fx-background-color: " + ThemeManager.getCardBackground() + ";" +
            "-fx-text-fill: " + ThemeManager.getSecondaryText() + ";" +
            "-fx-font-size: 18px;" +
            "-fx-padding: 6px 14px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;"
        );
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
            "-fx-background-color: " + ThemeManager.getSecondaryCardBackground() + ";" +
            "-fx-text-fill: " + ThemeManager.getPrimaryText() + ";" +
            "-fx-font-size: 18px;" +
            "-fx-padding: 6px 14px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;"
        ));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(
            "-fx-background-color: " + ThemeManager.getCardBackground() + ";" +
            "-fx-text-fill: " + ThemeManager.getSecondaryText() + ";" +
            "-fx-font-size: 18px;" +
            "-fx-padding: 6px 14px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;"
        ));
        backBtn.setOnAction(e -> controller.goBack());

        // Peer avatar + info
        userModel peer = controller.getPeer();
        String peerName = (peer != null) ? peer.getUsername() : "Unknown";
        String initials = peerName.length() >= 2
            ? peerName.substring(0, 2).toUpperCase()
            : peerName.toUpperCase();

        StackPane avatar = new StackPane();
        Circle circle = new Circle(20);
        String[] palette = {"#7c3aed","#db2777","#d97706","#059669","#2563eb","#dc2626"};
        String colour = palette[Math.abs(peerName.hashCode()) % palette.length];
        circle.setFill(Color.web(colour));
        Label initLbl = new Label(initials);
        initLbl.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
        avatar.getChildren().addAll(circle, initLbl);

        VBox peerInfo = new VBox(2);
        Label peerNameLabel = new Label(peerName);
        peerNameLabel.setStyle("-fx-text-fill: " + ThemeManager.getPrimaryText() + "; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label peerStatus = new Label("● Active now");
        peerStatus.setStyle("-fx-text-fill: #22c55e; -fx-font-size: 11px;");
        peerInfo.getChildren().addAll(peerNameLabel, peerStatus);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Delete Chat button
        Button deleteBtn = new Button("🗑 Delete Chat");
        deleteBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #ef4444;" + // red-500
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 6px 12px;" +
            "-fx-border-color: #ef4444;" +
            "-fx-border-radius: 6px;"
        );
        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #ef4444; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 6px 12px; -fx-border-color: #ef4444; -fx-border-radius: 6px;"));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ef4444; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 6px 12px; -fx-border-color: #ef4444; -fx-border-radius: 6px;"));
        deleteBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete all chat history with " + peerName + "? This cannot be undone.", ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Delete Chat History");
            confirm.setHeaderText("Delete Chat");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    controller.deleteHistory();
                }
            });
        });

        topBar.getChildren().addAll(backBtn, avatar, peerInfo, spacer, deleteBtn);
        root.setTop(topBar);

        // MESSAGES AREA
        messagesBox = new VBox(10);
        messagesBox.setPadding(new Insets(16));
        messagesBox.setStyle("-fx-background-color: " + ThemeManager.getRootBackground() + ";");
        messagesBox.setFillWidth(true);

        scrollPane = new ScrollPane(messagesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
            "-fx-background: " + ThemeManager.getRootBackground() + ";" +
            "-fx-background-color: " + ThemeManager.getRootBackground() + ";" +
            "-fx-border-color: transparent;"
        );
        scrollPane.vvalueProperty().bind(messagesBox.heightProperty());
        root.setCenter(scrollPane);

        //INPUT BAR
        HBox inputBar = new HBox(10);
        inputBar.setPadding(new Insets(14, 20, 14, 20));
        inputBar.setAlignment(Pos.CENTER);
        inputBar.setStyle(
            "-fx-background-color: " + ThemeManager.getSidebarBackground() + ";" +
            "-fx-border-color: " + ThemeManager.getSidebarBorder() + ";" +
            "-fx-border-width: 1 0 0 0;"
        );

        TextField inputField = new TextField();
        inputField.setPromptText("Type a message...");
        inputField.setStyle(
            "-fx-background-color: " + ThemeManager.getTextFieldBackground() + ";" +
            "-fx-text-fill: " + ThemeManager.getPrimaryText() + ";" +
            "-fx-prompt-text-fill: " + ThemeManager.getSecondaryText() + ";" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 12px 16px;" +
            "-fx-background-radius: 24px;" +
            "-fx-border-color: transparent;"
        );
        HBox.setHgrow(inputField, Priority.ALWAYS);

        Button sendBtn = new Button("➤");
        sendBtn.setStyle(
            "-fx-background-color: " + ThemeManager.getAccentColor() + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-min-width: 44px;" +
            "-fx-min-height: 44px;" +
            "-fx-background-radius: 22px;" +
            "-fx-cursor: hand;"
        );
        sendBtn.setOnMouseEntered(e -> sendBtn.setStyle(
            "-fx-background-color: " + ThemeManager.getAccentHover() + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-min-width: 44px;" +
            "-fx-min-height: 44px;" +
            "-fx-background-radius: 22px;" +
            "-fx-cursor: hand;"
        ));
        sendBtn.setOnMouseExited(e -> sendBtn.setStyle(
            "-fx-background-color: " + ThemeManager.getAccentColor() + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-min-width: 44px;" +
            "-fx-min-height: 44px;" +
            "-fx-background-radius: 22px;" +
            "-fx-cursor: hand;"
        ));

        Runnable sendAction = () -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                controller.sendMessage(text);
                inputField.clear();
            }
        };
        sendBtn.setOnAction(e -> sendAction.run());
        inputField.setOnAction(e -> sendAction.run());

        inputBar.getChildren().addAll(inputField, sendBtn);
        root.setBottom(inputBar);

        return new Scene(root, 900, 660);
    }

    /** Append a received message bubble (from the peer). */
    public void appendReceivedMessage(String senderName, String message) {
        Platform.runLater(() -> {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);

            VBox bubble = new VBox(3);
            bubble.setMaxWidth(480);
            bubble.setPadding(new Insets(10, 14, 10, 14));
            bubble.setStyle(
                "-fx-background-color: " + ThemeManager.getCardBackground() + ";" +
                "-fx-background-radius: 0 16 16 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 1);"
            );
            Label sender = new Label(senderName);
            sender.setStyle("-fx-text-fill: " + ThemeManager.getAccentColor() + "; -fx-font-size: 11px; -fx-font-weight: bold;");
            Label msg = new Label(message);
            msg.setStyle("-fx-text-fill: " + ThemeManager.getPrimaryText() + "; -fx-font-size: 14px;");
            msg.setWrapText(true);
            bubble.getChildren().addAll(sender, msg);

            row.getChildren().add(bubble);
            messagesBox.getChildren().add(row);
        });
    }

    /** Append a sent message bubble (from current user). */
    public void appendSentMessage(String message) {
        Platform.runLater(() -> {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_RIGHT);

            VBox bubble = new VBox(3);
            bubble.setMaxWidth(480);
            bubble.setPadding(new Insets(10, 14, 10, 14));
            bubble.setStyle(
                "-fx-background-color: " + ThemeManager.getAccentColor() + ";" +
                "-fx-background-radius: 16 0 16 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,180,216,0.4), 6, 0, 0, 2);"
            );
            Label msg = new Label(message);
            msg.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            msg.setWrapText(true);
            bubble.getChildren().add(msg);

            row.getChildren().add(bubble);
            messagesBox.getChildren().add(row);
        });
    }

    public void showStatus(String text) {
        Platform.runLater(() -> {
            Label lbl = new Label(text);
            lbl.setStyle("-fx-text-fill: " + ThemeManager.getSecondaryText() + "; -fx-font-size: 12px;");
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER);
            row.setPadding(new Insets(4));
            row.getChildren().add(lbl);
            messagesBox.getChildren().add(row);
        });
    }

    public void clearMessages() {
        Platform.runLater(() -> messagesBox.getChildren().clear());
    }
}
