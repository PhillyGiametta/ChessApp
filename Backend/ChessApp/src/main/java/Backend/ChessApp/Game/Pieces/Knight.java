package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardSquare;
import Backend.ChessApp.Game.Board.Position;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Entity
@DiscriminatorValue("KNIGHT")
public class Knight extends Piece {
    public Knight(PieceColor color, Position position) {
        super(color, position);
    }

    public Knight() {

    }

    @Override
    public boolean isValidMove(Position newPosition, List<BoardSquare> boardSquares) {
        if (newPosition.equals(this.position)) {
            return false;
        }

        int rowDiff = Math.abs(this.position.getRow() - newPosition.getRow());
        int colDiff = Math.abs(this.position.getColumn() - newPosition.getColumn());

        boolean isValidLMove = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);

        if (!isValidLMove) {
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
