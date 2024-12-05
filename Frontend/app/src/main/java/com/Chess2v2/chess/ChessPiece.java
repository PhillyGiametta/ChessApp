package com.Chess2v2.chess;

import androidx.annotation.DrawableRes;

public class ChessPiece {
    @DrawableRes
    private final int imageResource;
    private final boolean isWhite;

    public ChessPiece(@DrawableRes int imageResource, String knight, boolean isWhite) {
        this.imageResource = imageResource;
        this.isWhite = isWhite;
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public boolean isWhitePiece() {
        return isWhite;
    }

    public String getName() {
        return name;
    }
}
