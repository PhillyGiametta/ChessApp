package Backend.ChessApp.Game.Pieces;

public enum PieceType {
    PAWN, BISHOP, ROOK, KNIGHT, QUEEN, KING, OPEN;

    public char getSymbol() {
        return switch (this) {
            case PAWN -> 'P';
            case QUEEN -> 'Q';
            case KING -> 'K';
            case BISHOP -> 'B';
            case KNIGHT -> 'N';
            case ROOK -> 'R';
            case OPEN -> '_'; // Or use ' ' for a blank space
            default -> ' ';
        };
    }
}
