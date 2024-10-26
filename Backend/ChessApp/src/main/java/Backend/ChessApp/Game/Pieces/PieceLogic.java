package Backend.ChessApp.Game.Pieces;

public abstract class PieceLogic {

    PieceType pieceType;
    String[] possibleMoves;
    int color;
    /**
     * Sets the piece type for the individual pieces may need more later.
     * @param pieceType
     */
    public PieceLogic(PieceType pieceType, int color){
        this.pieceType = pieceType;
        this.color = color;
    }


    /**
     * This returns a list of possible moves for selected piece.
     * Used as a setter and getter for the possible moves
     * @params TBD
     * @return possibleMoves
     */
    public String[] possibleMoves(){

        return possibleMoves;
    }

    /**
     * This is a move token, will be different for every type of piece
     * may only be allowed to move based on possible moves
     */
    public abstract void move();

    /**
     * Checks if a piece is being threatened by another piece.
     * IMPORTANT FOR KING, allows for easier capture in more methods
     * @return
     */
    public boolean underAttack(){
        return false;
    }

    //King will need check and checkmate logic.

}
