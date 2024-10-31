package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardTile;

import java.util.Collection;
import java.util.List;

public class EmptySpace extends PieceLogic{

    /**
     * Sets the piece type for the individual pieces may need more later.
     *
     * @param pieceType
     * @param color
     */
    public EmptySpace(PieceType pieceType, int color) {
        super(pieceType, color);
    }

    @Override
    public Collection<BoardTile> setPossibleMoves() {
        return List.of();
    }

}
