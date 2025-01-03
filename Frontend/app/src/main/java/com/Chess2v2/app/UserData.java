package com.Chess2v2.app;

import com.Chess2v2.chess.Clock;

public class UserData {

    String userEmail, userName, userPassword, userMadeDate, userLastLoginDate,
            activity, passwordResetToken, userId;
    short settings;
    private Clock clock;


    public UserData(String userEmail, String userName, String userPassword, String userid) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userId = userid;
        settings = 0; //TODO
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserMadeDate() {
        return userMadeDate;
    }

    public void setUserMadeDate(String userMadeDate) {
        this.userMadeDate = userMadeDate;
    }

    public String getUserLastLoginDate() {
        return userLastLoginDate;
    }

    public void setUserLastLoginDate(String userLastLoginDate) {
        this.userLastLoginDate = userLastLoginDate;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public short getSettings() { return settings; }

    public void setSettings(short newSettings) { this.settings = newSettings;}

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
