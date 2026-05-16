package com.example.chatsystem;

import com.example.chatsystem.client.controller.DashboardController;
import com.example.chatsystem.client.controller.LoginController;
import com.example.chatsystem.client.controller.PeerChatController;
import com.example.chatsystem.client.models.userModel;
import com.example.chatsystem.client.ui.DashboardScreen;
import com.example.chatsystem.client.ui.LoginScreen;
import com.example.chatsystem.client.ui.PeerChatScreen;
import com.example.chatsystem.client.websocket.clientWebSOcket;
import javafx.application.Application;
import javafx.stage.Stage;

public class ChatClient extends Application {

    private Stage primaryStage;
    private userModel currentUser;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        showLoginScreen();
        this.primaryStage.show();
    }

    // ── Login ─────────────────────────────────────────────────────────────────
    public void showLoginScreen() {
        LoginController loginController = new LoginController(this);
        LoginScreen loginScreen = new LoginScreen(loginController);
        primaryStage.setScene(loginScreen.getScene());
        primaryStage.setTitle("ChatSystem — Login");
    }

    // ── Dashboard ─────────────────────────────────────────────────────────────
    public void showDashboard() {
        DashboardController controller = new DashboardController(this);
        DashboardScreen screen = new DashboardScreen(controller);
        primaryStage.setScene(screen.getScene());
        primaryStage.setTitle("ChatSystem — " + currentUser.getUsername());
    }

    // ── Peer-to-peer chat ─────────────────────────────────────────────────────
    public void showPeerChatScreen(userModel peer) {
        PeerChatController controller = new PeerChatController(this, peer);
        PeerChatScreen screen = new PeerChatScreen(controller);
        primaryStage.setScene(screen.getScene());
        primaryStage.setTitle("ChatSystem — " + currentUser.getUsername() + " ↔ " + peer.getUsername());
    }

    /**
     * Legacy method kept so LoginController still compiles unchanged.
     * After login, users now land on the dashboard.
     */
    public void showChatScreen() {
        showDashboard();
    }

    // ── User ──────────────────────────────────────────────────────────────────
    public void setCurrentUser(int id, String username) {
        this.currentUser = new userModel(id, username);
    }

    public userModel getCurrentUser() {
        return currentUser;
    }

    @Override
    public void stop() throws Exception {
        clientWebSOcket client = clientWebSOcket.getInstance();
        if (client != null) {
            client.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}