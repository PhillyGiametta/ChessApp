package Backend.ChessApp.Game.Board;

/**
 * Creates an updates a tile according to move logic.
 * This will create an individual square on the board and will allow pieces to move onto capture on,
 * and create a actively updated board for all players and spectators
 */

import javax.swing.*;
import java.awt.*;

public class ChessSquare extends JButton {
    private int row;
    private int col;

    public ChessSquare(int row, int col) {
        this.row = row;
        this.col = col;
        initButton();
    }

    private void initButton() {
        setPreferredSize(new Dimension(64, 64));

        if ((row + col) % 2 == 0) {
            setBackground(Color.LIGHT_GRAY);
        } else {
            setBackground(new Color(205, 133, 63));
        }

        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFont(new Font("Serif", Font.BOLD, 36));
    }

    public void setPieceSymbol(String symbol, Color color) {
        this.setText(symbol);
        this.setForeground(color);
    }

    public void clearPieceSymbol() {
        this.setText("");
    }
}
