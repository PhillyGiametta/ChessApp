package com.example.chessapp;

public class LeaderboardEntry {

    int rankPosition, rating, userWins, userLosses;
    double userWLRatio;
    UserData user;

    public LeaderboardEntry(int rankPosition, int rating, int userWins, int userLosses, double userWLRatio) {
        this.rankPosition = rankPosition;
        this.userWLRatio = userWLRatio;
        this.userLosses = userLosses;
        this.rating = rating;
        this.userWins = userWins;
    }

    public int getRankPosition() {
        return rankPosition;
    }

    public void setRankPosition(int rankPosition) {
        this.rankPosition = rankPosition;
    }

    public UserData getUser(){
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

    public String toString(){
        String rating = "Rating: " + Integer.toString(this.rating) + "\n";
        String rankPosition = "Rank: " + Integer.toString(this.rankPosition) + "\n";
        String userWins = "Wins: " + Integer.toString(this.userWins) + "\n";
        String userLosses = "Loses: " + Integer.toString(this.userLosses) + "\n";
        String userWLRatio = "Win/loss ratio: " + Double.toString(this.userWLRatio) + "\n";
        return rankPosition + rating + userWins + userLosses + userWLRatio;
    }

}
