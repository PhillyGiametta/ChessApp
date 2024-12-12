package com.Chess2v2.chess;

public class Move {
    public final int fromIndex;
    public final int toIndex;
    public final ChessPiece capturedPiece;
    public final ChessPiece pieceToMove;
    public Move(int fromIndex, int toIndex, ChessPiece chessPiece)
    {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.capturedPiece = chessPiece;
        this.pieceToMove = null;
    }
    public Move(int fromIndex, int toIndex, ChessPiece chessPiece, ChessPiece pieceToMove)
    {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.capturedPiece = chessPiece;
        this.pieceToMove = pieceToMove;
    }
}
