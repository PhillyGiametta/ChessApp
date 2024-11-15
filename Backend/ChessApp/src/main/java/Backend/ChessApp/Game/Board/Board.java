package Backend.ChessApp.Game.Board;

import Backend.ChessApp.Game.ChessGame;
import Backend.ChessApp.Game.Pieces.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "DBChessApp", name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_id")
    private int id;

    //One board has many board squares
    @OneToMany(mappedBy = "board")
    private List<BoardSquare> boardSquares;

    //One board corresponds to one chess game
    @OneToOne
    @JoinColumn(name = "chess_game_id")
    private ChessGame chessGame;

    public Board() {
        this.boardSquares = new ArrayList<>();
        setupBoardSquares();
        setupPieces();
    }

    private void setupBoardSquares(){
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                boardSquares.add(new BoardSquare(r, c, this));
            }
        }
    }

    private void setupPieces() {

        //Place Rooks
        BoardSquare square = getBoardSquare(0,0);
        square.setPiece(new Rook(PieceColor.BLACK, new Position(0, 0)));
        square = getBoardSquare(0,7);
        square.setPiece(new Rook(PieceColor.BLACK, new Position(0, 7)));
        square = getBoardSquare(7,0);
        square.setPiece(new Rook(PieceColor.WHITE, new Position(7, 0)));
        square = getBoardSquare(7,7);
        square.setPiece(new Rook(PieceColor.WHITE, new Position(7, 7)));

        //Place Knights
        square = getBoardSquare(0,1);
        square.setPiece(new Knight(PieceColor.BLACK, new Position(0, 1)));
        square = getBoardSquare(0,6);
        square.setPiece(new Knight(PieceColor.BLACK, new Position(0, 6)));
        square = getBoardSquare(7,1);
        square.setPiece(new Knight(PieceColor.WHITE, new Position(7, 1)));
        square = getBoardSquare(7,6);
        square.setPiece(new Knight(PieceColor.WHITE, new Position(7, 6)));

        //Place Bishops
        square = getBoardSquare(0,2);
        square.setPiece(new Bishop(PieceColor.BLACK, new Position(0, 2)));
        square = getBoardSquare(0,5);
        square.setPiece(new Bishop(PieceColor.BLACK, new Position(0, 5)));
        square = getBoardSquare(7,2);
        square.setPiece(new Bishop(PieceColor.WHITE, new Position(7, 2)));
        square = getBoardSquare(7,5);
        square.setPiece(new Bishop(PieceColor.WHITE, new Position(7, 5)));

        //Place Queens
        square = getBoardSquare(0,3);
        square.setPiece(new Queen(PieceColor.BLACK, new Position(0, 3)));
        square = getBoardSquare(7,3);
        square.setPiece(new Queen(PieceColor.WHITE, new Position(7, 3)));

        //Place Kings
        square = getBoardSquare(0,4);
        square.setPiece(new King(PieceColor.BLACK, new Position(0, 4)));
        square = getBoardSquare(7,4);
        square.setPiece(new King(PieceColor.WHITE, new Position(7, 4)));

        // Place Pawns
        for (int i = 0; i < 8; i++) {
            square = getBoardSquare(1,i);
            square.setPiece(new Pawn(PieceColor.BLACK, new Position(1, i)));
            square = getBoardSquare(6,i);
            square.setPiece(new Pawn(PieceColor.WHITE, new Position(6, i)));
        }
    }

    public BoardSquare getBoardSquare(int row, int col) {
        for(BoardSquare s : boardSquares){
            if(s.getRow() == row && s.getColumn() == col){
                return s;
            }
        }
        return null;
    }

    public void movePiece(Position start, Position end) {
        BoardSquare startSquare = getBoardSquare(start.getRow(), start.getColumn());
        BoardSquare endSquare = getBoardSquare(end.getRow(), end.getColumn());

        Piece piece = startSquare.getPiece();

            piece.setPosition(end);
            endSquare.setPiece(piece);
            startSquare.setPiece(null);


    }

    public List<BoardSquare> getBoardSquares(){
        return boardSquares;
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();

        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                BoardSquare square = getBoardSquare(r,c);
                if(square.getPiece() == null){
                    boardString.append(". ");
                }else{
                    boardString.append(square.getPiece().toString() + " ");
                }
            }
            boardString.append("\n");
        }

        return boardString.toString();
    }
}
