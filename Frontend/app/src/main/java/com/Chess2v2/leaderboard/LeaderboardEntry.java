package com.Chess2v2.leaderboard;

import com.Chess2v2.app.UserData;

import java.io.Serializable;

public class LeaderboardEntry implements Serializable {

    int rankPosition, rating, userWins, userLosses;
    double userWLRatio;
    UserData user;

    public LeaderboardEntry(int rankPosition, int rating, int userWins, int userLosses, double userWLRatio, UserData user) {
        this.rankPosition = rankPosition;
        this.userWLRatio = userWLRatio;
        this.userLosses = userLosses;
        this.rating = rating;
        this.userWins = userWins;
        this.user = user;
    }

    // Add a getter method to access the username
    public String getUserName() {
        if (user != null) {
            return user.getUserName(); // Assuming UserData has a method called getUserName()
        }
        return "Unknown User";
    }

    // Existing getters and setters

    public int getRankPosition() {
        return rankPosition;
    }

    public void setRankPosition(int rankPosition) {
        this.rankPosition = rankPosition;
    }

    public UserData getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getUserWins() {
        return userWins;
    }

    public void setUserWins(int userWins) {
        this.userWins = userWins;
    }

    public int getUserLosses() {
        return userLosses;
    }

    public void setUserLosses(int userLosses) {
        this.userLosses = userLosses;
    }

    public double getUserWLRatio() {
        return userWLRatio;
    }

    public void setUserWLRatio(double userWLRatio) {
        this.userWLRatio = userWLRatio;
    }

    @Override
    public String toString() {
        String rating = "Rating: " + this.rating + "\n";
        String rankPosition = "Rank: " + this.rankPosition + "\n";
        String userWins = "Wins: " + this.userWins + "\n";
        String userLosses = "Losses: " + this.userLosses + "\n";
        String userWLRatio = "Win/loss ratio: " + this.userWLRatio + "\n";
        String userName = "Username: " + getUserName() + "\n";
        return userName + rankPosition + rating + userWins + userLosses + userWLRatio;
    }
}
