package Backend.ChessApp.Game;

import Backend.ChessApp.AdminControl.Admin;
import Backend.ChessApp.Game.Board.Board;
import Backend.ChessApp.Game.Board.BoardSnapshot;
import Backend.ChessApp.Game.Board.BoardSquare;
import Backend.ChessApp.Game.Board.Position;
import Backend.ChessApp.Game.Pieces.King;
import Backend.ChessApp.Game.Pieces.PieceColor;
import Backend.ChessApp.Game.Pieces.Piece;
import Backend.ChessApp.Group.Group;
import Backend.ChessApp.Settings.SettingGameStates;
import Backend.ChessApp.Users.User;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "chess_game", schema = "DBChessApp")
public class ChessGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chess_game_id", nullable = false, unique = true)
    private int id;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    //Each group corresponds to one game
    @OneToOne
    @JoinColumn(name = "group_id")
    private Group group;

    //One game has one board
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "board_id")
    private Board board;

    //One game has many board snapshots
    @OneToMany(mappedBy = "chessGame", cascade = CascadeType.ALL)
    private List<BoardSnapshot> boardHistory = new ArrayList<>();

    //One game has one game setting states
    @OneToOne
    @JoinColumn(name = "game_settings_id")
    private SettingGameStates settingGameStates;

    private boolean whiteTurn = true; // White starts the game
    private GameActive gameActive = GameActive.GAME_NOT_STARTED;

    @ManyToMany
    @JoinTable(
            name = "chess_game_white_team",
            joinColumns = @JoinColumn(name = "chess_game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> whiteTeam = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "chess_game_black_team",
            joinColumns = @JoinColumn(name = "chess_game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> blackTeam = new ArrayList<>();


    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "white_timer_id")
    public Timer whiteTimer;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "black_timer_id")
    public Timer blackTimer;

    //Tracks number of moves for the board history
    int moveNumber = 0;


    public ChessGame() {
        this.board = new Board();
    }

    public void resetGame() {
        this.board = new Board();
        this.whiteTurn = true;
    }

    public boolean getCurrentPlayerColor() {
        return whiteTurn;
    }

    @Transient
    private Position selectedPosition;

    public enum GameActive{
        GAME_ACTIVE, GAME_ENDED, GAME_STALEMATE, GAME_NOT_STARTED
    }

    public boolean isPieceSelected() {
        return selectedPosition != null;
    }

    public boolean handleSquareSelection(int row, int col) {
        List<BoardSquare> boardSquares = board.getBoardSquares();

        if (selectedPosition == null) {
            Piece selectedPiece = board.getBoardSquare(row, col).getPiece();
            if (selectedPiece != null
                    && selectedPiece.getColor() == (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
                selectedPosition = new Position(row, col);
                return false;
            }
        } else {
            boolean moveMade = makeMove(selectedPosition, new Position(row, col));
            selectedPosition = null;
            return moveMade;
        }
        return false;
    }
    public boolean makeMove(Position start, Position end) {
        if(gameActive != GameActive.GAME_ACTIVE)
            return false;
        Piece movingPiece = board.getBoardSquare(start.getRow(), start.getColumn()).getPiece();
        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
            return false;
        }

        if (movingPiece.isValidMove(end, board.getBoardSquares())) {
            board.movePiece(start, end);
            whiteTurn = !whiteTurn;
            recordMove();
            return true;
        }
        return false;
    }

    public void recordMove(){
        BoardSnapshot history = new BoardSnapshot(this.board, this, moveNumber);
        boardHistory.add(history);
        moveNumber++;
    }



    private boolean isPositionOnBoard(Position position) {
        return position.getRow() >= 0 && position.getRow() < 8 && position.getColumn() >= 0 && position.getColumn() < 8;
    }
    private boolean wouldBeInCheckAfterMove(PieceColor kingColor, Position from, Position to) {
        makeMove(from, to);

        boolean inCheck = isInCheck(kingColor);

        makeMove(to, from);

        return inCheck;
    }

    public boolean isInCheck(PieceColor kingColor) {
        Position kingPosition = findKingPosition(kingColor);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getBoardSquare(row, col).getPiece();
                if (piece != null && piece.getColor() != kingColor) {
                    if (piece.isValidMove(kingPosition, board.getBoardSquares())) {
                        return true; // An opposing piece can capture the king
                    }
                }
            }
        }
        return false;

    }
    private Position findKingPosition(PieceColor color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getBoardSquare(row, col).getPiece();
                if (piece instanceof King && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        throw new RuntimeException("King not found, which should never happen.");

    }
    public boolean isCheckmate(PieceColor kingColor) {
        if (!isInCheck(kingColor)) {
            return false; // Not in check, so cannot be checkmate
        }

        Position kingPosition = findKingPosition(kingColor);
        King king = (King) board.getBoardSquare(kingPosition.getRow(), kingPosition.getColumn()).getPiece();

        // Attempt to find a move that gets the king out of check
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue; // Skip the current position of the king
                }
                Position newPosition = new Position(kingPosition.getRow() + rowOffset,
                        kingPosition.getColumn() + colOffset);
                // Check if moving the king to the new position is a legal move and does not
                // result in a check
                if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoardSquares())
                        && !wouldBeInCheckAfterMove(kingColor, kingPosition, newPosition)) {
                    return false; // Found a move that gets the king out of check, so it's not checkmate
                }
            }
        }

        return true; // No legal moves available to escape check, so it's checkmate

    }

    public List<Position> getLegalMovesForPieceAt(Position position) {
        Piece selectedPiece = board.getBoardSquare(position.getRow(), position.getColumn()).getPiece();
        if (selectedPiece == null)
            return new ArrayList<>();

        List<Position> legalMoves = new ArrayList<>();
        switch (selectedPiece.getClass().getSimpleName()) {
            case "Pawn":
                addPawnMoves(position, selectedPiece.getColor(), legalMoves);
                break;
            case "Rook":
                addLineMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }, legalMoves);
                break;
            case "Knight":
                addSingleMoves(position, new int[][] { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { -1, 2 },
                        { 1, -2 }, { -1, -2 } }, legalMoves);
                break;
            case "Bishop":
                addLineMoves(position, new int[][] { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } }, legalMoves);
                break;
            case "Queen":
                addLineMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
                        { 1, -1 }, { -1, 1 } }, legalMoves);
                break;
            case "King":
                addSingleMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
                        { 1, -1 }, { -1, 1 } }, legalMoves);
                break;
        }
        return legalMoves;
    }

    private void addLineMoves(Position position, int[][] directions, List<Position> legalMoves) {
        for (int[] d : directions) {
            Position newPos = new Position(position.getRow() + d[0], position.getColumn() + d[1]);
            while (isPositionOnBoard(newPos)) {
                if (board.getBoardSquare(newPos.getRow(), newPos.getColumn()).getPiece() == null) {
                    legalMoves.add(new Position(newPos.getRow(), newPos.getColumn()));
                    newPos = new Position(newPos.getRow() + d[0], newPos.getColumn() + d[1]);
                } else {
                    if (board.getBoardSquare(newPos.getRow(), newPos.getColumn()).getPiece().getColor() !=
                            board.getBoardSquare(position.getRow(), position.getColumn()).getPiece().getColor()) {
                        legalMoves.add(newPos);
                    }
                    break;
                }
            }
        }
    }
    private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves) {
        for (int[] move : moves) {
            Position newPos = new Position(position.getRow() + move[0], position.getColumn() + move[1]);
            BoardSquare newSquare = board.getBoardSquare(newPos.getRow(), newPos.getColumn());
            if (isPositionOnBoard(newPos) && (newSquare.getPiece() == null ||
                    newSquare.getPiece().getColor() != board.getBoardSquare(position.getRow(), position.getColumn()).getPiece().getColor())) {
                legalMoves.add(newPos);
            }
        }
    }

    private void addPawnMoves(Position position, PieceColor color, List<Position> legalMoves) {
        int direction = color == PieceColor.WHITE ? -1 : 1;
        Position newPos = new Position(position.getRow() + direction, position.getColumn());
        if (isPositionOnBoard(newPos) && board.getBoardSquare(newPos.getRow(), newPos.getColumn()).getPiece() == null) {
            legalMoves.add(newPos);
        }

        if ((color == PieceColor.WHITE && position.getRow() == 6)
                || (color == PieceColor.BLACK && position.getRow() == 1)) {

            newPos = new Position(position.getRow() + 2 * direction, position.getColumn());
            Position intermediatePos = new Position(position.getRow() + direction, position.getColumn());

            if (isPositionOnBoard(newPos) && board.getBoardSquare(newPos.getRow(), newPos.getColumn()).getPiece() == null
                    && board.getBoardSquare(intermediatePos.getRow(), intermediatePos.getColumn()).getPiece() == null) {
                legalMoves.add(newPos);
            }
        }

        int[] captureCols = { position.getColumn() - 1, position.getColumn() + 1 };
        for (int col : captureCols) {

            newPos = new Position(position.getRow() + direction, col);
            BoardSquare newSquare = board.getBoardSquare(newPos.getRow(), newPos.getColumn());

            if (isPositionOnBoard(newPos) && newSquare.getPiece() != null && newSquare.getPiece().getColor() != color) {
                legalMoves.add(newPos);
            }
        }
    }

    public void setBlackTeam(List<User> blackTeam) {
        this.blackTeam = blackTeam;
    }
    public void setWhiteTeam(List<User> whiteTeam){
        this.whiteTeam = whiteTeam;
    }

    public List<User> getBlackTeam() {
        return blackTeam;
    }

    public List<User> getWhiteTeam() {
        return whiteTeam;
    }

    public GameActive getGameActive() {
        return gameActive;
    }

    public void setGameActive(GameActive gameActive) {
        this.gameActive = gameActive;
    }

    public int getId() {
        return id;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public Board getBoard(){
        return this.board;
    }



}