package com.Chess2v2.chess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static Result calculatePieceDifferences(ChessBoard board) {
        // Count pieces for white and black
        Map<String, Integer> whiteCaptures = board.getWhiteCaptures();
        Map<String, Integer> blackCaptures = board.getBlackCaptures();

        StringBuilder whiteDifference = new StringBuilder();
        StringBuilder blackDifference = new StringBuilder();

        // Combine all piece types from both sides
        List<String> allPieces = List.of(
                "Pawn", "Knight", "Bishop", "Rook", "Queen", "King"
        );


        // Append differences for each piece type
        for (String piece : allPieces) {
            if (whiteCaptures.containsKey(piece)) {
                whiteDifference.append(getUnicode(piece, false).repeat(whiteCaptures.get(piece)));
            }
            if (blackCaptures.containsKey(piece)) {
                blackDifference.append(getUnicode(piece, true).repeat(blackCaptures.get(piece)));
            }

        }

        return new Result(whiteDifference.toString(), blackDifference.toString());
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
