package com.example.myapplication;

public class PlayerModel {
    String name;
    String rating;
    int image;

    public PlayerModel(String name, String rating, int image) {
        this.name = name;
        this.rating = rating;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }
}
