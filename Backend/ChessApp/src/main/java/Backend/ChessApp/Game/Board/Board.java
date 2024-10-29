package Backend.ChessApp.Game.Board;

import Backend.ChessApp.Game.Pieces.*;

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
                board[i][j] = new BoardTile(new EmptySpace(PieceType.OPEN, -1), col,row);
                if(row == 7 || row == 2)
                    if(row == 2)
                        board[i][j].setTile(new Pawn(PieceType.PAWN, 0));
                    else
                        board[i][j].setTile(new Pawn(PieceType.PAWN, 1));
                if(row == 8 || row == 1){
                    if(col == 'A' || col == 'H')
                        if(row == 8)
                            board[i][j].setTile(new Rook(PieceType.ROOK, 1));
                        else
                            board[i][j].setTile(new Rook(PieceType.ROOK, 0));
                    else if(col == 'B' || col == 'G')
                        if(row == 8)
                            board[i][j].setTile(new Rook(PieceType.KNIGHT, 1));
                        else
                            board[i][j].setTile(new Rook(PieceType.KNIGHT, 0));
                    else if(col == 'C' || col == 'F')
                        if(row == 8)
                            board[i][j].setTile(new Rook(PieceType.BISHOP, 1));
                        else
                            board[i][j].setTile(new Rook(PieceType.BISHOP, 0));
                    else if(col == 'D')
                        if(row == 8)
                            board[i][j].setTile(new Rook(PieceType.KING, 1));
                        else
                            board[i][j].setTile(new Rook(PieceType.KING, 0));
                    else if(col == 'E')
                        if(row == 8)
                            board[i][j].setTile(new Rook(PieceType.QUEEN, 1));
                        else
                            board[i][j].setTile(new Rook(PieceType.QUEEN, 0));
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
