package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardTile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends PieceLogic{
    /**
     * Sets the piece type for the individual pieces may need more later.
     *
     * @param pieceType
     */
    public Queen(PieceType pieceType, int color) {
        super(pieceType,color);
    }

    @Override
    public Collection<BoardTile> setPossibleMoves() {
        List<BoardTile> possibleMoves = new ArrayList<BoardTile>();
        int[][] offsets = {
                {-1, -1},{-2,-2},{-3,-3},{-4,-4},{-5,-5},{-6,-6},{-7,-7},
                {1, 1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},
                {1, -1},{2,-2},{3,-3},{4,-4},{5,-5},{6,-6},{7,-7},
                {-1,1},{-2,2},{-3,3},{-4,4},{-5,5},{-6,6},{-7,7}, //diagonal
                {-1, 0},{-2,0},{-3,0},{-4,0},{-5,0},{-6,0},{-7,0},
                {0, 1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},
                {1, 0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},
                {0, -1},{0,-2},{0,-3},{0,-4},{0,-5},{0,-6},{0,-7}, //straight
        };
        for (int[] o : offsets) {
            BoardTile candidate = this.getBoardTile().neighbour(o[0], o[1]);
            if (candidate != null && (candidate.getTile() == null || candidate.getTile().color != color)) {
                possibleMoves.add(candidate);
            }
        }
        this.possibleMoves = possibleMoves;
        return possibleMoves;
    }


}
