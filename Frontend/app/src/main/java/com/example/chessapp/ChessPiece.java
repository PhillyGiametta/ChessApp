package com.example.chessapp;

import androidx.annotation.DrawableRes;

public class ChessPiece {
    @DrawableRes
    private final int imageResource;
    private final boolean isWhite;

    public ChessPiece(@DrawableRes int imageResource, boolean isWhite) {
        this.imageResource = imageResource;
        this.isWhite = isWhite;
    }

    public int getImageResource() {
        return imageResource;
    }

    public boolean isWhitePiece() {
        return isWhite;
    }
}
