package com.example.chessapp;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("userName")
    private String userName;

    @SerializedName("userEmail")
    private String userEmail;

    public int getId() {
        return id; // Return user ID
    }

    public String getUsername() {
        return userName;
    }

    public String getEmail() {
        return userEmail;
    }
}
