package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardTile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends PieceLogic{
    /**
     * Sets the piece type for the individual pieces may need more later.
     *
     * @param pieceType
     */
    public Pawn(PieceType pieceType,int color) {
        super(pieceType,color);
    }

    @Override
    public Collection<BoardTile> setPossibleMoves() {
        List<BoardTile> possibleMoves = new ArrayList<BoardTile>();
        int[][] offsets = {
                {1,0},{1,1},{1,-1}
        };
        for (int[] o : offsets) {
            BoardTile candidate = this.getBoardTile().neighbour(o[0], o[1]);
            if (candidate != null && (candidate.getTile() == null || candidate.getTile().color != color)
                    && (o[1] != 0 && candidate.getTile().color == oppositeColor(candidate.getTile().color))) {
                possibleMoves.add(candidate);
            }
        }
        this.possibleMoves = possibleMoves;
        return possibleMoves;
    }

}
