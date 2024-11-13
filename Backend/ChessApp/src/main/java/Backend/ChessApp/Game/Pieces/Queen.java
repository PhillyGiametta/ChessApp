package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardSquare;
import Backend.ChessApp.Game.Board.Position;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {
    public Queen(PieceColor color, Position position) {
        super(color, position);
    }

    public Queen() {

    }

    @Override
    public boolean isValidMove(Position newPosition, List<BoardSquare> boardSquares) {
        if (newPosition.equals(this.position)) {
            return false;
        }

        int rowDiff = Math.abs(newPosition.getRow() - this.position.getRow());
        int colDiff = Math.abs(newPosition.getColumn() - this.position.getColumn());

        boolean straightLine = this.position.getRow() == newPosition.getRow()
                || this.position.getColumn() == newPosition.getColumn();

        boolean diagonal = rowDiff == colDiff;

        if (!straightLine && !diagonal) {
            return false;
        }

        int rowDirection = Integer.compare(newPosition.getRow(), this.position.getRow());
        int colDirection = Integer.compare(newPosition.getColumn(), this.position.getColumn());

        int currentRow = this.position.getRow() + rowDirection;
        int currentCol = this.position.getColumn() + colDirection;
        while (currentRow != newPosition.getRow() || currentCol != newPosition.getColumn()) {
            BoardSquare nextSquare = getBoardSquare(currentRow, currentCol, boardSquares);
            if(nextSquare != null) {
                return false;
            }

            currentRow += rowDirection;
            currentCol += colDirection;
        }

        BoardSquare destSquare = getBoardSquare(newPosition.getRow(), newPosition.getColumn(), boardSquares);
        return destSquare == null || destSquare.getPiece().getColor() != this.getColor();
    }
}
