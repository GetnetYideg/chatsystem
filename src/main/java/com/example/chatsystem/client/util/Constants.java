package com.example.chatsystem.client.util;

public class Constants {
    public static final String SERVER_URL = "ws://localhost:1235";

    // Auth
    public static final String MSG_TYPE_LOGIN            = "LOGIN";
    public static final String MSG_TYPE_REGISTER         = "REGISTER";
    public static final String MSG_TYPE_AUTH_SUCCESS     = "AUTH_SUCCESS";
    public static final String MSG_TYPE_AUTH_FAIL        = "AUTH_FAIL";

    // Chat
    public static final String MSG_TYPE_CHAT             = "CHAT";

    // Dashboard
    public static final String MSG_TYPE_GET_CONTACTS      = "GET_CONTACTS";
    public static final String MSG_TYPE_CONTACTS_RESPONSE = "CONTACTS_RESPONSE";
    public static final String MSG_TYPE_FIND_USER         = "FIND_USER";
    public static final String MSG_TYPE_FIND_USER_RESPONSE= "FIND_USER_RESPONSE";
}
