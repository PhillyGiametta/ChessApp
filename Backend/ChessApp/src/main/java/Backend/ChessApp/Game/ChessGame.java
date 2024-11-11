package Backend.ChessApp.Game;

import Backend.ChessApp.AdminControl.Admin;
import Backend.ChessApp.Game.Board.ChessBoard;
import Backend.ChessApp.Game.Board.Position;
import Backend.ChessApp.Game.Pieces.King;
import Backend.ChessApp.Game.Pieces.PieceColor;
import Backend.ChessApp.Game.Pieces.Piece;
import Backend.ChessApp.Settings.GameSettingsService;
import Backend.ChessApp.Settings.SettingGameStates;
import Backend.ChessApp.Users.User;
import jakarta.persistence.*;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(schema = "DBChessApp", name = "chess_game")
public class ChessGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chess_game_id", nullable = false)
    private int id;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "chess_game_id")
    private Admin admin;

    @OneToMany(cascade = CascadeType.MERGE)
    private List<User> listOfUsers;

    @OneToOne(cascade = CascadeType.PERSIST)
    @PrimaryKeyJoinColumn
    private ChessBoard board;

    @OneToMany(mappedBy = "gameHistory", cascade = CascadeType.PERSIST)
    private List<ChessBoard> boardHistory = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    @PrimaryKeyJoinColumn
    private SettingGameStates settingGameStates;

    private boolean whiteTurn = true; // White starts the game
    private GameActive gameActive = GameActive.GAME_NOT_STARTED;

    @ManyToMany(cascade = CascadeType.MERGE)
    List<User> WhiteTeam = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.MERGE)
    List<User> BlackTeam = new ArrayList<>();


    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "chess_game_id")
    public Timer whiteTimer;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "chess_game_id")
    public Timer blackTimer;

    public ChessGame() {
        this.board = new ChessBoard();
    }

    public ChessBoard getBoard() {
        return this.board;
    }

    public void resetGame() {
        this.board = new ChessBoard();
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
        if (selectedPosition == null) {
            Piece selectedPiece = board.getPiece(row, col);
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
        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
            return false;
        }

        if (movingPiece.isValidMove(end, board.getBoard())) {
            board.movePiece(start, end);
            whiteTurn = !whiteTurn;
            recordMove();
            return true;
        }
        return false;
    }

    public void recordMove(){
        boardHistory.add(this.board);
    }



    private boolean isPositionOnBoard(Position position) {
        return position.getRow() >= 0 && position.getRow() < board.getBoard().length &&
                position.getColumn() >= 0 && position.getColumn() < board.getBoard()[0].length;
    }
    private boolean wouldBeInCheckAfterMove(PieceColor kingColor, Position from, Position to) {
        Piece temp = board.getPiece(to.getRow(), to.getColumn());
        board.setPiece(to.getRow(), to.getColumn(), board.getPiece(from.getRow(), from.getColumn()));
        board.setPiece(from.getRow(), from.getColumn(), null);

        boolean inCheck = isInCheck(kingColor);

        board.setPiece(from.getRow(), from.getColumn(), board.getPiece(to.getRow(), to.getColumn()));
        board.setPiece(to.getRow(), to.getColumn(), temp);

        return inCheck;
    }

    public boolean isInCheck(PieceColor kingColor) {
        Position kingPosition = findKingPosition(kingColor);
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[row].length; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() != kingColor) {
                    if (piece.isValidMove(kingPosition, board.getBoard())) {
                        return true; // An opposing piece can capture the king
                    }
                }
            }
        }
        return false;

    }
    private Position findKingPosition(PieceColor color) {
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[row].length; col++) {
                Piece piece = board.getPiece(row, col);
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
        King king = (King) board.getPiece(kingPosition.getRow(), kingPosition.getColumn());

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
                if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoard())
                        && !wouldBeInCheckAfterMove(kingColor, kingPosition, newPosition)) {
                    return false; // Found a move that gets the king out of check, so it's not checkmate
                }
            }
        }

        return true; // No legal moves available to escape check, so it's checkmate

    }

    public List<Position> getLegalMovesForPieceAt(Position position) {
        Piece selectedPiece = board.getPiece(position.getRow(), position.getColumn());
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
                if (board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
                    legalMoves.add(new Position(newPos.getRow(), newPos.getColumn()));
                    newPos = new Position(newPos.getRow() + d[0], newPos.getColumn() + d[1]);
                } else {
                    if (board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board
                            .getPiece(position.getRow(), position.getColumn()).getColor()) {
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
            if (isPositionOnBoard(newPos) && (board.getPiece(newPos.getRow(), newPos.getColumn()) == null ||
                    board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board
                            .getPiece(position.getRow(), position.getColumn()).getColor())) {
                legalMoves.add(newPos);
            }
        }
    }

    private void addPawnMoves(Position position, PieceColor color, List<Position> legalMoves) {
        int direction = color == PieceColor.WHITE ? -1 : 1;
        Position newPos = new Position(position.getRow() + direction, position.getColumn());
        if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
            legalMoves.add(newPos);
        }

        if ((color == PieceColor.WHITE && position.getRow() == 6)
                || (color == PieceColor.BLACK && position.getRow() == 1)) {
            newPos = new Position(position.getRow() + 2 * direction, position.getColumn());
            Position intermediatePos = new Position(position.getRow() + direction, position.getColumn());
            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null
                    && board.getPiece(intermediatePos.getRow(), intermediatePos.getColumn()) == null) {
                legalMoves.add(newPos);
            }
        }

        int[] captureCols = { position.getColumn() - 1, position.getColumn() + 1 };
        for (int col : captureCols) {
            newPos = new Position(position.getRow() + direction, col);
            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) != null &&
                    board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != color) {
                legalMoves.add(newPos);
            }
        }
    }

    public void setBlackTeam(List<User> blackTeam) {
        BlackTeam = blackTeam;
    }
    public void setWhiteTeam(List<User> whiteTeam){
        WhiteTeam = whiteTeam;
    }

    public List<User> getBlackTeam() {
        return BlackTeam;
    }

    public List<User> getWhiteTeam() {
        return WhiteTeam;
    }

    public GameActive getGameActive() {
        return gameActive;
    }

    public void setGameActive(GameActive gameActive) {
        this.gameActive = gameActive;
    }
}