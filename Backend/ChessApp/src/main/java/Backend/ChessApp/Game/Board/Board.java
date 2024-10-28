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
    BoardTile[][] board;

    /**
     * Creates a new fresh board. With initialized pieces.
     */
    public Board(){
        BoardBuilder();
    }

    /**
     * Builds a chess board for pieces to move on, 2d array of BoardTiles
     * null BoardTile means edge, otherwise an available move, as specified per piece
     */
    private void BoardBuilder(){
        int row=8;
        char col = 'A';
        BoardTile[][] board = new BoardTile[8][8];
        for(int i = 0; i < 8; i++, row--){
            col = 'A';
            for(int j = 0; j < 8; j++, col++){
                board[i][j] = new BoardTile(PieceType.OPEN, col,row);
                if(row == 7 || row == 2)
                    board[i][j].setTile(PieceType.PAWN);
                if(row == 8 || row == 1){
                    if(col == 'A' || col == 'H')
                        board[i][j].setTile(PieceType.ROOK);
                    else if(col == 'B' || col == 'G')
                        board[i][j].setTile(PieceType.KNIGHT);
                    else if(col == 'C' || col == 'F')
                        board[i][j].setTile(PieceType.BISHOP);
                    else if(col == 'D')
                        board[i][j].setTile(PieceType.KING);
                    else if(col == 'E')
                        board[i][j].setTile(PieceType.QUEEN);
                }

            }
        }
        this.board = board;
    }
    private String printBoard(BoardTile[][] board) {
        String consolePrint = "";
        for (BoardTile[] boardTiles : board) {
            for (BoardTile boardTile : boardTiles) {

                consolePrint += (boardTile + "\t");
            }
            consolePrint += "\n"; // New line at the end of each row
        }
        return consolePrint;
    }

    @Override
    public String toString(){
        return printBoard(this.board);
    }




}
