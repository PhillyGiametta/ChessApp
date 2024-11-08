package com.example.chessapp;

import android.graphics.Color;
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

    private static final char[] INITIAL_PIECES = {
                                                    'R', 'H', 'B', 'Q', 'K','B','H','R',
                                                    'P', 'P', 'P', 'P', 'P','P','P','P',
                                                    'X', 'X', 'X', 'X', 'X','X','X','X',
                                                    'X', 'X', 'X', 'X', 'X','X','X','X',
                                                    'X', 'X', 'X', 'X', 'X','X','X','X',
                                                    'X', 'X', 'X', 'X', 'X','X','X','X',
                                                    'P', 'P', 'P', 'P', 'P','P','P','P',
                                                    'R', 'H', 'B', 'Q', 'K','B','H','R',
                                                };

    public ChessBoardAdapter(OnPieceSelectedListener listener) {
        this.onPieceSelectedListener = listener;
        this.board = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        boolean isWhite;
        ChessPiece piece;
        char c;
        // Initialize the board
        for(int i = 0; i < INITIAL_PIECES.length; i++)
        {
            isWhite = (i + (i / 8) % 2)% 2 == 0;
            c = INITIAL_PIECES[i];
            switch (c)
            {
                case 'P':
                    piece = new ChessPiece(i > 16 ? R.drawable.pawn_white :  R.drawable.pawn_black, isWhite);
                    break;
                case 'R':
                    piece = new ChessPiece(i > 16 ? R.drawable.rook_white :  R.drawable.rook_black, isWhite);
                    break;
                case 'H':
                    piece = new ChessPiece(i > 16 ? R.drawable.knight_white :  R.drawable.knight_black, isWhite);
                    break;
                case 'B':
                    piece = new ChessPiece(i > 16 ? R.drawable.bishop_white :  R.drawable.bishop_black, isWhite);
                    break;
                case 'Q':
                    piece = new ChessPiece(i > 16 ? R.drawable.queen_white :  R.drawable.queen_black, isWhite);
                    break;
                case 'K':
                    piece = new ChessPiece(i > 16 ? R.drawable.king_white :  R.drawable.king_black, isWhite);
                    break;
                default:
                    piece = new ChessPiece(isWhite ? R.drawable.empty_white :  R.drawable.empty_space, isWhite);
                    break;
            }

            board.add(piece);
        }
    }

    @NonNull
    @Override
    public ChessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chess_cell, parent, false);
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

    public void movePiece(int fromX, int fromY, int toX, int toY) {
        movePiece(fromX * 8 + fromY, toX * 8 + toY);
    }

    public void movePiece(int fromIndex, int toIndex) {
        ChessPiece pieceToMove = board.get(fromIndex);

        if (pieceToMove == null || pieceToMove.getImageResource() == R.drawable.empty_space) {
            // No piece to move
            return;
        }

        // Update the board
        board.set(toIndex, pieceToMove);

        boolean isWhite = (fromIndex + (fromIndex / 8) % 2) % 2 == 0;
        board.set(fromIndex, new ChessPiece(isWhite ? R.drawable.empty_white : R.drawable.empty_space, isWhite));

        // Notify that the data has changed so the UI is updated
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
            pieceImageView = itemView.findViewById(R.id.chessPieceImageView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                int fromX = position / 8;
                int fromY = position % 8;

                // Select the piece and determine a target move (for simplicity, just move in place as an example)
                onPieceSelectedListener.onPieceSelected(fromX, fromY, fromX, fromY);
            });
        }

        public void bind(ChessPiece piece) {
            pieceImageView.setBackgroundColor(piece.isWhitePiece() ? Color.WHITE : Color.BLACK);
            pieceImageView.setImageResource(piece.getImageResource());
        }
    }

    public ChessPiece getPieceAtPosition(int x, int y) {
        int index = x * 8 + y;
        return board.get(index);
    }


    public interface OnPieceSelectedListener {
        void onPieceSelected(int fromX, int fromY, int toX, int toY);
    }
}
