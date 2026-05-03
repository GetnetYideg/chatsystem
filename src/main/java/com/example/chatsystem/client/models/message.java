package com.example.chatsystem.client.models;

public class message {
    private int senderId;
    private String senderUsername;
    private int receiverId;
    private String content;

    public message(int senderId, String senderUsername, int receiverId, String content) {
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.receiverId = receiverId;
        this.content = content;
    }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
