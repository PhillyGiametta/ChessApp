package com.example.chessapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChessBoardActivity extends AppCompatActivity {
    private RecyclerView chessBoardRecyclerView;
    private ChessBoardAdapter chessBoardAdapter;
    private TextView playerTurnTextView;
    private int currentTurn = 0; // 0 for white, 1 for black
    private boolean isMyTurn = true; // Start with White's turn

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);

        playerTurnTextView = findViewById(R.id.playerTurnTextView);
        chessBoardRecyclerView = findViewById(R.id.chessBoardRecyclerView);
        chessBoardRecyclerView.setLayoutManager(new GridLayoutManager(this, 8));

        chessBoardAdapter = new ChessBoardAdapter(this::onPieceSelected);
        chessBoardRecyclerView.setAdapter(chessBoardAdapter);

        playerTurnTextView.setText("White's Turn");
    }

    private void onPieceSelected(int fromX, int fromY, int toX, int toY) {
        if (!isMyTurn) {
            Toast.makeText(this, "It's not your turn!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (chessBoardAdapter.isValidMove(fromX, fromY, toX, toY, currentTurn)) {
            chessBoardAdapter.movePiece(fromX, fromY, toX, toY);

            // Switch turn after a successful move
            currentTurn = (currentTurn + 1) % 2;
            playerTurnTextView.setText(currentTurn == 0 ? "White's Turn" : "Black's Turn");
            isMyTurn = !isMyTurn;
        } else {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
        }
    }
}
