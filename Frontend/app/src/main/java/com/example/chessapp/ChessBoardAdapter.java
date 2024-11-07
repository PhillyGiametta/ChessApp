package com.example.chessapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChessBoardAdapter extends RecyclerView.Adapter<ChessBoardAdapter.ChessViewHolder> {

    private final List<ChessPiece> board;
    private final OnPieceSelectedListener onPieceSelectedListener;
    private int currentTurn = 0; // 0 for white, 1 for black

    public ChessBoardAdapter(OnPieceSelectedListener listener) {
        this.onPieceSelectedListener = listener;
        this.board = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize the board with empty pieces for simplicity.
        for (int i = 0; i < 64; i++) {
            board.add(new ChessPiece(R.drawable.empty_piece, false));
        }
        // Add actual pieces where needed for initial setup.
    }

    @NonNull
    @Override
    public ChessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chess_square, parent, false);
        return new ChessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChessViewHolder holder, int position) {
        holder.bind(board.get(position));
    }

    @Override
    public int getItemCount() {
        return board.size();
    }

    public void movePiece(int fromIndex, int toIndex) {
        ChessPiece pieceToMove = board.get(fromIndex);
        board.set(toIndex, pieceToMove);
        board.set(fromIndex, new ChessPiece(R.drawable.empty_piece, false));
        notifyDataSetChanged();
    }

    public boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        // Basic move validation (modify as needed for more complete rules)
        return fromX >= 0 && fromX < 8 && fromY >= 0 && fromY < 8 && toX >= 0 && toX < 8 && toY >= 0 && toY < 8;
    }

    public class ChessViewHolder extends RecyclerView.ViewHolder {
        private final ImageView pieceImageView;

        public ChessViewHolder(@NonNull View itemView) {
            super(itemView);
            pieceImageView = itemView.findViewById(R.id.pieceImageView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                int fromX = position / 8;
                int fromY = position % 8;
                // Placeholder logic for selecting a target position, you can expand this
                onPieceSelectedListener.onPieceSelected(fromX, fromY, fromX, fromY);
            });
        }

        public void bind(ChessPiece piece) {
            pieceImageView.setImageResource(piece.getImageResource());
        }
    }

    public interface OnPieceSelectedListener {
        void onPieceSelected(int fromX, int fromY, int toX, int toY);
    }
}
