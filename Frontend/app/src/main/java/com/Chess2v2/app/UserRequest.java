package com.Chess2v2.app;

import com.google.gson.annotations.SerializedName;

public class UserRequest {

    @SerializedName("userName")
    private String userName;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("userPassword")
    private String userPassword;

    // Constructor for username, userEmail, and password
    public UserRequest(String username, String email, String password) {
        this.userName = username;
        this.userEmail = email;
        this.userPassword = password;
    }

    // Overloaded constructor for username and password only (for login)
    public UserRequest(String username, String password) {
        this.userName = username;
        this.userPassword = password;
    }

    // Getters if needed
    public String getUsername() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String userPassword() {
        return userPassword;
    }
}
