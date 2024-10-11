package com.example.chessapp;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    public int getId() {
        return id; // Return user ID
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
