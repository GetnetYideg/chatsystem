package com.example.chatsystem.client.ui;

import com.example.chatsystem.client.controller.DashboardController;
import com.example.chatsystem.client.models.userModel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.example.chatsystem.client.util.ThemeManager;

import java.util.List;

public class DashboardScreen {

    private DashboardController controller;
    private VBox contactList;
    private Label statusLabel;

    public DashboardScreen(DashboardController controller) {
        this.controller = controller;
        this.controller.setScreen(this);
    }

    public Scene getScene() {
        // ── Root: horizontal split ──────────────────────────────────────────
        HBox root = new HBox();
        root.setStyle("-fx-background-color: " + ThemeManager.getRootBackground() + ";");

        // ── LEFT SIDEBAR ────────────────────────────────────────────────────
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(300);
        sidebar.setMinWidth(300);
        sidebar.setStyle("-fx-background-color: " + ThemeManager.getSidebarBackground() + ";");

        // Sidebar header
        HBox sidebarHeader = new HBox(10);
        sidebarHeader.setPadding(new Insets(20, 16, 16, 16));
        sidebarHeader.setAlignment(Pos.CENTER_LEFT);
        sidebarHeader.setStyle("-fx-background-color: " + ThemeManager.getSidebarBackground() + "; -fx-border-color: " + ThemeManager.getSidebarBorder() + "; -fx-border-width: 0 0 1 0;");

        Label appTitle = new Label("💬 ChatSystem");
        appTitle.setStyle(
            "-fx-text-fill: " + ThemeManager.getAccentColor() + ";" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;"
        );
        HBox.setHgrow(appTitle, Priority.ALWAYS); // Push settings button to the right

        Button settingsBtn = new Button("⚙️");
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + ThemeManager.getPrimaryText() + "; -fx-cursor: hand; -fx-font-size: 16px;");
        settingsBtn.setOnAction(e -> showSettingsDialog());

        sidebarHeader.getChildren().addAll(appTitle, settingsBtn);

        // User profile card
        VBox profileCard = buildProfileCard();

        // Search bar
        HBox searchBox = new HBox(8);
        searchBox.setPadding(new Insets(12, 16, 12, 16));
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setStyle("-fx-background-color: " + ThemeManager.getSidebarBackground() + ";");

        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Search contacts...");
        searchField.setStyle(
            "-fx-background-color: " + ThemeManager.getTextFieldBackground() + ";" +
            "-fx-text-fill: " + ThemeManager.getPrimaryText() + ";" +
            "-fx-prompt-text-fill: " + ThemeManager.getSecondaryText() + ";" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 8px 12px;" +
            "-fx-background-radius: 20px;" +
            "-fx-border-color: transparent;"
        );
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchBox.getChildren().add(searchField);

        // Contacts section label
        HBox contactsHeader = new HBox();
        contactsHeader.setPadding(new Insets(8, 16, 6, 16));
        Label contactsLabel = new Label("RECENT CHATS");
        contactsLabel.setStyle(
            "-fx-text-fill: " + ThemeManager.getSecondaryText() + ";" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: bold;"
        );
        contactsHeader.getChildren().add(contactsLabel);

        // Scrollable contact list
        contactList = new VBox(0);
        contactList.setStyle("-fx-background-color: " + ThemeManager.getSidebarBackground() + ";");

        statusLabel = new Label("Loading contacts...");
        statusLabel.setStyle("-fx-text-fill: " + ThemeManager.getSecondaryText() + "; -fx-font-size: 13px; -fx-padding: 20px;");
        statusLabel.setWrapText(true);
        contactList.getChildren().add(statusLabel);

        ScrollPane contactScroll = new ScrollPane(contactList);
        contactScroll.setFitToWidth(true);
        contactScroll.setStyle(
            "-fx-background: " + ThemeManager.getSidebarBackground() + ";" +
            "-fx-background-color: " + ThemeManager.getSidebarBackground() + ";" +
            "-fx-border-color: transparent;"
        );
        VBox.setVgrow(contactScroll, Priority.ALWAYS);

        // Search filter
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
            controller.filterContacts(newVal.trim())
        );

        sidebar.getChildren().addAll(sidebarHeader, profileCard, searchBox, contactsHeader, contactScroll);

        // RIGHT CONTENT AREA
        VBox contentArea = new VBox();
        HBox.setHgrow(contentArea, Priority.ALWAYS);
        contentArea.setStyle("-fx-background-color: " + ThemeManager.getRootBackground() + ";");
        contentArea.setAlignment(Pos.CENTER);

        VBox welcomeCard = new VBox(16);
        welcomeCard.setAlignment(Pos.CENTER);
        welcomeCard.setPadding(new Insets(40));
        welcomeCard.setMaxWidth(420);

        Label welcomeIcon = new Label("💬");
        welcomeIcon.setStyle("-fx-font-size: 64px;");

        Label welcomeTitle = new Label("Select a conversation");
        welcomeTitle.setStyle(
            "-fx-text-fill: " + ThemeManager.getPrimaryText() + ";" +
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;"
        );

        Label welcomeSub = new Label("Pick a contact from the sidebar to start chatting, or search for someone new.");
        welcomeSub.setStyle(
            "-fx-text-fill: " + ThemeManager.getSecondaryText() + ";" +
            "-fx-font-size: 14px;"
        );
        welcomeSub.setWrapText(true);
        welcomeSub.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // New chat button
        Button newChatBtn = new Button("＋  Start New Chat");
        newChatBtn.setStyle(
            "-fx-background-color: " + ThemeManager.getAccentColor() + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12px 28px;" +
            "-fx-background-radius: 25px;" +
            "-fx-cursor: hand;"
        );
        newChatBtn.setOnMouseEntered(e -> newChatBtn.setStyle(
            "-fx-background-color: " + ThemeManager.getAccentHover() + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12px 28px;" +
            "-fx-background-radius: 25px;" +
            "-fx-cursor: hand;"
        ));
        newChatBtn.setOnMouseExited(e -> newChatBtn.setStyle(
            "-fx-background-color: " + ThemeManager.getAccentColor() + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12px 28px;" +
            "-fx-background-radius: 25px;" +
            "-fx-cursor: hand;"
        ));
        newChatBtn.setOnAction(e -> showNewChatDialog());

        welcomeCard.getChildren().addAll(welcomeIcon, welcomeTitle, welcomeSub, newChatBtn);
        contentArea.getChildren().add(welcomeCard);

        root.getChildren().addAll(sidebar, contentArea);

        // Load contacts from server
        controller.loadContacts();

        return new Scene(root, 900, 660);
    }

    // Profile Card 
    private VBox buildProfileCard() {
        VBox card = new VBox(8);
        card.setPadding(new Insets(16));
        card.setStyle(
            "-fx-background-color: " + ThemeManager.getCardBackground() + ";" +
            "-fx-background-radius: 12px;" +
            "-fx-margin: 12px;"
        );
        card.setMargin(card, new Insets(12, 12, 0, 12));

        userModel user = controller.getCurrentUser();
        String username = (user != null) ? user.getUsername() : "Unknown";
        int userId = (user != null) ? user.getId() : 0;

        // Avatar circle with initials
        String initials = username.length() >= 2
            ? username.substring(0, 2).toUpperCase()
            : username.toUpperCase();

        StackPane avatar = new StackPane();
        Circle avatarCircle = new Circle(26);
        avatarCircle.setFill(Color.web("#00b4d8"));
        Label avatarLabel = new Label(initials);
        avatarLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        avatar.getChildren().addAll(avatarCircle, avatarLabel);

        // Online indicator
        StackPane avatarWithBadge = new StackPane();
        Circle onlineDot = new Circle(6, Color.web("#22c55e"));
        onlineDot.setTranslateX(18);
        onlineDot.setTranslateY(18);
        avatarWithBadge.getChildren().addAll(avatar, onlineDot);

        VBox userInfo = new VBox(3);
        Label nameLabel = new Label(username);
        nameLabel.setStyle(
            "-fx-text-fill: " + ThemeManager.getPrimaryText() + ";" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;"
        );
        Label idLabel = new Label("ID: " + userId);
        idLabel.setStyle("-fx-text-fill: " + ThemeManager.getSecondaryText() + "; -fx-font-size: 11px;");
        Label onlineLabel = new Label("● Online");
        onlineLabel.setStyle("-fx-text-fill: #22c55e; -fx-font-size: 11px;");
        userInfo.getChildren().addAll(nameLabel, idLabel, onlineLabel);

        HBox profileRow = new HBox(12);
        profileRow.setAlignment(Pos.CENTER_LEFT);
        profileRow.getChildren().addAll(avatarWithBadge, userInfo);

        card.getChildren().add(profileRow);
        VBox.setMargin(card, new Insets(12, 12, 0, 12));
        return card;
    }

    //Contact Row 
    public void populateContacts(List<userModel> contacts) {
        Platform.runLater(() -> {
            contactList.getChildren().clear();
            if (contacts.isEmpty()) {
                Label empty = new Label("No previous chats yet.\nStart a new conversation!");
                empty.setStyle("-fx-text-fill: " + ThemeManager.getSecondaryText() + "; -fx-font-size: 13px; -fx-padding: 20px;");
                empty.setWrapText(true);
                contactList.getChildren().add(empty);
                return;
            }
            for (userModel contact : contacts) {
                contactList.getChildren().add(buildContactRow(contact));
            }
        });
    }

    private HBox buildContactRow(userModel contact) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        String initials = contact.getUsername().length() >= 2
            ? contact.getUsername().substring(0, 2).toUpperCase()
            : contact.getUsername().toUpperCase();

        // Avatar
        StackPane avatar = new StackPane();
        Circle circle = new Circle(20);
        // Deterministic colour from username hash
        String[] palette = {"#7c3aed","#db2777","#d97706","#059669","#2563eb","#dc2626"};
        String colour = palette[Math.abs(contact.getUsername().hashCode()) % palette.length];
        circle.setFill(Color.web(colour));
        Label initLbl = new Label(initials);
        initLbl.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
        avatar.getChildren().addAll(circle, initLbl);

        VBox info = new VBox(3);
        Label nameLabel = new Label(contact.getUsername());
        nameLabel.setStyle("-fx-text-fill: " + ThemeManager.getPrimaryText() + "; -fx-font-size: 14px; -fx-font-weight: bold;");
        Label subLabel = new Label("Tap to open chat");
        subLabel.setStyle("-fx-text-fill: " + ThemeManager.getSubLabelText() + "; -fx-font-size: 12px;");
        info.getChildren().addAll(nameLabel, subLabel);

        HBox.setHgrow(info, Priority.ALWAYS);
        row.getChildren().addAll(avatar, info);

        // Hover effects
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: " + ThemeManager.getSecondaryCardBackground() + "; -fx-cursor: hand;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-cursor: hand;"));
        row.setOnMouseClicked(e -> controller.openChat(contact));

        return row;
    }

    // New-chat dialog
    private void showNewChatDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Start New Chat");
        dialog.setHeaderText(null);

        DialogPane pane = dialog.getDialogPane();
        pane.setStyle("-fx-background-color: " + ThemeManager.getDialogBackground() + ";");

        Label lbl = new Label("Enter Username to chat with:");
        lbl.setStyle("-fx-text-fill: " + ThemeManager.getPrimaryText() + "; -fx-font-size: 13px;");

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Username...");
        usernameInput.setStyle(
            "-fx-background-color: " + ThemeManager.getTextFieldBackground() + ";" +
            "-fx-text-fill: " + ThemeManager.getPrimaryText() + ";" +
            "-fx-prompt-text-fill: " + ThemeManager.getSecondaryText() + ";" +
            "-fx-padding: 10px;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 6px;"
        );

        VBox content = new VBox(10, lbl, usernameInput);
        content.setPadding(new Insets(16));
        pane.setContent(content);

        ButtonType startType = new ButtonType("Start Chat", ButtonBar.ButtonData.OK_DONE);
        pane.getButtonTypes().addAll(startType, ButtonType.CANCEL);

        // Style the buttons
        Button startBtn = (Button) pane.lookupButton(startType);
        startBtn.setStyle(
            "-fx-background-color: #00b4d8; -fx-text-fill: white;" +
            "-fx-font-weight: bold; -fx-background-radius: 6px;"
        );

        dialog.setResultConverter(btn -> {
            if (btn == startType) return usernameInput.getText().trim();
            return null;
        });

        dialog.showAndWait().ifPresent(username -> {
            if (!username.isEmpty()) {
                controller.findUserAndOpenChat(username);
            }
        });
    }

    private void showSettingsDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Settings");
        dialog.setHeaderText(null);

        DialogPane pane = dialog.getDialogPane();
        pane.setStyle("-fx-background-color: " + ThemeManager.getDialogBackground() + ";");

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Profile Section
        Label profileLabel = new Label("Profile");
        profileLabel.setStyle("-fx-text-fill: " + ThemeManager.getPrimaryText() + "; -fx-font-size: 16px; -fx-font-weight: bold;");

        // Change Username
        HBox usernameBox = new HBox(10);
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        TextField newUsernameInput = new TextField();
        newUsernameInput.setPromptText("New Username");
        newUsernameInput.setStyle("-fx-background-color: " + ThemeManager.getTextFieldBackground() + "; -fx-text-fill: " + ThemeManager.getPrimaryText() + ";");
        Button updateUsernameBtn = new Button("Update");
        updateUsernameBtn.setStyle("-fx-background-color: " + ThemeManager.getAccentColor() + "; -fx-text-fill: white;");
        updateUsernameBtn.setOnAction(e -> {
            if (!newUsernameInput.getText().trim().isEmpty()) {
                controller.changeUsername(newUsernameInput.getText().trim());
                newUsernameInput.clear();
            }
        });
        usernameBox.getChildren().addAll(newUsernameInput, updateUsernameBtn);

        // Change Password
        HBox passwordBox = new HBox(10);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        PasswordField newPasswordInput = new PasswordField();
        newPasswordInput.setPromptText("New Password");
        newPasswordInput.setStyle("-fx-background-color: " + ThemeManager.getTextFieldBackground() + "; -fx-text-fill: " + ThemeManager.getPrimaryText() + ";");
        Button updatePasswordBtn = new Button("Update");
        updatePasswordBtn.setStyle("-fx-background-color: " + ThemeManager.getAccentColor() + "; -fx-text-fill: white;");
        updatePasswordBtn.setOnAction(e -> {
            if (!newPasswordInput.getText().isEmpty()) {
                controller.changePassword(newPasswordInput.getText());
                newPasswordInput.clear();
            }
        });
        passwordBox.getChildren().addAll(newPasswordInput, updatePasswordBtn);

        VBox profileBox = new VBox(10, profileLabel, new Label("Change Username"), usernameBox, new Label("Change Password"), passwordBox);
        profileBox.getChildren().get(1).setStyle("-fx-text-fill: " + ThemeManager.getSecondaryText() + ";");
        profileBox.getChildren().get(3).setStyle("-fx-text-fill: " + ThemeManager.getSecondaryText() + ";");

        content.getChildren().addAll(profileBox);
        pane.setContent(content);

        pane.getButtonTypes().add(ButtonType.CLOSE);
        Button closeBtn = (Button) pane.lookupButton(ButtonType.CLOSE);
        closeBtn.setStyle("-fx-background-color: " + ThemeManager.getSecondaryCardBackground() + "; -fx-text-fill: " + ThemeManager.getPrimaryText() + ";");

        dialog.showAndWait();
    }

    public void showError(String message) {
        Platform.runLater(() -> {
            contactList.getChildren().clear();
            statusLabel.setText(message);
            contactList.getChildren().add(statusLabel);
        });
    }

    public void showProfileUpdateResult(boolean success, String message) {
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Profile Update");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane pane = alert.getDialogPane();
        pane.setStyle("-fx-background-color: " + ThemeManager.getDialogBackground() + ";");
        alert.showAndWait();
    }
}
