package com.example.chatsystem.client.websocket;

import com.example.chatsystem.client.util.Constants;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class clientWebSOcket extends WebSocketClient {
    private static clientWebSOcket instance;
    private messageListener listener;

    private clientWebSOcket(URI serverUri) {
        super(serverUri);
    }

    public static void initialize(messageListener listener) {
        try {
            if (instance != null) {
                instance.close();
            }
            instance = new clientWebSOcket(new URI(Constants.SERVER_URL));
            instance.setListener(listener);
            instance.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static clientWebSOcket getInstance() {
        return instance;
    }

    public void setListener(messageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (listener != null) listener.onConnectionOpened();
    }

    @Override
    public void onMessage(String message) {
        if (listener != null) {
            try {
                JSONObject json = new JSONObject(message);
                listener.onMessageReceived(json);
            } catch (Exception e) {
                System.err.println("Failed to parse incoming message: " + message);
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (listener != null) listener.onConnectionClosed();
    }

    @Override
    public void onError(Exception ex) {
        if (listener != null) listener.onError(ex);
    }
}
