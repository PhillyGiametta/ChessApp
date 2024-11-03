package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardTile;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceLogic {

    protected PieceType pieceType;
    protected BoardTile boardTile;
    protected Collection<BoardTile> possibleMoves = new ArrayList<>();
    protected boolean kingInCheck = false;
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
     * This is a move token, will be different for every type of piece if(this.getPossibleMoves() )
     * may only be allowed to move based on possible moves
     */
    public void move(BoardTile newBoardTile) {
        if(possibleMoves.contains(newBoardTile)){
            BoardTile current = this.getBoardTile();
            newBoardTile.setTile(this);
            boardTile = newBoardTile;
            current.setTile(new EmptySpace(PieceType.OPEN, -1));
        }
    }

    /**
     * Sees if king is checked (threatened of capture)
     * Boolean dictates what moves are available to either block check or only let king move
     * @return
     */
    public boolean kingIsChecked(){
        return kingInCheck;
    }

    public void setColor(int color){this.color = color;}
    public int getColor(){return this.color;}
    public PieceType getPieceType() {return pieceType;}
    public void setPieceType(PieceType pieceType) {this.pieceType = pieceType;}
    public void setBoardTile(BoardTile bT) {this.boardTile = bT;}
    public BoardTile getBoardTile(){return boardTile;}
    public String printPossibleMoves(){
        String s = "";
        for(BoardTile b: possibleMoves)
            s+=b.toString();
        return s;
    }
    //King will need check and checkmate logic.

}
