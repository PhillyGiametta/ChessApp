package Backend.ChessApp.Game.Board;


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
    private PieceType pieceType = null;
    private char col;
    private int row;

    public BoardTile(PieceType pieceType, char col, int row){
        this.pieceType = pieceType;
        this.col = col;
        this.row = row;
    }

    /**
     * checks current tile to see if it has piece on it
     */
    public boolean isFilled(){
        return pieceType != PieceType.OPEN;
    }
    /**
     * Set the board tile piece will handle main updates to individual tile
     */
    public void setTile(PieceType pieceType) {
        this.pieceType = pieceType;
    }
    public PieceType getTile(){
        return this.pieceType;
    }

    @Override
    public String toString() {
        return String.valueOf(this.pieceType.getSymbol());
    }
}
