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
    private boolean firstMove = true;
    @Override
    public Collection<BoardTile> setPossibleMoves() {
        possibleMoves.clear(); //in case they are kept from the previous move
        int[][]offsets;
        if(firstMove){
            offsets = new int[][]{{1, 0}, {1, 1}, {1, -1}, {2, 0}};
            firstMove = false;
        }
        else {
            offsets = new int[][]{{1, 0}, {1, 1}, {1, -1}};
        }
        for (int[] o : offsets) {
            if(color == 0)
                 o[0] *= -1;
            BoardTile candidate = this.getBoardTile().neighbour(o[0], o[1]);
            if (candidate != null && (candidate.getTile().getPieceType() == PieceType.OPEN || (candidate.getTile().color != color && o[1] != 0))) {
                possibleMoves.add(candidate);
            }
        }
        return possibleMoves;
    }

}
