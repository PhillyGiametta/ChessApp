package Backend.ChessApp.Game.Board;

import Backend.ChessApp.Game.ChessGame;
import jakarta.persistence.*;

@Entity
@Table(name = "board_snapshot", schema = "DBChessApp")
public class BoardSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snapshot_id")
    private int id;

    //Many board snapshots correspond to one chess game
    @ManyToOne
    @JoinColumn(name = "chess_game_id")
    private ChessGame chessGame;

    //Many snapshots correspond to one board
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "move_number")
    private int moveNumber;

    public BoardSnapshot() {

    }

    public BoardSnapshot(Board board, ChessGame chessGame, int moveNumber) {
        this.board = board;
        this.chessGame = chessGame;
        this.moveNumber = moveNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

}
