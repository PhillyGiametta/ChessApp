package com.Chess2v2.app;

public class UserData {

    String userEmail, userName, userPassword, userMadeDate, userLastLoginDate,
            activity, passwordResetToken, userId;

    public UserData(String userEmail, String userName, String userPassword, String userid) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userId = userid;
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
}
