package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardSquare;
import Backend.ChessApp.Game.Board.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Entity
@DiscriminatorValue("KING")
public class King extends Piece {
    public King(PieceColor color, Position position) {
        super(color, position);
    }

    public King() {

    }

    @Override
    public boolean isValidMove(Position newPosition, List<BoardSquare> boardSquares) {
        int rowDiff = Math.abs(position.getRow() - newPosition.getRow());
        int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());

        boolean isOneSquareMove = rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0);

        if (!isOneSquareMove) {
            return false;
        }

        BoardSquare destSquare = getBoardSquare(newPosition.getRow(), newPosition.getColumn(), boardSquares);
        if(destSquare == null){
            return false; //Bad destination
        }

        Piece destPiece = destSquare.getPiece();
        if(destPiece == null || destPiece.getColor() != getColor()){
            return true; //Destination is empty or has opponent piece
        }

        return false;
    }
}
