package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.Position;

public abstract class Piece {
    protected Position position;
    protected PieceColor color;

    public Piece(PieceColor color, Position position) {
        this.color = color;
        this.position = position;
    }

    public PieceColor getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract boolean isValidMove(Position newPosition, Piece[][] board);

    @Override
    public String toString() {
        if (this instanceof King) {
            return "K";
        } else if (this instanceof Queen) {
            return "Q";
        } else if (this instanceof Rook) {
            return "R";
        } else if (this instanceof Bishop) {
            return "B";
        } else if (this instanceof Knight) {
            return "N";
        } else if (this instanceof Pawn) {
            return "P";
        } else {
            return "?"; // Fallback for unknown pieces
        }
    }
}
