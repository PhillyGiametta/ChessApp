package com.Chess2v2;

import android.app.Application;

public class ChessApplication extends Application {

    private static ChessApplication instance;

    protected int userId;
    protected String email;
    protected String userName;
    protected String groupName;
    protected String baseUrl = "http://10.90.73.46:8080/";
    protected String webSocketBaseUrl = "ws://10.90.73.46:8080/";

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    public static ChessApplication getInstance() {
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getWebSocketBaseUrl() {
        return webSocketBaseUrl;
    }
}
