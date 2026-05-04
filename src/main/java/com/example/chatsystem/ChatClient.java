package com.example.chatsystem;

import com.example.chatsystem.client.controller.ChatController;
import com.example.chatsystem.client.controller.LoginController;
import com.example.chatsystem.client.models.userModel;
import com.example.chatsystem.client.ui.ChatScreen;
import com.example.chatsystem.client.ui.LoginScreen;
import com.example.chatsystem.client.websocket.clientWebSOcket;
import javafx.application.Application;
import javafx.stage.Stage;

public class ChatClient extends Application {

    private Stage primaryStage;
    private userModel currentUser;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Chat System - Login");
         
        showLoginScreen();
        this.primaryStage.show();
    }

    public void showLoginScreen() {
        LoginController loginController = new LoginController(this);
        LoginScreen loginScreen = new LoginScreen(loginController);
        primaryStage.setScene(loginScreen.getScene());
        primaryStage.setTitle("Chat System - Login");
    }

    public void showChatScreen() {
        ChatController chatController = new ChatController(this);
        ChatScreen chatScreen = new ChatScreen(chatController);
        primaryStage.setScene(chatScreen.getScene());
        primaryStage.setTitle("Chat System - Chatting as " + currentUser.getUsername());
    }

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