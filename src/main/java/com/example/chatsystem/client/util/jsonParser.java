package com.example.chatsystem.client.util;

import com.example.chatsystem.client.models.message;
import org.json.JSONObject;

public class jsonParser {
    
    public static String createAuthMessage(String type, String username, String password) {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("username", username);
        json.put("password", password);
        return json.toString();
    }

    public static String createChatMessage(int senderId, String senderUsername, int receiverId, String content) {
        JSONObject json = new JSONObject();
        json.put("type", Constants.MSG_TYPE_CHAT);
        json.put("sender_id", senderId);
        json.put("sender_username", senderUsername);
        json.put("receiver_id", receiverId);
        json.put("message", content);
        return json.toString();
    }

    public static message parseMessage(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            if (Constants.MSG_TYPE_CHAT.equals(json.optString("type"))) {
                return new message(
                    json.optInt("sender_id", -1),
                    json.optString("sender_username", "Unknown"),
                    json.optInt("receiver_id", -1),
                    json.optString("message", "")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
