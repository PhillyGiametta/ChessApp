package com.Chess2v2.chess;

import java.util.ArrayList;
import java.util.List;

public class MoveValidator {

    private final List<Position> board;
    private final boolean isBoardFlipped;

    public MoveValidator(List<Position> board, boolean isBoardFlipped) {
        this.board = board;
        this.isBoardFlipped = isBoardFlipped;
    }

    /**
     * Validates a move based on chess rules.
     *
     * @param fromIndex the starting position of the piece.
     * @param toIndex   the target position of the piece.
     * @param castling  the castling object to track moves of kings and rooks
     * @return true if the move is valid, false otherwise.
     */
    public boolean isValidMove(int fromIndex, int toIndex, Castling castling) {
        if (fromIndex == toIndex) return false;

        ChessPiece piece = board.get(fromIndex).getPiece();
        if (piece == null) return false;

        // Check if the destination position contains the enemy king
        ChessPiece targetPiece = board.get(toIndex).getPiece();
        if (targetPiece != null && targetPiece.getName().equals("King")) {
            // Allow the move to take the king
            System.out.println(targetPiece.isWhitePiece()
                    ? "Game over! Black wins. The white king has been captured."
                    : "Game over! White wins. The black king has been captured.");
            return true; // Allow the move to capture the king
        }

        if (isOwnPieceAtDestination(toIndex, piece)) {
            return false;
        }

        boolean isValid = false;

        switch (piece.getName()) {
            case "Pawn":
                isValid = isValidPawnMove(fromIndex, toIndex, piece.isWhitePiece());
                break;
            case "Rook":
                isValid = isValidRookMove(fromIndex, toIndex);
                break;
            case "Knight":
                isValid = isValidKnightMove(fromIndex, toIndex);
                break;
            case "Bishop":
                isValid = isValidBishopMove(fromIndex, toIndex);
                break;
            case "Queen":
                isValid = isValidQueenMove(fromIndex, toIndex);
                break;
            case "King":
                isValid = isValidKingMove(fromIndex, toIndex, castling);
                break;
        }
        return isValid;
    }


    private boolean isValidKingMove(int fromIndex, int toIndex, Castling castling) {
        Coord fromCord = Position.toCoord(fromIndex);
        Coord toCord = Position.toCoord(toIndex);

        int dx = Math.abs(fromCord.x - toCord.x);
        int dy = Math.abs(fromCord.y - toCord.y);

        // Regular King move (1 square in any direction)
        if (dx <= 1 && dy <= 1) {
            return true;
        }

        return false;
    }

    private boolean isValidPawnMove(int fromIndex, int toIndex, boolean isWhite) {
        int direction = isWhite ? (isBoardFlipped ? -1 : 1) : (isBoardFlipped ? 1 : -1);
        int startingY = isWhite ? (isBoardFlipped ? 6 : 1) : (isBoardFlipped ? 1 : 6);

        int fromX = Position.toCoord(fromIndex).x;
        int fromY = Position.toCoord(fromIndex).y;
        int toX = Position.toCoord(toIndex).x;
        int toY = Position.toCoord(toIndex).y;

        // Ensure the pawn moves in the correct column
        if (fromX == toX) {
            // Single step forward
            if (toY == fromY + direction && board.get(toIndex).getPiece() == null) {
                return true;
            }
            // Double step forward from starting position
            if (fromY == startingY && toY == fromY + 2 * direction && board.get(toIndex).getPiece() == null
                    && board.get(Position.coordToInt(fromX, fromY + direction)).getPiece() == null) {
                return true;
            }
        }

        // Diagonal capture
        return Math.abs(fromX - toX) == 1 && toY == fromY + direction && board.get(toIndex).getPiece() != null;
    }

    private boolean isValidRookMove(int fromIndex, int toIndex) {
        return isStraightLineMove(fromIndex, toIndex) && isPathClear(fromIndex, toIndex);
    }

    private boolean isValidKnightMove(int fromIndex, int toIndex) {
        int dx = Math.abs(Position.toCoord(fromIndex).x - Position.toCoord(toIndex).x);
        int dy = Math.abs(Position.toCoord(fromIndex).y - Position.toCoord(toIndex).y);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    private boolean isValidBishopMove(int fromIndex, int toIndex) {
        return isDiagonalMove(fromIndex, toIndex) && isPathClear(fromIndex, toIndex);
    }

    private boolean isValidQueenMove(int fromIndex, int toIndex) {
        return (isStraightLineMove(fromIndex, toIndex) || isDiagonalMove(fromIndex, toIndex)) && isPathClear(fromIndex, toIndex);
    }

    private boolean isStraightLineMove(int fromIndex, int toIndex) {
        Coord from = Position.toCoord(fromIndex);
        Coord to = Position.toCoord(toIndex);
        return from.x == to.x || from.y == to.y;
    }

    private boolean isDiagonalMove(int fromIndex, int toIndex) {
        Coord from = Position.toCoord(fromIndex);
        Coord to = Position.toCoord(toIndex);
        return Math.abs(from.x - to.x) == Math.abs(from.y - to.y);
    }

    private boolean isPathClear(int fromIndex, int toIndex) {
        Coord from = Position.toCoord(fromIndex);
        Coord to = Position.toCoord(toIndex);

        int dx = Integer.signum(to.x - from.x);
        int dy = Integer.signum(to.y - from.y);

        int x = from.x + dx, y = from.y + dy;
        while (x != to.x || y != to.y) {
            int index = Position.coordToInt(x, y);
            if (board.get(index).getPiece() != null) return false;
            x += dx;
            y += dy;
        }
        return true;
    }

    private boolean isOwnPieceAtDestination(int toIndex, ChessPiece piece) {
        ChessPiece destPiece = board.get(toIndex).getPiece();
        if (destPiece == null) {
            return false;
        }
        return piece.isWhitePiece() == destPiece.isWhitePiece();
    }

    public List<Integer> getValidMoves(int fromIndex, Castling castling) {
        List<Integer> validMoves = new ArrayList<>();

        for (int toIndex = 0; toIndex < 64; toIndex++) {
            if (isValidMove(fromIndex, toIndex, castling)) {
                validMoves.add(toIndex);
            }
        }
        return validMoves;
    }

    public static class Castling {
        public boolean whiteKingMoved = false;
        public boolean blackKingMoved = false;
        public boolean whiteRookMovedLeft = false;
        public boolean whiteRookMovedRight = false;
        public boolean blackRookMovedLeft = false;
        public boolean blackRookMovedRight = false;
    }
}
