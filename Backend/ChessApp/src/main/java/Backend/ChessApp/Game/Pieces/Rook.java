package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardSquare;
import Backend.ChessApp.Game.Board.Position;
import jakarta.persistence.Entity;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Piece {
    public Rook(PieceColor color, Position position) {
        super(color, position);
    }

    public Rook() {

    }

    @Override
    public boolean isValidMove(Position newPosition, List<BoardSquare> boardSquares) {
        if (position.getRow() == newPosition.getRow()) {
            int columnStart = Math.min(position.getColumn(), newPosition.getColumn()) + 1;
            int columnEnd = Math.max(position.getColumn(), newPosition.getColumn());

            for (int column = columnStart; column < columnEnd; column++) {
                BoardSquare nextSquare = getBoardSquare(position.getRow(), column, boardSquares);
                if (nextSquare != null) {
                    return false;
                }
            }
        } else if (position.getColumn() == newPosition.getColumn()) {
            int rowStart = Math.min(position.getRow(), newPosition.getRow()) + 1;
            int rowEnd = Math.max(position.getRow(), newPosition.getRow());

            for (int row = rowStart; row < rowEnd; row++) {
                BoardSquare nextSquare = getBoardSquare(row, position.getColumn(), boardSquares);

                if (nextSquare != null) {
                    return false;
                }
            }
        } else {
            return false;
        }

        BoardSquare destSquare = getBoardSquare(newPosition.getRow(), newPosition.getColumn(), boardSquares);
        if (destSquare.getPiece() == null) {
            return true;
        } else if (destSquare.getPiece().getColor() != this.getColor()) {
            return true;
        }

        return false;
    }
}
