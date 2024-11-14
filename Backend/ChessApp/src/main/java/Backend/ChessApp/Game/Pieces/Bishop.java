package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardSquare;
import Backend.ChessApp.Game.Board.Position;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(PieceColor color, Position position) {
        super(color, position);
    }

    public Bishop() {

    }

    @Override
    public boolean isValidMove(Position newPosition, List<BoardSquare> boardSquares) {
        int rowDiff = Math.abs(position.getRow() - newPosition.getRow());
        int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());

        if (rowDiff != colDiff) {
            return false;
        }

        int rowStep = newPosition.getRow() > position.getRow() ? 1 : -1;
        int colStep = newPosition.getColumn() > position.getColumn() ? 1 : -1;

        for (int i = 1; i < rowDiff; i++) {
            int currentRow = position.getRow() + i * rowStep;
            int currentCol = position.getColumn() + i * colStep;

            BoardSquare square = getBoardSquare(currentRow, currentCol, boardSquares);
            if(square != null && square.getPiece() != null){
                return false; //Piece in the way
            }
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
