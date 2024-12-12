package com.Chess2v2.chess;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Chess2v2.ChessApplication;
import com.Chess2v2.app.R;
import com.Chess2v2.app.UserData;
import com.Chess2v2.app.WebSocketChessListener;
import com.Chess2v2.app.WebSocketManager;
import com.google.gson.JsonObject;

/**
 * Activity representing the chess board screen where players can make moves,
 * view turn information, and interact with a WebSocket server.
 */
public class ChessBoardActivity extends AppCompatActivity {

    PlayerInfoView whiteInfoView;
    PlayerInfoView blackInfoView;
    UserData whitePlayer, blackPlayer;
    /**
     * Manages WebSocket connections for sending and receiving game moves.
     */
    private WebSocketManager webSocketManager;
    /**
     * Adapter for managing chess pieces and board layout.
     */
    private ChessBoardAdapter chessBoardAdapter;
    /**
     * TextView to display the current player's turn.
     */
    private TextView playerTurnTextView;
    private boolean isBoardInverted;
    private boolean isCurrentUserWhite;

    public PlayerInfoView getWhiteInfoView() {
        return whiteInfoView;
    }

    public void setWhiteInfoView(PlayerInfoView whiteInfoView) {
        this.whiteInfoView = whiteInfoView;
    }

    public PlayerInfoView getBlackInfoView() {
        return blackInfoView;
    }

    public void setBlackInfoView(PlayerInfoView blackInfoView) {
        this.blackInfoView = blackInfoView;
    }

    public UserData getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(UserData whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public UserData getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(UserData blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    /**
     * Called when the activity is created. Initializes the board, WebSocket connection,
     * and view components.
     *
     * @param savedInstanceState Bundle containing the saved state of the activity.
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);


        this.isBoardInverted = true;
        isCurrentUserWhite = true;


        ChessApplication app = ChessApplication.getInstance();
        UserData currentPlayer = new UserData(app.getEmail(), app.getUserName(), null, app.getUserId() + "");
        UserData opponent = new UserData(app.getEmail(), "Opponent", null, app.getUserId() + "");

        whiteInfoView = findViewById(isBoardInverted ? R.id.player2 : R.id.player1);
        blackInfoView = findViewById(isBoardInverted ? R.id.player1 : R.id.player2);

        if (isCurrentUserWhite) {
            whitePlayer = currentPlayer;
            blackPlayer = opponent;
        } else {
            blackPlayer = currentPlayer;
            whitePlayer = opponent;
        }
        whitePlayer.setClock(
                new Clock(
                        new Handler(msg -> {
                            String time = (String) msg.obj;
                            if (time.equals("00:00") && chessBoardAdapter.isGameStarted()) {
                                getOver("Timeout: Black wins");
                            }
                            whiteInfoView.setClockTime(time);
                            return true;
                        })
                )
        );
        blackPlayer.setClock(
                new Clock(
                        new Handler(msg -> {
                            String time = (String) msg.obj;
                            blackInfoView.setClockTime(time);
                            if (time.equals("00:00") && chessBoardAdapter.isGameStarted()) {
                                getOver("Timeout: White wins");
                            }
                            return true;
                        })
                )
        );
        whitePlayer.getClock().setTime(10, 0);
        blackPlayer.getClock().setTime(10, 0);
        whiteInfoView.setPlayerName(whitePlayer.getUserName());
        blackInfoView.setPlayerName(blackPlayer.getUserName());

        playerTurnTextView = findViewById(R.id.playerTurnTextView);
        /**
         * RecyclerView for displaying the chess board and pieces.
         */
        RecyclerView chessBoardRecyclerView = findViewById(R.id.chessBoardRecyclerView);
        chessBoardRecyclerView.setLayoutManager(new GridLayoutManager(this, 8));
        chessBoardAdapter = new ChessBoardAdapter(this::onPieceSelected,
                true, currentPlayer, isBoardInverted, (board, from, to) -> this.moveCompleted());
        chessBoardRecyclerView.setAdapter(chessBoardAdapter);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            chessBoardAdapter.startGame();

            Clock g = whitePlayer.getClock();
            g.setTime(10, 0);
            g.start();
            whiteInfoView.setClockActive(true);
            blackInfoView.setClockActive(false);

            g = blackPlayer.getClock();
            g.pause();
            g.setTime(10, 0);
            sendStartMessage();
            playerTurnTextView.setText("White's Turn");

        });


        playerTurnTextView.setText("Click start");

        WebSocketChessListener listener = new WebSocketChessListener(this);
        webSocketManager = new WebSocketManager(listener);

        // Construct and connect to WebSocket server
        String webSocketUrl = ChessApplication.getInstance().getWebSocketBaseUrl() + "game/" + currentPlayer.getUserName();
        webSocketManager.connect(webSocketUrl);


    }


    /**
     * Sends a start message to the server to indicate the start of the game.
     */
    private void sendStartMessage() {
        if (webSocketManager != null) {
            JsonObject startMessage = new JsonObject();
            startMessage.addProperty("type", "start");
            Log.d("ChessBoardActivity", "Sending start message: " + startMessage);
            webSocketManager.sendMessage(startMessage.toString());
        }
    }

    /**
     * Updates the board by moving a piece from one position to another and switches the turn.
     *
     * @param fromX X-coordinate of the starting position.
     * @param fromY Y-coordinate of the starting position.
     * @param toX   X-coordinate of the destination position.
     * @param toY   Y-coordinate of the destination position.
     */
    public void updateBoard(int fromX, int fromY, int toX, int toY) {
        chessBoardAdapter.movePiece(fromX, fromY, toX, toY);
    }

    /**
     * Reverts an invalid move and displays a message to the user.
     */
    public void undoInvalidMove() {
        chessBoardAdapter.undoInvalidMove();
        Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
    }

    private void moveCompleted() {
        playerTurnTextView.setText(chessBoardAdapter.getWhiteTurn() ? "White's Turn" : "Black's Turn");
        ChessBoard chessBoard = chessBoardAdapter.getBoard();


        if (chessBoardAdapter.getWhiteTurn()) {
            whitePlayer.getClock().start();
            blackPlayer.getClock().pause();
            whiteInfoView.setClockActive(true);
            blackInfoView.setClockActive(false);
        } else {
            blackPlayer.getClock().start();
            whitePlayer.getClock().pause();
            blackInfoView.setClockActive(true);
            whiteInfoView.setClockActive(false);
        }
        if ((chessBoard.isBlackKingChecked() || chessBoard.isWhiteKingChecked()) && chessBoard.getOutOffCheckMoves().isEmpty()) {
            getOver("CheckMate: " + (chessBoardAdapter.getWhiteTurn() ? "Black wins" : "White wins"));
        }
        BoardAnalyzer.Result analyzer = BoardAnalyzer.calculatePieceDifferences(chessBoardAdapter.getBoard());
        blackInfoView.setPieceDifference(analyzer.black);
        whiteInfoView.setPieceDifference(analyzer.white);

    }

    /**
     * Displays the connection status of the WebSocket to the user.
     *
     * @param status The connection status message.
     */
    public void displayConnectionStatus(String status) {
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a general message to the user.
     *
     * @param text The message to be displayed.
     */
    public void displayMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Notifies the server of a move made by sending a JSON object via WebSocket.
     *
     * @param fromX X-coordinate of the starting position.
     * @param fromY Y-coordinate of the starting position.
     * @param toX   X-coordinate of the destination position.
     * @param toY   Y-coordinate of the destination position.
     */
    protected void notifyMove(int fromX, int fromY, int toX, int toY) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "chessMove");
        obj.addProperty("rowStart", fromY);
        obj.addProperty("colStart", fromX);
        obj.addProperty("rowEnd", toY);
        obj.addProperty("colEnd", toX);
        Log.d("ChessBoardActivity", "Sending move: " + obj);
        webSocketManager.sendMessage(obj.toString());
    }

    /**
     * Handles the selection of a piece and attempts to make a move if it is valid.
     *
     * @param piece The chess piece being moved.
     * @param fromX X-coordinate of the starting position.
     * @param fromY Y-coordinate of the starting position.
     * @param toX   X-coordinate of the destination position.
     * @param toY   Y-coordinate of the destination position.
     */
    private void onPieceSelected(ChessPiece piece, int fromX, int fromY, int toX, int toY) {

        // Check if the selected move is valid
        if (chessBoardAdapter.isValidMove(fromX, fromY, toX, toY)
                && (chessBoardAdapter.getWhiteTurn() && piece.isWhitePiece()
                || !chessBoardAdapter.getWhiteTurn() && !piece.isWhitePiece())) {

            chessBoardAdapter.movePiece(fromX, fromY, toX, toY);

            notifyMove(fromX, fromY, toX, toY);
        } else {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getOver(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        playerTurnTextView.setText(message);
        chessBoardAdapter.gameOver();
    }
}
