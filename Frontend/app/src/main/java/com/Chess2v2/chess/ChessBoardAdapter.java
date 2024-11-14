package com.Chess2v2.chess;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Chess2v2.app.R;

import java.util.ArrayList;
import java.util.List;

public class ChessBoardAdapter extends RecyclerView.Adapter<ChessBoardAdapter.ChessViewHolder> {

    private final List<ChessPiece> board;
    private final OnPieceSelectedListener onPieceSelectedListener;
    private int currentTurn = 0; // 0 for white, 1 for black

    private List<Move> moves;
    private int previousAdapterPosition = -1;
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
        moves = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        boolean isWhite;
        ChessPiece piece;
        char c;
        // Initialize the board
        for(int i = 0; i < INITIAL_PIECES.length; i++)
        {
            c = INITIAL_PIECES[i];
            switch (c)
            {
                case 'P':
                    piece = new ChessPiece(i > 16 ? R.drawable.pawn_white :  R.drawable.pawn_black, i > 16);
                    break;
                case 'R':
                    piece = new ChessPiece(i > 16 ? R.drawable.rook_white :  R.drawable.rook_black, i > 16);
                    break;
                case 'H':
                    piece = new ChessPiece(i > 16 ? R.drawable.knight_white :  R.drawable.knight_black, i > 16);
                    break;
                case 'B':
                    piece = new ChessPiece(i > 16 ? R.drawable.bishop_white :  R.drawable.bishop_black, i > 16);
                    break;
                case 'Q':
                    piece = new ChessPiece(i > 16 ? R.drawable.queen_white :  R.drawable.queen_black, i > 16);
                    break;
                case 'K':
                    piece = new ChessPiece(i > 16 ? R.drawable.king_white :  R.drawable.king_black, i > 16);
                    break;
                default:
                    isWhite = (i + (i / 8) % 2)% 2 == 0;
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
        holder.bind(board.get(position), position);
    }

    @Override
    public int getItemCount() {
        return board.size();
    }

    public void movePiece(int fromX, int fromY, int toX, int toY) {
        movePiece(fromX * 8 + fromY, toX * 8 + toY);
    }

    public boolean undoInvalidMove() {
        if(moves.isEmpty())
            return false;

        Move move = moves.remove(moves.size() - 1);
        board.set(move.fromIndex, board.get(move.toIndex));
        board.set(move.toIndex, move.capturedPiece);

        notifyDataSetChanged();
        return true;
    }
    public void movePiece(int fromIndex, int toIndex) {
        ChessPiece pieceToMove = board.get(fromIndex);
        Move move = new Move(fromIndex, toIndex, board.get(toIndex));
        boolean isWhite = (fromIndex + (fromIndex / 8) % 2)% 2 == 0;
        board.set(fromIndex, new ChessPiece(isWhite ? R.drawable.empty_white :  R.drawable.empty_space, isWhite));
        board.set(toIndex, pieceToMove);
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
                if(previousAdapterPosition == -1)
                {
                    previousAdapterPosition = position;
                    return;
                }
                int fromX = previousAdapterPosition / 8;
                int fromY = previousAdapterPosition % 8;
                int toX = position / 8;
                int toY = position % 8;
                previousAdapterPosition = -1;
                if(fromX == toX && fromY == toY)
                    return;
                // Placeholder logic for selecting a target position, you can expand this
                onPieceSelectedListener.onPieceSelected(fromX, fromY, toX, toY);
            });
        }

        public void bind(ChessPiece piece, int position) {
            pieceImageView.setBackgroundColor((position + (position / 8) % 2)% 2 == 0 ? Color.WHITE : Color.BLACK);
            pieceImageView.setImageResource(piece.getImageResource());
        }
    }

    public interface OnPieceSelectedListener {
        void onPieceSelected(int fromX, int fromY, int toX, int toY);
    }
}
