package com.Chess2v2.leaderboard;

public class LeaderboardEntry {

    int rankPosition, rating, userWins, userLosses;
    double userWLRatio;

    public int getRankPosition() {
        return rankPosition;
    }

    public void setRankPosition(int rankPosition) {
        this.rankPosition = rankPosition;
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
}
