package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardTile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends PieceLogic{
    /**
     * Sets the piece type for the individual pieces may need more later.
     *
     * @param pieceType
     */
    public Knight(PieceType pieceType,int color) {
        super(pieceType,color);
    }

    @Override
    public Collection<BoardTile> setPossibleMoves() {
        List<BoardTile> possibleMoves = new ArrayList<BoardTile>();
        int[][] offsets = {
                {-2, 1},
                {-1, 2},
                {1, 2},
                {2, 1},
                {2, -1},
                {1, -2},
                {-1, -2},
                {-2, -1}
        };
        for (int[] o : offsets) {
            BoardTile candidate = this.getBoardTile().neighbour(o[0], o[1]);
            if (candidate != null && (candidate.getTile() == null || candidate.getTile().color != this.getColor())) {
                possibleMoves.add(candidate);
            }
        }
        this.possibleMoves = possibleMoves;
        return possibleMoves;
    }

}
