package Backend.ChessApp.Game.Board;

import Backend.ChessApp.Game.Pieces.Piece;
import jakarta.persistence.*;

@Entity
@Table(schema = "DBChessApp", name = "board_square")
public class BoardSquare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "chess_board_id")
    private Board board;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "piece_id")
    private Piece piece;

    @Column(name = "square_row")
    private int row;

    @Column(name = "square_column")
    private int column;

    public BoardSquare() {

    }

    public BoardSquare(int row, int col, Board board){
        this.row = row;
        this.column = col;
        this.board = board;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public Piece getPiece(){
        return piece;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }
}
