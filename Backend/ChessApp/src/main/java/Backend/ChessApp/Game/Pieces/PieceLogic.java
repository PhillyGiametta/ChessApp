package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardTile;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceLogic {

    protected PieceType pieceType;
    protected BoardTile boardTile;
    protected Collection<BoardTile> possibleMoves;
    protected int color; //0 is white, 1 is black, -1 is colorless for emptyspace
    /**
     * Sets the piece type for the individual pieces may need more later.
     * @param pieceType
     */
    public PieceLogic(PieceType pieceType, int color){
        this.pieceType = pieceType;
        this.color = color;
    }

    public int oppositeColor(int color){
        return switch (color) {
            case 0 -> 1;
            case 1 -> 0;
            default -> -1;
        };
    }

    /**
     * This returns a list of possible moves for selected piece.
     * Used as a getter for the moves
     * @
     * @return possibleMoves
     */
    public Collection<BoardTile> getPossibleMoves(){return possibleMoves;}

    /**
     * Sets the list for possibles moves, individual pieces vary on this must be set in class
     * @return
     */
    public abstract Collection<BoardTile> setPossibleMoves();

    /**
     * This is a move token, will be different for every type of piece
     * may only be allowed to move based on possible moves
     */
    public abstract void move();

    /**
     * Sees if king is checked (threatened of capture)
     * Boolean dictates what moves are available to either block check or only let king move
     * @return
     */
    public boolean kingIsChecked(){
        return false;
    }

    public void setColor(int color){this.color = color;}
    public int getColor(){return this.color;}
    public PieceType getPieceType() {return pieceType;}
    public void setPieceType(PieceType pieceType) {this.pieceType = pieceType;}
    public void setBoardTile(BoardTile bT) {this.boardTile = bT;}
    public BoardTile getBoardTile(){return boardTile;}
    //King will need check and checkmate logic.

}
