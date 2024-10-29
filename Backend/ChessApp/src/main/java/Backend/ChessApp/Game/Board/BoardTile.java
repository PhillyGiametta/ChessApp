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
    private Board board;

    public BoardTile(PieceLogic pieceLogic, char col, int row, Board board){
        this.pieceLogic = pieceLogic;
        this.col = col;
        this.row = row;
        this.board = board;
    }

    /**
     * checks current tile to see if it has piece on it
     */
    public boolean isFilled(){
        return pieceLogic.getPieceType() != PieceType.OPEN;
    }
    /**
     * Gets the neighbor for the tile
     */
    public BoardTile neighbour(int rowOffset, int colOffset) {
        int newRow = this.row + rowOffset;
        char newCol = (char)(this.col + colOffset);

        if (newRow >= 0 && newRow < 8 && newCol >= 'A' && newCol <= 'H') {
            return this.board.getTile(newRow, newCol); // Retrieve tile from the board
        } else {
            return null; // Out of bounds
        }
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
    public void setCol(char col){this.col = col;}
    public char getCol(){return this.col;}
    public void setRow(int row){this.row = row;}
    public int getRow(){return row;}

    @Override
    public String toString() {
        return String.valueOf(this.pieceLogic.getPieceType().getSymbol() + this.pieceLogic.getColor());
    }
}
