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
    boolean flipped = false;


    /**
     * Creates a new fresh board. With initialized pieces.
     */
    public Board() {
        BoardBuilder();
    }
    public Board(Board flipper){
        this.board = flipBoard(flipper.getBoard());
        flipped = true;
    }

    /**
     * Builds a chess board for pieces to move on, 2d array of BoardTiles
     * null BoardTile means edge, otherwise an available move, as specified per piece
     */
    private void BoardBuilder() {
        int row = 7;
        BoardTile[][] board = new BoardTile[8][8];
        for (int i = 0; i < 8; i++, row--) {
            char col = 0;
            for (int j = 0; j < 8; j++, col++) {
                board[i][j] = new BoardTile(new EmptySpace(PieceType.OPEN, -1), j, i, this);

                if (row == 6 || row == 1) {
                    if (row == 1) {
                        board[i][j].setTile(new Pawn(PieceType.PAWN, 0));
                        board[i][j].getTile().setBoardTile(board[i][j]);
                    } else {
                        board[i][j].setTile(new Pawn(PieceType.PAWN, 1));
                        board[i][j].getTile().setBoardTile(board[i][j]);
                    }
                }
                if (row == 7 || row == 0) {
                    if (col == 0 || col == 7) {
                        if (row == 7) {
                            board[i][j].setTile(new Rook(PieceType.ROOK, 1));
                            board[i][j].getTile().setBoardTile(board[i][j]);
                        } else {
                            board[i][j].setTile(new Rook(PieceType.ROOK, 0));
                            board[i][j].getTile().setBoardTile(board[i][j]);
                        }
                    } else if (col == 1 || col == 6) {
                        if (row == 7) {
                            board[i][j].setTile(new Knight(PieceType.KNIGHT, 1));
                            board[i][j].getTile().setBoardTile(board[i][j]);
                        } else {
                            board[i][j].setTile(new Knight(PieceType.KNIGHT, 0));
                            board[i][j].getTile().setBoardTile(board[i][j]);
                        }
                    } else if (col == 2 || col == 5) {
                        if (row == 7) {
                            board[i][j].setTile(new Bishop(PieceType.BISHOP, 1));
                            board[i][j].getTile().setBoardTile(board[i][j]);
                        } else {
                            board[i][j].setTile(new Bishop(PieceType.BISHOP, 0));
                            board[i][j].getTile().setBoardTile(board[i][j]);
                        }
                    } else if (col == 3) {
                        if (row == 7) {
                            board[i][j].setTile(new King(PieceType.KING, 1));
                            board[i][j].getTile().setBoardTile(board[i][j]);
                        } else {
                            board[i][j].setTile(new King(PieceType.KING, 0));
                            board[i][j].getTile().setBoardTile(board[i][j]);
                        }
                    } else if (col == 4) {
                        if (row == 7) {
                            board[i][j].setTile(new Queen(PieceType.QUEEN, 1));
                            board[i][j].getTile().setBoardTile(board[i][j]);
                        } else {
                            board[i][j].setTile(new Queen(PieceType.QUEEN, 0));
                            board[i][j].getTile().setBoardTile(board[i][j]);
                        }
                    }
                }
            }

        }
        this.board = board;
    }

    public static BoardTile[][] flipBoard(BoardTile[][] board) {
        BoardTile[][] flippedBoard = new BoardTile[8][8];


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                flippedBoard[i][j] = board[7 - i][7 - j]; // Flip row and column
            }
        }

        return flippedBoard;
    }


    private String printBoard(BoardTile[][] board) {
        selectedRow = 8;
        String consolePrint = "";
        for (BoardTile[] boardTiles : board) {
            selectedColumn = 'A';
            for (BoardTile boardTile : boardTiles) {

                consolePrint += (boardTile + "\t");
            }
            consolePrint += ("\t" + selectedRow--);
            consolePrint += "\n"; // New line at the end of each row
        }
        for(char i = 'A'; i <= 'H'; i++){
            consolePrint += (i + "\t");
        }

        return consolePrint;
    }

    private String printFlippedBoard(BoardTile[][] board) {
        selectedRow = 1;
        String consolePrint = "";
        for (BoardTile[] boardTiles : board) {
            for (BoardTile boardTile : boardTiles) {

                consolePrint += (boardTile + "\t");
            }
            consolePrint += ("\t" + selectedRow++);
            consolePrint += "\n"; // New line at the end of each row
        }
        for(selectedColumn = 'H'; selectedColumn >= 'A'; selectedColumn--){
            consolePrint += (selectedColumn + "\t");
        }

        return consolePrint;
    }

    public BoardTile getTile(int row, int col) {
        return board[row][col];
    }

    @Override
    public String toString() {
        if(flipped)
            return printFlippedBoard(this.board);
        return printBoard(this.board);
    }

    public BoardTile[][] getBoard() {
        return board;
    }
}

