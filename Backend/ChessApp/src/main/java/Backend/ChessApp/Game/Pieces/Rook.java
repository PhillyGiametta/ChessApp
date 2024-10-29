package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardTile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends PieceLogic{
    /**
     * Sets the piece type for the individual pieces may need more later.
     *
     * @param pieceType
     */
    public Rook(PieceType pieceType, int color) {
        super(pieceType, color);
    }

    @Override
    public Collection<BoardTile> setPossibleMoves() {
        List<BoardTile> possibleMoves = new ArrayList<BoardTile>();
        int[][] offsets = {
                {-1, 0},{-2,0},{-3,0},{-4,0},{-5,0},{-6,0},{-7,0},
                {0, 1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},
                {1, 0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},
                {0, -1},{0,-2},{0,-3},{0,-4},{0,-5},{0,-6},{0,-7},
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
