package Backend.ChessApp.Game.Board;


import Backend.ChessApp.Game.Pieces.PieceLogic;
import Backend.ChessApp.Game.Pieces.PieceType;

/**
 * Creates an updates a tile according to move logic.
 * This will create an individual square on the board and will allow pieces to move onto capture on,
 * and create a actively updated board for all players and spectators
 */
public class BoardTile {

    /**
     * What piece is on the tile, updates according to board. null means empty, otherwise specify piece
     */

    private PieceLogic pieceLogic;
    private char col;
    private int row;

    public BoardTile(PieceLogic pieceLogic, char col, int row){
        this.pieceLogic = pieceLogic;
        this.col = col;
        this.row = row;
    }

    /**
     * checks current tile to see if it has piece on it
     */
    public boolean isFilled(){
        return pieceLogic.pieceType != PieceType.OPEN;
    }
    /**
     * Set the board tile piece will handle main updates to individual tile
     */
    public void setTile(PieceLogic pieceLogic) {
        this.pieceLogic = pieceLogic;
    }
    public PieceLogic getTile(){
        return this.pieceLogic;
    }

    @Override
    public String toString() {
        return String.valueOf(this.pieceLogic.pieceType.getSymbol());
    }
}
