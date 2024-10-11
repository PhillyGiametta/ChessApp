package com.example.chessapp;

import com.google.gson.annotations.SerializedName;

public class UserRequest {

    @SerializedName("userName")
    private String userName;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("password")
    private String password;

    // Constructor for username, userEmail, and password
    public UserRequest(String username, String email, String password) {
        this.userName = username;
        this.userEmail = email;
        this.password = password;
    }

    // Overloaded constructor for username and password only (for login)
    public UserRequest(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    // Getters if needed
    public String getUsername() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword() {
        return password;
    }
}
