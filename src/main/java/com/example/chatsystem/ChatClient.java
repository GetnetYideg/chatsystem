package com.example.chatsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ChatClient extends Application {

    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void start(Stage stage) throws Exception {
        Socket socket = new Socket("localhost", 1235);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);

        TextField inputField = new TextField();

        Button sendBtn = new Button("Send");
        // Chat Area (TextArea)
        chatArea.setStyle(
            "-fx-background-color: #1e1e1e;" +
            "-fx-text-fill: #e0e0e0;" +
            "-fx-font-size: 15px;" +
            "-fx-padding: 12px;" +
            "-fx-border-color: #333333;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-focus-color: #00b4d8;" +
            "-fx-faint-focus-color: transparent;"
        );

        // Input Field (TextField)
        inputField.setStyle(
            "-fx-background-color: #2d2d2d;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-font-size: 15px;" +
            "-fx-prompt-text-fill: #888888;" +
            "-fx-padding: 12px;" +
            "-fx-border-color: #444444;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-focus-color: #00b4d8;"
        );

        // Send Button
        sendBtn.setStyle(
            "-fx-background-color: #00b4d8;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12px 24px;" +
            "-fx-background-radius: 8px;" +
            "-fx-border-radius: 8px;" +
            "-fx-cursor: hand;"
        );

        // Optional: Hover effect for Send Button (using inline is limited, but we can improve it)
        sendBtn.setOnMouseEntered(e -> 
            sendBtn.setStyle(
                "-fx-background-color: #0099b8;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12px 24px;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;"
            )
        );

        sendBtn.setOnMouseExited(e -> 
            sendBtn.setStyle(
                "-fx-background-color: #00b4d8;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12px 24px;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;"
            )
        );
        sendBtn.setOnAction(e -> {
            String msg = inputField.getText();
            out.println(msg);
            inputField.clear();
        });

        VBox root = new VBox(chatArea, inputField, sendBtn);
        root.setStyle(
            "-fx-background-color: #121212;" +
            "-fx-padding: 15px;" +
            "-fx-spacing: 10px;"
        );
        chatArea.setStyle(
            "-fx-background-color: #1e1e1e;" +
            "-fx-text-fill: #242323ff;" +
            "-fx-font-size: 15px;" +
            "-fx-padding: 15px;" +
            "-fx-border-color: #333;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10px;" +
            "-fx-background-radius: 10px;" +
            "-fx-focus-color: transparent;" +     // removes blue border on focus
            "-fx-faint-focus-color: transparent;"
        );

        // Allow text wrapping and auto-scroll
        chatArea.setWrapText(true);
        
        Scene scene = new Scene(root, 500, 700);

        stage.setScene(scene);
        stage.setTitle("Chat Client");
        stage.show();

        // Thread to receive messages
        new Thread(() -> {
            String msg;
            try {
                while ((msg = in.readLine()) != null) {
                    String finalMsg = msg;
                    javafx.application.Platform.runLater(() -> {
                        chatArea.appendText(finalMsg + "\n");
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch();
    }
}