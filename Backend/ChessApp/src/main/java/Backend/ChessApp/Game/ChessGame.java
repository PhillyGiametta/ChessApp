package Backend.ChessApp.Game;

import Backend.ChessApp.AdminControl.Admin;
import Backend.ChessApp.Game.Board.*;
import Backend.ChessApp.Game.Pieces.King;
import Backend.ChessApp.Game.Pieces.PieceColor;
import Backend.ChessApp.Game.Pieces.Piece;
import Backend.ChessApp.Group.Group;
import Backend.ChessApp.Settings.SettingGameStates;
import Backend.ChessApp.Users.User;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

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

//    //One game has many board snapshots
    @OneToMany(mappedBy = "chessGame", cascade = CascadeType.ALL)
    private List<BoardSnapshot> boardHistory = new ArrayList<>();

    //One game has one game setting states
    @OneToOne
    @JoinColumn(name = "game_settings_id")
    private SettingGameStates settingGameStates;

    @ManyToMany
    @JoinTable(
            name = "white_team",
            joinColumns = @JoinColumn(name = "chess_game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> whiteTeam = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "black_team",
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

    private boolean whiteTurn = true; // White starts the game

    private GameActive gameActive = GameActive.GAME_NOT_STARTED;

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
        moveNumber++;
        BoardSnapshot history = new BoardSnapshot(this.board, this, moveNumber);
        boardHistory.add(history);
    }

    public void undo(){
        //idea: undo moves back to undoers previous turn;
        //black does undo, still blacks turn from 2 ago, can only undo on your turn to avoid odd logic;
        moveNumber -=2;
        boardHistory.remove(boardHistory.get(boardHistory.size() -1));
        boardHistory.remove(boardHistory.get(boardHistory.size() -1)); //twice to remove two turns
        board = boardHistory.get(boardHistory.size()-1).getBoard(); //set playing board
        //nothing should need changed since we are just clean slating the turn.


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

    public int getMoveNumber(){
        return moveNumber;
    }

    public List<BoardSnapshot> getBoardHistory(){
        return boardHistory;
    }

    /**
     * This is a main class to ensure that the game is working as intended before going to the server.
     * Will be deleted before production
     * @param args
     */
    public static void main(String[] args){
        ChessGame chessGame = new ChessGame();
        System.out.println(chessGame.getBoard().toString());

        // Setting the game to active
        chessGame.setGameActive(GameActive.GAME_ACTIVE);

        // 1. White pawn moves e2 to e4 (Position (6, 4) to (4, 4))
        Position whitePawnStart = new Position(6, 4);
        Position whitePawnEnd = new Position(5, 4);
        String result = chessGame.makeMove(whitePawnStart, whitePawnEnd) ? "yes" : "no";
        System.out.println(result);
        System.out.println(chessGame.getBoard().toString());

        // 2. Black pawn moves e7 to e5 (Position (1, 4) to (3, 4))
        Position blackPawnStart = new Position(1, 4);
        Position blackPawnEnd = new Position(3, 4);
        result = chessGame.makeMove(blackPawnStart, blackPawnEnd) ? "yes" : "no";
        System.out.println(result);
        System.out.println(chessGame.getBoard().toString());

        // 3. White knight moves g1 to f3 (Position (7, 6) to (5, 5))
        Position whiteKnightStart = new Position(7, 6);
        Position whiteKnightEnd = new Position(5, 5);
        result = chessGame.makeMove(whiteKnightStart, whiteKnightEnd) ? "yes" : "no";
        System.out.println(result);
        System.out.println(chessGame.getBoard().toString());

        // 4. Black knight moves b8 to c6 (Position (0, 1) to (2, 2))
        Position blackKnightStart = new Position(0, 1);
        Position blackKnightEnd = new Position(2, 2);
        result = chessGame.makeMove(blackKnightStart, blackKnightEnd) ? "yes" : "no";
        System.out.println(result);
        System.out.println(chessGame.getBoard().toString());

        // 5. White bishop moves f1 to c4 (Position (7, 5) to (4, 2))
        Position whiteBishopStart = new Position(7, 5);
        Position whiteBishopEnd = new Position(4, 2);
        result = chessGame.makeMove(whiteBishopStart, whiteBishopEnd) ? "yes" : "no";
        System.out.println(result);
        System.out.println(chessGame.getBoard().toString());

        // 6. Black queen moves d8 to h4 (Position (0, 3) to (4, 7))
        Position blackQueenStart = new Position(0, 3);
        Position blackQueenEnd = new Position(4, 7);
        result = chessGame.makeMove(blackQueenStart, blackQueenEnd) ? "yes" : "no";
        System.out.println(result);
        System.out.println(chessGame.getBoard().toString());

        // 7. White rook moves a1 to a3 (Position (7, 0) to (5, 0))
        Position whiteRookStart = new Position(7, 7);
        Position whiteRookEnd = new Position(7, 5);
        result = chessGame.makeMove(whiteRookStart, whiteRookEnd) ? "yes" : "no";
        System.out.println(result);
        System.out.println(chessGame.getBoard().toString());

        // 8. Black king moves e8 to e7 (Position (0, 4) to (1, 4))
        Position blackKingStart = new Position(0, 4);
        Position blackKingEnd = new Position(1, 4);
        result = chessGame.makeMove(blackKingStart, blackKingEnd) ? "yes" : "no";
        System.out.println(result);
        System.out.println(chessGame.getBoard().toString());

        // 9. White queen moves d1 to h5 (Position (7, 3) to (3, 7))
        Position whiteQueenStart = new Position(7, 3);
        Position whiteQueenEnd = new Position(6, 4);
        result = chessGame.makeMove(whiteQueenStart, whiteQueenEnd) ? "yes" : "no";
        System.out.println(result);
        System.out.println(chessGame.getBoard().toString());


        Position blackKnightStart2 = new Position(2, 2);
        Position blackKnightEnd2 = new Position(4, 3);
        result = chessGame.makeMove(blackKnightStart2, blackKnightEnd2) ? "yes" : "no";
        System.out.println(result);
        System.out.println(chessGame.getBoard().toString());
    }
}