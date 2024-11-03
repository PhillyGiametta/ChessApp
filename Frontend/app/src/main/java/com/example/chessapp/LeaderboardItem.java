package com.example.chessapp;

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
    public String toString(){
        return "Name: " + playerName + "\n" + "Rating: " + Integer.toString(playerRating) + "\n" + "Rank: " + Integer.toString(rank) + "\n";
    }
}
