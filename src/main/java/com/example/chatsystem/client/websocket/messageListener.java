package com.example.chatsystem.client.websocket;

import org.json.JSONObject;

public interface messageListener {
    void onMessageReceived(JSONObject jsonMessage);
    void onConnectionOpened();
    void onConnectionClosed();
    void onError(Exception ex);
}
