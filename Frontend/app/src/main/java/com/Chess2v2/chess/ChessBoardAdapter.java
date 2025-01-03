package com.Chess2v2.chess;

import android.graphics.Color;
import android.util.Log;
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
import java.util.Objects;
import java.util.stream.Collectors;

public class ChessBoardAdapter extends RecyclerView.Adapter<ChessBoardAdapter.ChessViewHolder> {

    private static final char[] INITIAL_PIECES = {
            'R', 'H', 'B', 'Q', 'K', 'B', 'H', 'R',
            'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P',
            'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
            'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
            'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
            'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
            'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P',
            'R', 'H', 'B', 'Q', 'K', 'B', 'H', 'R',
    };
    private final ChessBoard board;
    private final OnPositionSelectedListener onPositionSelectedListener;
    private final MoveValidator validator;
    private final OnMoveCompletionListener moveCompletionListener;
    private boolean whiteTurn; // 0 for white, 1 for black
    private UserData player = null;
    private int previousAdapterPosition = -1;
    private List<Marker> markers;
    private boolean isBoardInverted;
    private List<Integer> validMoves = new ArrayList<>();
    private boolean hasGameStarted = false;
    private MoveValidator.Castling castlingState;

    public ChessBoardAdapter(OnPositionSelectedListener listener,
                             boolean currentTurn, UserData player,
                             boolean isBoardInverted,
                             OnMoveCompletionListener moveCompletionListener
    ) {
        this.onPositionSelectedListener = listener;
        this.castlingState = new MoveValidator.Castling();
        this.board = new ChessBoard(isBoardInverted, this.castlingState);
        this.player = player;
        this.isBoardInverted = isBoardInverted;
        this.validator = board.getValidator();
        this.moveCompletionListener = moveCompletionListener;

        this.whiteTurn = currentTurn;
        this.markers = new ArrayList<>();
        initializeBoard();
    }

    public boolean isGameStarted() {
        return hasGameStarted;
    }

    public ChessBoard getBoard() {
        return board;
    }

    private void setBoard(char[] pieces) {
        for (int i = 0; i < INITIAL_PIECES.length; i++) {
            board.set(i, getPiecePosition(i, pieces));
        }
    }

    public boolean getWhiteTurn() {
        return whiteTurn;
    }

    public void setBoardInverted(boolean boardInverted) {
        isBoardInverted = boardInverted;
    }

    public void addMarkers(List<Integer> positions, List<Marker.MarkerType> markerTypes) {
        for (int i = 0; i < positions.size(); i++) {
            markers.add(new Marker(positions.get(i), markerTypes.get(i)));
        }
        for (int position : positions
        ) {
            notifyItemChanged(position);
        }
    }

    private Marker.MarkerType getMarkerTypeAtPosition(int positionIndex) {
        for (Marker marker : markers) {
            if (marker.getPositionIndex() == positionIndex) {
                return marker.getMarkerType();
            }
        }
        return Marker.MarkerType.SQUARE;
    }

    public void removeAllMarker() {
        List<Integer> positions = new ArrayList<>();
        for (Marker marker : markers) {
            positions.add(marker.getPositionIndex());
            positions.add(marker.getPositionIndex());
        }
        markers = new ArrayList<>();
        for (int positionIndex : positions
        ) {
            notifyItemChanged(positionIndex);
        }
    }

    private void initializeBoard() {
        this.markers = new ArrayList<>();
        Position position;
        // Initialize the board
        for (int i = 0; i < INITIAL_PIECES.length; i++) {
            position = getPiecePosition(i, INITIAL_PIECES);
            board.add(position);
        }
    }

    private @NonNull Position getPiecePosition(int i, char[] pieces) {
        Position position;
        char c;
        boolean isWhite;
        ChessPiece piece;
        isWhite = Position.isWhite(i);
        position = new Position(i, isWhite ? R.drawable.empty_white : R.drawable.empty_space);
        c = pieces[i];

        switch (c) {
            case 'P':
                if (player.getSettings() == 0) {
                    piece = new ChessPiece(i > 16 ? R.drawable.pawn_white : R.drawable.pawn_black, "Pawn", i > 16);
                } else {
                    piece = new ChessPiece(i > 16 ? R.drawable.gold_pawn : R.drawable.silver_pawn, "Pawn", i > 16);
                }
                break;
            case 'R':
                if (player.getSettings() == 0) {
                    piece = new ChessPiece(i > 16 ? R.drawable.rook_white : R.drawable.rook_black, "Rook", i > 16);
                } else {
                    piece = new ChessPiece(i > 16 ? R.drawable.gold_rook : R.drawable.silver_rook, "Rook", i > 16);
                }
                break;
            case 'H':
                if (player.getSettings() == 0) {
                    piece = new ChessPiece(i > 16 ? R.drawable.knight_white : R.drawable.knight_black, "Knight", i > 16);
                } else {
                    piece = new ChessPiece(i > 16 ? R.drawable.gold_knight : R.drawable.silver_knight, "Knight", i > 16);
                }
                break;
            case 'B':
                if (player.getSettings() == 0) {
                    piece = new ChessPiece(i > 16 ? R.drawable.bishop_white : R.drawable.bishop_black, "Bishop", i > 16);
                } else {
                    piece = new ChessPiece(i > 16 ? R.drawable.gold_bishop : R.drawable.silver_bishop, "Bishop", i > 16);
                }
                break;
            case 'Q':
                if (player.getSettings() == 0) {
                    piece = new ChessPiece(i > 16 ? R.drawable.queen_white : R.drawable.queen_black, "Queen", i > 16);
                } else {
                    piece = new ChessPiece(i > 16 ? R.drawable.gold_queen : R.drawable.silver_queen, "Queen", i > 16);
                }
                break;
            case 'K':
                if (player.getSettings() == 0) {
                    piece = new ChessPiece(i > 16 ? R.drawable.king_white : R.drawable.king_black, "King", i > 16);
                } else {
                    piece = new ChessPiece(i > 16 ? R.drawable.gold_king : R.drawable.silver_king, "King", i > 16);
                }
                break;
            default:
                piece = null;
                break;
        }
        position.setPiece(piece);
        return position;
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
        int from = Position.coordToInt(fromX, fromY);
        int to = Position.coordToInt(toX, toY);
        ChessPiece piece = board.get(from).getPiece();

        // test for pawn promotion
        if (piece.getName().equals("Pawn") && (toY == 0 || toY == 7)) {
            piece = new ChessPiece(piece.isWhitePiece() ? R.drawable.queen_white : R.drawable.queen_black, "Queen", piece.isWhitePiece());
            board.get(from).setPiece(piece);
        }

        board.move(from, to);
        notifyItemChanged(from);
        notifyItemChanged(to);
        removeAllMarker();
        //
        List<Integer> _markersPosition = new ArrayList<>(List.of(from, to));
        List<Marker.MarkerType> _markerTypes = new ArrayList<>(List.of(Marker.MarkerType.SQUARE, Marker.MarkerType.SQUARE));

        // mark the king if it's checked
        if (board.isBlackKingChecked()) {
            _markersPosition.add(board.blackKingPosition().getIndex());
            _markerTypes.add(Marker.MarkerType.ERROR_SQUARE);
        } else if (
                board.isWhiteKingChecked()
        ) {
            _markersPosition.add(board.whiteKingPosition().getIndex());
            _markerTypes.add(Marker.MarkerType.ERROR_SQUARE);
        }

        addMarkers(_markersPosition, _markerTypes);
        if (piece.getName().equals("King")) {
            if (piece.isWhitePiece()) {
                this.castlingState.whiteKingMoved = true;
            } else {
                this.castlingState.blackKingMoved = true;
            }
            moveCompletionListener.onMoveCompletion(board, from, to);
        }
        if (piece.getName().equals("Rook")) {
            if (piece.isWhitePiece()) {
                if ((fromX == 0)) {
                    castlingState.whiteRookMovedLeft = true;
                } else if (fromX == 7) {
                    castlingState.whiteRookMovedRight = true;
                }
            } else {
                if (fromX == 0) {
                    castlingState.blackRookMovedLeft = true;
                } else if (fromX == 7) {
                    castlingState.blackRookMovedRight = true;
                }
            }
        }

        this.whiteTurn = !whiteTurn;
        moveCompletionListener.onMoveCompletion(board, from, to);
    }

    public void undoInvalidMove() {
        if (board.getMoves().isEmpty())
            return;

        Move move = board.undoMove();

        notifyItemChanged(move.fromIndex);
        notifyItemChanged(move.toIndex);
        this.whiteTurn = !whiteTurn;
        moveCompletionListener.onMoveCompletion(board, move.fromIndex, move.toIndex);
    }

    public boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        if (fromX < 0 || fromX >= 8 || fromY < 0 || fromY >= 8 || toX < 0 || toX >= 8 || toY < 0 || toY >= 8) {
            // Ensure coordinates are within the board bounds
            return false;
        }

        int fromIndex = Position.coordToInt(fromX, fromY);
        int toIndex = Position.coordToInt(toX, toY);

        return validator.isValidMove(fromIndex, toIndex, castlingState);
    }

    public void startGame() {
        this.hasGameStarted = true;
        this.whiteTurn = true;
        this.markers = new ArrayList<>();
        this.castlingState = new MoveValidator.Castling();

        for (int i = 0; i < INITIAL_PIECES.length; i++) {
            board.set(i, getPiecePosition(i, INITIAL_PIECES));
        }
        this.board.getMoves().clear();
        notifyDataSetChanged();
    }

    private boolean isValidCastlingMove(int from, int to) {
        // Check if the castling move is valid for the king and rook
        ChessPiece king = board.get(from).getPiece();
        ChessPiece rook = board.get(to).getPiece();

        // Ensure that the king and rook haven't moved yet
        if (king != null && king.getName().equals("King") && rook != null && rook.getName().equals("Rook")) {
            if (king.isWhitePiece() && !castlingState.whiteKingMoved && !castlingState.whiteRookMovedLeft && !castlingState.whiteRookMovedRight) {
                return true;
            } else
                return !king.isWhitePiece() && !castlingState.blackKingMoved && !castlingState.blackRookMovedLeft && !castlingState.blackRookMovedRight;
        }
        return false;
    }

    private boolean isOutOfCheckMove(ChessPiece clickedPiece, int position) {
        if (clickedPiece == null) {
            previousAdapterPosition = -1;
            return false;
        }


        boolean isOutOfCheckMove = false;

        // Iterate through all moves that can resolve the check
        for (Move move : board.getOutOffCheckMoves()) {
            // Check if the current clicked position is valid for moving out of check
            if ((previousAdapterPosition == -1 && move.fromIndex == position
                    && Objects.equals(move.pieceToMove.getName(), clickedPiece.getName()))
                    || (move.fromIndex == previousAdapterPosition && move.toIndex == position)) {
                isOutOfCheckMove = true;
                break;
            }
        }
        return isOutOfCheckMove;
    }

    public void gameOver() {
        this.hasGameStarted = false;
    }

    public interface OnPositionSelectedListener {
        void onPositionSelected(ChessPiece piece, int fromX, int fromY, int toX, int toY);
    }

    public interface OnMoveCompletionListener {
        void onMoveCompletion(ChessBoard board, int from, int to);
    }

    public class ChessViewHolder extends RecyclerView.ViewHolder {
        private final ImageView pieceImageView;
        private final View dotMarker;
        private final View squareMarker;
        private final View errorSquareMarker;


        public ChessViewHolder(@NonNull View itemView) {
            super(itemView);
            pieceImageView = itemView.findViewById(R.id.chessPieceImageView);
            dotMarker = itemView.findViewById(R.id.dotMarker);
            squareMarker = itemView.findViewById(R.id.squareMarker);
            errorSquareMarker = itemView.findViewById(R.id.errorSquareMarker);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (!hasGameStarted) {
                    Log.v("Clicked", "Game has not started");
                    return;
                }
                Log.v("Clicked", "Position: " + position);

                removeAllMarker();
                // If clicking the same position again, reset the selection
                if (position == previousAdapterPosition) {
                    previousAdapterPosition = -1;
                    return;
                }
                if (previousAdapterPosition == -1) {
                    validMoves = validator.getValidMoves(position, castlingState);
                }

                ChessPiece clickedPiece = board.get(position).getPiece();

                // verify if the move cancels check
                if ((board.isWhiteKingChecked() || board.isBlackKingChecked()) && previousAdapterPosition == -1) {
                    Position kingPosition = board.isBlackKingChecked() ? board.blackKingPosition() : board.whiteKingPosition();

                    if (!isOutOfCheckMove(clickedPiece, position)) {
                        addMarkers(List.of(kingPosition.getIndex()), List.of(Marker.MarkerType.ERROR_SQUARE));
                        previousAdapterPosition = -1;
                        return;
                    }

                    Log.v("Piece Clicked", board.isWhiteKingChecked() ? "White in check" : "Black in check");

                    // filter valid moves that will take the user out of check
                    validMoves = board.getOutOffCheckMoves().stream().filter(move -> validMoves.contains(move.toIndex)
                                    && Objects.equals(move.pieceToMove.getName(), clickedPiece.getName()
                            )
                    ).map(move -> move.toIndex).collect(Collectors.toList());
                }

                // If no piece is selected yet
                if (previousAdapterPosition == -1) {
                    if (clickedPiece != null) {
                        // Check if the selected piece belongs to the current player
                        if ((clickedPiece.isWhitePiece() && whiteTurn) ||
                                (!clickedPiece.isWhitePiece() && !whiteTurn)) {
                            // Highlight valid moves
                            previousAdapterPosition = position;
                            List<Integer> markerPosition = new ArrayList<>();
                            List<Marker.MarkerType> markerTypes = new ArrayList<>();
                            markerTypes.add(Marker.MarkerType.SQUARE);
                            markerPosition.add(position);
                            for (Integer move : validMoves) {
                                markerTypes.add(Marker.MarkerType.DOT);
                                markerPosition.add(move);
                            }
                            addMarkers(markerPosition, markerTypes);
                        } else {
                            Log.v("Click", "Cannot select opponent's piece.");
                        }
                    }
                } else {
                    // Attempting to move the previously selected piece
                    ChessPiece selectedPiece = board.get(previousAdapterPosition).getPiece();
                    if (selectedPiece == null) {
                        Log.v("Click", "No piece to move.");
                        previousAdapterPosition = -1;
                        return;
                    }

                    // Validate the move
                    if (!validMoves.contains(position)) {
                        Log.v("Click", "Invalid move.");
                        previousAdapterPosition = -1;
                        return;
                    }

                    Coord fromCoord = Position.toCoord(previousAdapterPosition);
                    int fromX = fromCoord.x;
                    int fromY = fromCoord.y;
                    Coord toCoord = Position.toCoord(position);
                    int toX = toCoord.x;
                    int toY = toCoord.y;

                    // Deselect after move
                    previousAdapterPosition = -1;

                    // Notify listener about the move
                    onPositionSelectedListener.onPositionSelected(selectedPiece, fromX, fromY, toX, toY);
                }
            });

        }

        public void bind(Position position) {
            pieceImageView.setBackgroundColor(position.isWhite ? Color.WHITE : Color.BLACK);
            pieceImageView.setImageResource(position.getPiece() == null ? position.getImageResource() : position.getPiece().getImageResource());

            dotMarker.setVisibility(View.GONE);
            squareMarker.setVisibility(View.GONE);
            errorSquareMarker.setVisibility(View.GONE);


            for (Marker m : markers
            ) {
                if (m.getPositionIndex() == position.getIndex()) {
                    Marker.MarkerType markerType = m.getMarkerType();
                    switch (markerType) {
                        case DOT:
                            dotMarker.setVisibility(View.VISIBLE);
                            break;
                        case SQUARE:
                            squareMarker.setVisibility(View.VISIBLE);
                            break;
                        case ERROR_SQUARE:
                            errorSquareMarker.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }


        }

    }
}
