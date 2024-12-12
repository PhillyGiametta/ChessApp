package com.Chess2v2.chess;

public class Marker {

    public enum MarkerType {
        SQUARE, DOT, ERROR_SQUARE
    }

    private final int positionIndex;
    private final MarkerType markerType;

    public Marker(int positionIndex, MarkerType markerType) {
        this.positionIndex = positionIndex;
        this.markerType = markerType;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public MarkerType getMarkerType() {
        return markerType;
    }
}

