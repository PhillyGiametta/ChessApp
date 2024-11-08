package com.example.chessapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

public class ChessBoardActivity extends AppCompatActivity {
    private static final String TAG = "ChessBoardActivity";
    String webSocketUrl =  "ws://coms-3090-050.class.las.iastate.edu:8080/game/et";

    private RecyclerView chessBoardRecyclerView;
    private ChessBoardAdapter chessBoardAdapter;
    private TextView playerTurnTextView;
    private int currentTurn = 0; // 0 for white, 1 for black
    private boolean isMyTurn = true; // Start with White's turn

    private WebSocketManager webSocketManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);

        playerTurnTextView = findViewById(R.id.playerTurnTextView);
        chessBoardRecyclerView = findViewById(R.id.chessBoardRecyclerView);
        chessBoardRecyclerView.setLayoutManager(new GridLayoutManager(this, 8));

        chessBoardAdapter = new ChessBoardAdapter(this::onPieceSelected);
        chessBoardRecyclerView.setAdapter(chessBoardAdapter);

        // Set up WebSocket for game communication
        WebSocketChessListener listener = new WebSocketChessListener(this);
        webSocketManager = new WebSocketManager(listener);

        Log.d(TAG, "Attempting to connect to WebSocket at: " + webSocketUrl);
        webSocketManager.connect(webSocketUrl);

        playerTurnTextView.setText("White's Turn");
    }

    private void onPieceSelected(int fromX, int fromY, int toX, int toY) {
        if (!isMyTurn) {
            Toast.makeText(this, "It's not your turn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if there is a piece in the selected cell
        ChessPiece piece = chessBoardAdapter.getPieceAtPosition(fromX, fromY);
        if (piece == null || (currentTurn == 0 && !piece.isWhitePiece()) || (currentTurn == 1 && piece.isWhitePiece())) {
            Toast.makeText(this, "Invalid piece selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a move message with "type" as chessMove
        JSONObject moveMessage = new JSONObject();
        try {
            moveMessage.put("type", "chessMove");
            moveMessage.put("colStart", fromY);
            moveMessage.put("rowStart", fromX);
            moveMessage.put("colEnd", toY);
            moveMessage.put("rowEnd", toX);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create move message!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send move to the backend
        Log.d(TAG, "Sending move: " + moveMessage.toString());
        webSocketManager.sendMessage(moveMessage.toString());
    }

    // Update board on receiving a move confirmation
    public void updateBoard(int fromX, int fromY, int toX, int toY) {
        Log.d(TAG, "Updating board with move: [" + fromX + ", " + fromY + "] to [" + toX + ", " + toY + "]");
        chessBoardAdapter.movePiece(fromX, fromY, toX, toY);

        // Switch turn after a successful move
        currentTurn = (currentTurn + 1) % 2;
        playerTurnTextView.setText(currentTurn == 0 ? "White's Turn" : "Black's Turn");
        isMyTurn = !isMyTurn;

        Log.d(TAG, "Turn switched. Current turn: " + (currentTurn == 0 ? "White" : "Black"));
    }

    // Display message when an invalid move is attempted
    public void displayInvalidMoveMessage() {
        Log.d(TAG, "Invalid move attempted.");
        Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
    }

    // Display connection status
    public void displayConnectionStatus(String status) {
        Log.d(TAG, "WebSocket connection status: " + status);
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketManager != null) {
            Log.d(TAG, "Closing WebSocket connection.");
            webSocketManager.close();
        }
    }
}
