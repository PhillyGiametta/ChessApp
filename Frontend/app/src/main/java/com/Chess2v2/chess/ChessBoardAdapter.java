package com.Chess2v2.chess;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Chess2v2.app.R;
import com.Chess2v2.app.UserData;

import java.util.ArrayList;
import java.util.List;

public class ChessBoardAdapter extends RecyclerView.Adapter<ChessBoardAdapter.ChessViewHolder> {

    private final List<Position> board;
    private final OnPositionSelectedListener onPositionSelectedListener;
    private int currentTurn = 0; // 0 for white, 1 for black
    private UserData player = null;

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

    public ChessBoardAdapter(OnPositionSelectedListener listener) {
        this.onPositionSelectedListener = listener;
        this.board = new ArrayList<>();
        moves = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        boolean isWhite;
        ChessPiece piece;
        Position position;
        char c;
        // Initialize the board
        for(int i = 0; i < INITIAL_PIECES.length; i++)
        {
            isWhite = Position.isWhite(i);
            position = new Position(i, isWhite ? R.drawable.empty_white :  R.drawable.empty_space);
            c = INITIAL_PIECES[i];

            switch (c)
            {
                case 'P':
                    if(player.getSettings() == 0){
                        piece = new ChessPiece(i > 16 ? R.drawable.pawn_white :  R.drawable.pawn_black, "Pawn", i > 16);
                    } else {
                        piece = new ChessPiece(i > 16 ? R.drawable.gold_pawn :  R.drawable.silver_pawn, "Pawn", i > 16);
                    }
                    break;
                case 'R':
                    if(player.getSettings() == 0){
                        piece = new ChessPiece(i > 16 ? R.drawable.rook_white :  R.drawable.rook_black, "Rook", i > 16);
                    } else {
                        piece = new ChessPiece(i > 16 ? R.drawable.gold_rook :  R.drawable.silver_rook, "Rook", i > 16);
                    }
                    break;
                case 'H':
                    if(player.getSettings() == 0){
                        piece = new ChessPiece(i > 16 ? R.drawable.knight_white :  R.drawable.knight_black, "Knight", i > 16);
                    } else {
                        piece = new ChessPiece(i > 16 ? R.drawable.gold_knight :  R.drawable.silver_knight, "Knight", i > 16);
                    }
                    break;
                case 'B':
                    if(player.getSettings() == 0){
                        piece = new ChessPiece(i > 16 ? R.drawable.bishop_white :  R.drawable.bishop_black, "Bishop", i > 16);
                    } else {
                        piece = new ChessPiece(i > 16 ? R.drawable.gold_bishop :  R.drawable.silver_bishop, "Bishop", i > 16);
                    }
                    break;
                case 'Q':
                    if(player.getSettings() == 0){
                        piece = new ChessPiece(i > 16 ? R.drawable.queen_white :  R.drawable.queen_black, "Queen", i > 16);
                    } else {
                        piece = new ChessPiece(i > 16 ? R.drawable.gold_queen :  R.drawable.silver_queen, "Queen", i > 16);
                    }
                    break;
                case 'K':
                    if(player.getSettings() == 0){
                        piece = new ChessPiece(i > 16 ? R.drawable.king_white :  R.drawable.king_black, "King", i > 16);
                    } else {
                        piece = new ChessPiece(i > 16 ? R.drawable.gold_king :  R.drawable.silver_king, "King", i > 16);
                    }
                    break;
                default:
                    piece = null;
                    break;
            }
            position.setPiece(piece);
            board.add(position);
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
        movePiece(Position.coordToInt(fromX, fromY), Position.coordToInt(toX, toY));
    }

    public boolean undoInvalidMove() {
        if(moves.isEmpty())
            return false;

        Move move = moves.remove(moves.size() - 1);
        board.get(move.fromIndex).setPiece(board.get(move.toIndex).getPiece());
        board.get(move.toIndex).setPiece(move.capturedPiece);

        notifyDataSetChanged();
        return true;
    }
    public void movePiece(int fromIndex, int toIndex) {
        ChessPiece pieceToMove = board.get(fromIndex).getPiece();
        Move move = new Move(fromIndex, toIndex, board.get(toIndex).getPiece());
        moves.add(move);
        board.get(toIndex).setPiece(pieceToMove);
        board.get(fromIndex).setPiece(null);
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
                    if(board.get(position).getPiece() != null)
                    {
                        previousAdapterPosition = position;
                    }
                    return;
                }
                ChessPiece piece = board.get(previousAdapterPosition).getPiece();
                Coord coord = Position.toCoord(previousAdapterPosition);
                int fromX =  coord.x;
                int fromY = coord.y;
                coord = Position.toCoord(position);
                int toX = coord.x;
                int toY = coord.y;
                previousAdapterPosition = -1;
                if(fromX == toX && fromY == toY)
                    return;
                // Placeholder logic for selecting a target position, you can expand this
                onPositionSelectedListener.onPositionSelected(piece, fromX, fromY, toX, toY);
            });
        }

        public void bind(Position position) {
            pieceImageView.setBackgroundColor(position.isWhite ? Color.WHITE : Color.BLACK);
            pieceImageView.setImageResource(position.getPiece() == null ? position.getImageResource() : position.getPiece().getImageResource());
        }
    }

    public interface OnPositionSelectedListener {
        void onPositionSelected(ChessPiece piece, int fromX, int fromY, int toX, int toY);
    }
}
