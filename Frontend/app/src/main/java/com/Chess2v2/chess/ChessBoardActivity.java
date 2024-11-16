package com.Chess2v2.chess;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Chess2v2.ChessApplication;
import com.Chess2v2.app.R;
import com.Chess2v2.app.WebSocketChessListener;
import com.Chess2v2.app.WebSocketManager;
import com.google.gson.JsonObject;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity representing the chess board screen where players can make moves,
 * view turn information, and interact with a WebSocket server.
 */
public class ChessBoardActivity extends AppCompatActivity {

    /** Manages WebSocket connections for sending and receiving game moves. */
    private WebSocketManager webSocketManager;

    /** RecyclerView for displaying the chess board and pieces. */
    private RecyclerView chessBoardRecyclerView;

    /** Adapter for managing chess pieces and board layout. */
    private ChessBoardAdapter chessBoardAdapter;

    /** TextView to display the current player's turn. */
    private TextView playerTurnTextView;

    /** Tracks the current turn (0 for white, 1 for black). */
    private int currentTurn = 0;

    /** Indicates if it is the current player's turn. */
    private boolean isMyTurn = true;

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

        playerTurnTextView = findViewById(R.id.playerTurnTextView);
        chessBoardRecyclerView = findViewById(R.id.chessBoardRecyclerView);
        chessBoardRecyclerView.setLayoutManager(new GridLayoutManager(this, 8));

        chessBoardAdapter = new ChessBoardAdapter(this::onPieceSelected);
        chessBoardRecyclerView.setAdapter(chessBoardAdapter);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> sendStartMessage());


        playerTurnTextView.setText("White's Turn");

        WebSocketChessListener listener = new WebSocketChessListener(this);
        webSocketManager = new WebSocketManager(listener);

        // Construct and connect to WebSocket server
        String webSocketUrl = "ws://10.90.73.46:8080/game/lofe";
        webSocketManager.connect(webSocketUrl);
    }


    /**
     * Sends a start message to the server to indicate the start of the game.
     */
    private void sendStartMessage() {
        if (webSocketManager != null) {
            JsonObject startMessage = new JsonObject();
            startMessage.addProperty("type", "start");
            Log.d("ChessBoardActivity", "Sending start message: " + startMessage.toString());
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
        currentTurn = (currentTurn + 1) % 2;
        playerTurnTextView.setText(currentTurn == 0 ? "White's Turn" : "Black's Turn");
        isMyTurn = !isMyTurn;
    }

    /**
     * Reverts an invalid move and displays a message to the user.
     */
    public void undoInvalidMove() {
        chessBoardAdapter.undoInvalidMove();
        Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
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
        Log.d("ChessBoardActivity", "Sending move: " + obj.toString());
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
        if (chessBoardAdapter.isValidMove(fromX, fromY, toX, toY)
                && (currentTurn % 2 == 0 && piece.isWhitePiece()
                || currentTurn % 2 != 0 && !piece.isWhitePiece())) {
            chessBoardAdapter.movePiece(fromX, fromY, toX, toY);
            currentTurn = (currentTurn + 1) % 2;
            playerTurnTextView.setText(currentTurn == 0 ? "White's Turn" : "Black's Turn");
            notifyMove(fromX, fromY, toX, toY);
        } else {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
        }
    }
}
