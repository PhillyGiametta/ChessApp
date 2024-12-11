package com.Chess2v2.chess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for analyzing a chess board and calculating the piece differences
 * between white and black players using ASCII/Unicode symbols.
 */
public class BoardAnalyzer {

    private static final Map<String, Integer> PIECE_COUNT = new HashMap<>();

    static {
        // Initializing default piece counts for a standard chess game
        PIECE_COUNT.put("Pawn", 8);
        PIECE_COUNT.put("Rook", 2);
        PIECE_COUNT.put("Knight", 2);
        PIECE_COUNT.put("Bishop", 2);
        PIECE_COUNT.put("Queen", 1);
        PIECE_COUNT.put("King", 1);
    }

    /**
     * Gets the Unicode representation of a chess piece based on its name and color.
     *
     * @param pieceName The name of the chess piece (e.g., "Pawn", "Queen").
     * @param isWhite   True if the piece is white, false if black.
     * @return The Unicode representation of the piece.
     */
    public static String getUnicode(String pieceName, boolean isWhite) {
        switch (pieceName) {
            case "Pawn":
                return isWhite ? "♙" : "♟";
            case "Rook":
                return isWhite ? "♖" : "♜";
            case "Knight":
                return isWhite ? "♘" : "♞";
            case "Bishop":
                return isWhite ? "♗" : "♝";
            case "Queen":
                return isWhite ? "♕" : "♛";
            case "King":
                return isWhite ? "♔" : "♚";
            default:
                return "";
        }
    }

    /**
     * Calculates the differences in pieces between white and black players.
     *
     * @param board The list of positions representing the chess board.
     * @return A result containing strings for white and black piece differences.
     */
    public static Result calculatePieceDifferences(List<Position> board) {
        // Count pieces for white and black
        Map<String, Integer> whitePieces = countPieces(board, true);
        Map<String, Integer> blackPieces = countPieces(board, false);

        StringBuilder whiteDifference = new StringBuilder();
        StringBuilder blackDifference = new StringBuilder();

        // Combine all piece types from both sides
        Set<String> allPieces = new HashSet<>();
        allPieces.addAll(whitePieces.keySet());
        allPieces.addAll(blackPieces.keySet());

        // Append differences for each piece type
        for (String piece : allPieces) {
            if (Objects.equals(piece, "X") || Objects.equals(piece, "King")) {
                continue; // Skip empty positions or kings (no capture possible)
            }

            int whiteCount = whitePieces.getOrDefault(piece, 0);
            int blackCount = blackPieces.getOrDefault(piece, 0);

            int whiteMissing = PIECE_COUNT.get(piece) - whiteCount;
            int blackMissing = PIECE_COUNT.get(piece) - blackCount;

            whiteDifference.append(getUnicode(piece, true).repeat(Math.max(0, blackMissing)));
            blackDifference.append(getUnicode(piece, false).repeat(Math.max(0, whiteMissing)));
        }

        return new Result(whiteDifference.toString(), blackDifference.toString());
    }

    /**
     * Counts the pieces for the specified color.
     *
     * @param board   The list of positions representing the chess board.
     * @param isWhite True to count white pieces, false for black pieces.
     * @return A map of piece types to their counts.
     */
    private static Map<String, Integer> countPieces(List<Position> board, boolean isWhite) {
        return board.stream()
                .filter(position -> position.piece != null && position.getPiece().isWhitePiece() == isWhite)
                .collect(Collectors.groupingBy(
                        position -> position.piece.getName(),
                        Collectors.summingInt(e -> 1)
                ));
    }

    public static class Result {
        String white;
        String black;

        public Result(String white, String black) {
            this.white = white;
            this.black = black;
        }

        public String getWhite() {
            return white;
        }

        public String getBlack() {
            return black;
        }
    }
}
