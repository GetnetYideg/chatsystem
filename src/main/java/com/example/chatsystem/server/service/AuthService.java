package com.example.chatsystem.server.service;

import com.example.chatsystem.server.model.users;
import com.example.chatsystem.server.repository.userRepository;

public class AuthService {
    private userRepository userRepo;

    public AuthService() {
        this.userRepo = new userRepository();
    }

    public users login(String username, String password) {
        return userRepo.authenticateUser(username, password);
    }

    public users register(String username, String password) {
        // Simple check
        if (userRepo.findByUsername(username) != null) {
            return null; // User already exists
        }
        return userRepo.registerUser(username, password);
    }
}
