package com.example.leaderboard;

public class LeaderboardItem {
    private String playerName;
    private int playerRating;
    private int rank;

    public LeaderboardItem(String playerName, int playerRating, int rank) {
        this.playerName = playerName;
        this.playerRating = playerRating;
        this.rank = rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerRating() {
        return playerRating;
    }

    public int getRank() {
        return rank;
    }
}
