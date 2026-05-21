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

    // Chat history
    public static final String MSG_TYPE_GET_HISTORY       = "GET_HISTORY";
    public static final String MSG_TYPE_HISTORY_RESPONSE  = "HISTORY_RESPONSE";
    public static final String MSG_TYPE_DELETE_HISTORY    = "DELETE_HISTORY";
    public static final String MSG_TYPE_DELETE_HISTORY_RESPONSE = "DELETE_HISTORY_RESPONSE";

    // Profile updates
    public static final String MSG_TYPE_CHANGE_USERNAME   = "CHANGE_USERNAME";
    public static final String MSG_TYPE_CHANGE_PASSWORD   = "CHANGE_PASSWORD";
    public static final String MSG_TYPE_UPDATE_PROFILE_RESPONSE = "UPDATE_PROFILE_RESPONSE";
}
