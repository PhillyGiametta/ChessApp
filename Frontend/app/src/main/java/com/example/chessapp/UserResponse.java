package com.example.chessapp;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("key")
    private String key;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    public String getKey() {
        return key;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
