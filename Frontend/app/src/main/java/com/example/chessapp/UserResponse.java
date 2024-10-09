package com.example.chessapp;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("key")
    private String key;

    @SerializedName("one")
    private String one;

    public String getKey() {
        return key;
    }

    public String getOne() {
        return one;
    }
}
