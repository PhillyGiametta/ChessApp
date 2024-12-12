package com.Chess2v2.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ChessBoard extends ArrayList<Position> {
    private final Map<String, Integer> whiteCaptures;
    private final Map<String, Integer> blackCaptures;
    private final List<Move> moves;
    private final MoveValidator.Castling castling;
    private final MoveValidator validator;
    private final boolean isBoardInverted;
    private boolean whiteKingChecked;
    private boolean blackKingChecked;
    private final List<Move> outOffCheckMoves;

    public List<Move> getOutOffCheckMoves() {
        return outOffCheckMoves;
    }

    public MoveValidator.Castling getCastling() {
        return castling;
    }

    public ChessBoard(boolean isBoardInverted, MoveValidator.Castling castling) {
        this.castling = castling;
        this.moves = new ArrayList<>();
        this.whiteCaptures = new HashMap<>();
        this.blackCaptures = new HashMap<>();
        this.isBoardInverted = isBoardInverted;
        this.outOffCheckMoves = new ArrayList<>();
        this.validator = new MoveValidator(this, isBoardInverted);
    }

    public MoveValidator getValidator() {
        return validator;
    }

    public boolean isBoardInverted() {
        return isBoardInverted;
    }

    public boolean isWhiteKingChecked() {
        return whiteKingChecked;
    }

    public boolean isBlackKingChecked() {
        return blackKingChecked;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Position whiteKingPosition() {
        for (Position position : this) {
            if (position.getPiece() != null && position.getPiece().getName().equals("King") && position.getPiece().isWhitePiece()) {
                return position;
            }
        }
        return null;
    }

    public Position blackKingPosition() {
        for (Position position : this) {
            if (position.getPiece() != null && position.getPiece().getName().equals("King") && !position.getPiece().isWhitePiece()) {
                return position;
            }
        }
        return null;
    }

    @SuppressWarnings("all")
    public void pieceCaptured(ChessPiece piece) {
        if (piece.isWhitePiece()) {
            int currentCount = blackCaptures.getOrDefault(piece.getName(), 0);
            blackCaptures.put(piece.getName(), currentCount + 1);
        } else {
            int currentCount = whiteCaptures.getOrDefault(piece.getName(), 0);
            whiteCaptures.put(piece.getName(), currentCount + 1);
        }
    }

    public Map<String, Integer> getWhiteCaptures() {
        return whiteCaptures;
    }

    public Map<String, Integer> getBlackCaptures() {
        return blackCaptures;
    }

    public Move undoMove() {
        Move move = moves.remove(moves.size() - 1);
        ChessPiece pieceToMove = get(move.toIndex).getPiece();
        get(move.fromIndex).setPiece(pieceToMove);
        get(move.toIndex).setPiece(move.capturedPiece);

        computeChecked(move, pieceToMove);
        return move;
    }

    public void move(int fromIndex, int toIndex) {
        ChessPiece pieceToMove = get(fromIndex).getPiece();
        Move move = new Move(fromIndex, toIndex, get(toIndex).getPiece());

        get(toIndex).setPiece(pieceToMove);
        get(fromIndex).setPiece(null);
        computeChecked(move, pieceToMove);
        if (move.capturedPiece != null) {
            pieceCaptured(move.capturedPiece);
        }

        moves.add(move);
    }

    private void computeChecked(Move move, ChessPiece pieceMoved) {
        Position kingPosition = pieceMoved.isWhitePiece() ? blackKingPosition() : whiteKingPosition();
        List<Integer> attackerPosition = new ArrayList<>();
        whiteKingChecked = false;
        blackKingChecked = false;

        for (Position position : this) {
            if (position.getPiece() != null && position.getPiece().isWhitePiece() != kingPosition.getPiece().isWhitePiece()) {
                for (Integer possibleMove : validator.getValidMoves(position.getIndex(), castling)) {
                    if (possibleMove == kingPosition.getIndex()) {
                        attackerPosition.add(position.getIndex());
                    }
                }
            }
        }
        if (attackerPosition.isEmpty()) {
            return;
        }
        if (pieceMoved.isWhitePiece()) {
            blackKingChecked = true;
        } else {
            whiteKingChecked = true;
        }
        computeOutOffCheckMoves();
    }

    public void computeOutOffCheckMoves() {
        outOffCheckMoves.clear();
        boolean isWhite = whiteKingChecked;

        for (Position position : this) {
            if (position.getPiece() != null && position.getPiece().isWhitePiece() == isWhite) {
                List<Integer> possibleMoves = validator.getValidMoves(position.getIndex(), castling);
                for (Integer moveIndex : possibleMoves) {
                    Move testMove = new Move(position.getIndex(), moveIndex, get(moveIndex).getPiece(), position.getPiece());

                    // Simulate move
                    ChessPiece capturedPiece = get(moveIndex).getPiece();
                    ChessPiece movedPiece = position.getPiece();
                    get(moveIndex).setPiece(movedPiece);
                    get(position.getIndex()).setPiece(null);

                    // Check if king remains in check
                    boolean isStillChecked = isKingInCheck(isWhite);

                    // Undo move
                    get(position.getIndex()).setPiece(movedPiece);
                    get(moveIndex).setPiece(capturedPiece);

                    if (!isStillChecked) {
                        outOffCheckMoves.add(testMove);
                    }
                }
            }
        }
    }

    private boolean isKingInCheck(boolean isWhite) {
        Position kingPosition = isWhite ? whiteKingPosition() : blackKingPosition();
        if (kingPosition == null) return false;

        for (Position position : this) {
            if (position.getPiece() != null && position.getPiece().isWhitePiece() != isWhite) {
                List<Integer> attackerMoves = validator.getValidMoves(position.getIndex(), castling);
                if (attackerMoves.contains(kingPosition.getIndex())) {
                    return true;
                }
            }
        }
        return false;
    }
}
