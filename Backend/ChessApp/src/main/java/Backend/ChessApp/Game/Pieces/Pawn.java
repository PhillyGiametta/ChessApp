package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardSquare;
import Backend.ChessApp.Game.Board.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Entity
@DiscriminatorValue("PAWN")
public class Pawn extends Piece {
    public Pawn(PieceColor color, Position position) {
        super(color, position);
    }

    public Pawn() {

    }

    @Override
    public boolean isValidMove(Position newPosition, List<BoardSquare> boardSquares) {
        int forwardDirection = color == PieceColor.WHITE ? -1 : 1;
        int rowDiff = Math.abs((newPosition.getRow() - position.getRow()) * forwardDirection);
        int colDiff = newPosition.getColumn() - position.getColumn();

        BoardSquare destSquare = getBoardSquare(newPosition.getRow(), newPosition.getColumn(), boardSquares);
        if (colDiff == 0 && rowDiff == 1 && destSquare.getPiece() == null) {
            return true;
        }

        boolean isStartingPosition = (color == PieceColor.WHITE && position.getRow() == 6) ||
                (color == PieceColor.BLACK && position.getRow() == 1);
        if (colDiff == 0 && rowDiff == 2 && isStartingPosition){
            BoardSquare betweenSquare = getBoardSquare(position.getRow() + forwardDirection, position.getColumn(), boardSquares);
            if(destSquare != null && destSquare.getPiece() == null && betweenSquare != null && betweenSquare.getPiece() == null){
                return true;
            }
        }

        if (Math.abs(colDiff) == 1 && rowDiff == 1 && destSquare != null && destSquare.getPiece() == null && destSquare.getPiece().getColor() != this.color) {
            return true;
        }

        return false;
    }
}