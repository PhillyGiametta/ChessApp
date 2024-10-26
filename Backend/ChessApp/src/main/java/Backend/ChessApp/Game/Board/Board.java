package Backend.ChessApp.Game.Board;

import Backend.ChessApp.Game.Pieces.PieceLogic;
import Backend.ChessApp.Game.Pieces.PieceType;

public class Board {

    /**
     * contains the column and row of selected piece, that is going to be controlled by {selectedPiece}
     */
    char selectedColumn;
    int selectedRow;
    PieceLogic selectedPiece;

    /**
     * Creates a new fresh board. With initialized pieces.
     */
    public Board(){

    }

    /**
     * Builds a chess board for pieces to move on, 2d array of BoardTiles
     * null BoardTile means edge, otherwise an available move, as specified per piece
     */
    private void BoardBuilder(){
        int row=8;
        char col = 'A';
        BoardTile[][] board = null;
        for(char i = 0; i < 8; i++, row++){
            for(int j = 0; j < 0; j++, col++){
                board[i][j] = new BoardTile(null, col,row);

            }
        }
    }


}
