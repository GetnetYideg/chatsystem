package com.example.chatsystem.server.service;

import com.example.chatsystem.server.model.messages;
import com.example.chatsystem.server.repository.messageRepository;

public class ChatService {
    private messageRepository msgRepo;

    public ChatService() {
        this.msgRepo = new messageRepository();
    }

    public boolean saveMessage(int senderId, int receiverId, String text) {
        messages msg = new messages();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setMessage(text);
        return msgRepo.saveMessage(msg);
    }
}
