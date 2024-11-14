package com.Chess2v2.chess;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Chess2v2.app.R;
import com.Chess2v2.app.WebSocketChessListener;
import com.Chess2v2.app.WebSocketManager;
import com.Chess2v2.chess.ChessBoardAdapter;
import com.google.gson.JsonObject;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class ChessBoardActivity extends AppCompatActivity {
    private WebSocketManager webSocketManager;
    private RecyclerView chessBoardRecyclerView;
    private ChessBoardAdapter chessBoardAdapter;
    private TextView playerTurnTextView;
    private int currentTurn = 0; // 0 for white, 1 for black
    private boolean isMyTurn = true; // Start with White's turn
    private static final String GROUP_NAME = "test";  // Hardcoded group name
    private static final String USER_NAME = "lofe";   // Hardcoded username

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

        playerTurnTextView.setText("White's Turn");

        WebSocketChessListener listener = new WebSocketChessListener(this);
        webSocketManager = new WebSocketManager(listener);

        // Construct the WebSocket URL using the group name and user name
        String webSocketUrl = "ws://10.90.73.46:8080/group/" + GROUP_NAME + "/" + USER_NAME;

        // Connect to WebSocket server using the constructed URL
        webSocketManager.connect(webSocketUrl);
    }

    public  void updateBoard(int fromX, int fromY, int toX, int toY)
    {
        chessBoardAdapter.movePiece(fromX, fromY, toX, toY);
        // Switch turn after a move
        currentTurn = (currentTurn + 1) % 2;
        playerTurnTextView.setText(currentTurn == 0 ? "White's Turn" : "Black's Turn");
        isMyTurn = !isMyTurn;
    }

    public  void undoInvalidMove()
    {
        chessBoardAdapter.undoInvalidMove();
        Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
    }

    public void displayConnectionStatus(String status)
    {
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
    }
    public void displayMessage(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void notifyMove(int fromX, int fromY, int toX, int toY)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "chessMove");
        obj.addProperty("rowStart", fromY);
        obj.addProperty("colStart", fromX);
        obj.addProperty("rowEnd", toX);
        obj.addProperty("colEnd", toX);
        Log.d("ChessBoardActivity", "Sending move: " + obj.toString());
        webSocketManager.sendMessage(obj.toString());
    }

    private void onPieceSelected(int fromX, int fromY, int toX, int toY) {
        if (!isMyTurn) {
            Toast.makeText(this, "It's not your turn!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (chessBoardAdapter.isValidMove(fromX, fromY, toX, toY)) {
            chessBoardAdapter.movePiece(fromX, fromY, toX, toY);

            // Switch turn after a successful move
            currentTurn = (currentTurn + 1) % 2;
            playerTurnTextView.setText(currentTurn == 0 ? "White's Turn" : "Black's Turn");
            isMyTurn = !isMyTurn;
            notifyMove(fromX, fromY, toX, toY);
        } else {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
        }
    }
}
