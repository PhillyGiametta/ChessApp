package com.Chess2v2.chess;

import androidx.annotation.DrawableRes;

public class Position {
    @DrawableRes
    private final int imageResource;

    protected ChessPiece piece;
    protected int index;
    protected final Coord coord;

    protected final boolean isWhite;

    public Position(int index, @DrawableRes int imageResource) {
        this.index = index;
        this.coord = toCoord(index);
        this.isWhite = isWhite(index);
        this.imageResource = imageResource;
    }

    public static Coord toCoord(int pos) {
        return new Coord(pos % 8, pos / 8);
    }

    public static boolean isWhite(int pos) {
        return (pos + (pos / 8) % 2)% 2 == 0;
    }

    public static int coordToInt(int x, int y) {
        return y * 8 + x;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

    public int getIndex() {
        return index;
    }

    public int getImageResource() {
        return imageResource;
    }
}
