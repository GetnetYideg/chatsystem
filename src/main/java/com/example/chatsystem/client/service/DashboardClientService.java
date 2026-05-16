package com.example.chatsystem.client.service;

import com.example.chatsystem.client.util.Constants;
import com.example.chatsystem.client.websocket.clientWebSOcket;
import org.json.JSONObject;

public class DashboardClientService {

    /** Ask the server for the list of users this user has chatted with. */
    public void requestContacts(int userId) {
        JSONObject json = new JSONObject();
        json.put("type", Constants.MSG_TYPE_GET_CONTACTS);
        json.put("user_id", userId);
        send(json.toString());
    }

    /** Ask the server to look up a user by their username. */
    public void findUserByUsername(String username) {
        JSONObject json = new JSONObject();
        json.put("type", Constants.MSG_TYPE_FIND_USER);
        json.put("username", username);
        send(json.toString());
    }

    private void send(String payload) {
        clientWebSOcket client = clientWebSOcket.getInstance();
        if (client != null && client.isOpen()) {
            client.send(payload);
        }
    }
}
