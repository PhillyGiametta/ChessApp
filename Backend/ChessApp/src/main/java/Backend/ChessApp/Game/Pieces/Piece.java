package Backend.ChessApp.Game.Pieces;

import Backend.ChessApp.Game.Board.BoardSquare;
import Backend.ChessApp.Game.Board.Position;
import Backend.ChessApp.Game.ChessGame;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "piece_type", discriminatorType = DiscriminatorType.STRING)
//@MappedSuperclass
public abstract class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "piece_id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    protected PieceColor color;

    @ManyToOne
    @JoinColumn(name = "board_square_id")
    private BoardSquare boardSquare;


    @ManyToOne
    @JoinColumn(name = "chess_game_id")
    private ChessGame chessGame;

    @Embedded
    protected Position position;

    public Piece(){

    }

    public Piece(PieceColor color, Position position) {
        this.color = color;
        this.position = position;
    }

    public PieceColor getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public BoardSquare getBoardSquare(int row, int col, List<BoardSquare> boardSquares){

        boardSquare = boardSquares.get(row * 8 + col);
        if(boardSquare == null)
            return null;
        return boardSquare;

    }

    public abstract boolean isValidMove(Position newPosition, List<BoardSquare> boardSquares);

    @Override
    public String toString() {
        if (this instanceof King) {
            return "K";
        } else if (this instanceof Queen) {
            return "Q";
        } else if (this instanceof Rook) {
            return "R";
        } else if (this instanceof Bishop) {
            return "B";
        } else if (this instanceof Knight) {
            return "N";
        } else if (this instanceof Pawn) {
            return "P";
        } else {
            return "?"; // Fallback for unknown pieces
        }
    }
}
