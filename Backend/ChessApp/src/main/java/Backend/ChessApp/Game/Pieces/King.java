package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardTile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends PieceLogic{

    boolean checked;
    boolean checkmated;
    /**
     * Sets the piece type for the individual pieces may need more later.
     *
     * @param pieceType
     */
    public King(PieceType pieceType,int color) {
        super(pieceType,color);
    }

    @Override
    public Collection<BoardTile> setPossibleMoves() {
        List<BoardTile> possibleMoves = new ArrayList<BoardTile>();
        int[][] offsets = {
                {1,0},{1,1},{1,-1},{-1,0},{-1,-1},{-1,1},{0,1},{0,-1}
        };
        for (int[] o : offsets) {
            BoardTile candidate = this.getBoardTile().neighbour(o[0], o[1]);
            if (candidate != null && (candidate.getTile() == null || candidate.getTile().color != color)
                    && (!candidate.getTile().kingIsChecked())) {
                possibleMoves.add(candidate);
            }
        }
        this.possibleMoves = possibleMoves;
        return possibleMoves;
    }

    public void setCheckStatus(Collection<BoardTile> otherMoves){
        //if this is in path of any pieces possible moves set true

        //else set false;
    }


}
