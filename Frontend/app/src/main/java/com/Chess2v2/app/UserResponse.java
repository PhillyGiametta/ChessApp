package com.Chess2v2.app;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("userName")
    private String userName;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("userPassword")
    private String userPassword;

    public int getId() {
        return id; // Return user ID
    }

    public String getUsername() {
        return userName;
    }

    public String getEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
