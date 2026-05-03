package com.example.chatsystem.client.models;

public class userModel {
    private int id;
    private String username;

    public userModel(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
