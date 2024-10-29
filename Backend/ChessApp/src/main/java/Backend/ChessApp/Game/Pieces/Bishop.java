package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardTile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends PieceLogic{
    /**
     * Sets the piece type for the individual pieces may need more later.
     *
     * @param pieceType
     */
    public Bishop(PieceType pieceType,int color) {
        super(pieceType,color);
    }

    @Override
    public Collection<BoardTile> setPossibleMoves() {
        List<BoardTile> possibleMoves = new ArrayList<BoardTile>();
        int[][] offsets = {
                {-1, -1},{-2,-2},{-3,-3},{-4,-4},{-5,-5},{-6,-6},{-7,-7},
                {1, 1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},
                {1, -1},{2,-2},{3,-3},{4,-4},{5,-5},{6,-6},{7,-7},
                {-1,1},{-2,2},{-3,3},{-4,4},{-5,5},{-6,6},{-7,7},
        };
        for (int[] o : offsets) {
            BoardTile candidate = this.getBoardTile().neighbour(o[0], o[1]);
            if (candidate != null && (candidate.getTile() == null || candidate.getTile().color != color)) {
                possibleMoves.add(candidate);
            }
        }
        return possibleMoves;
    }

    @Override
    public void move() {

    }
}
