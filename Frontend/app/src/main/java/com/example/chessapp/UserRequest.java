package com.example.chessapp;

import com.google.gson.annotations.SerializedName;

public class UserRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
