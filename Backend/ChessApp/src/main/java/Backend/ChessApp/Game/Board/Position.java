package Backend.ChessApp.Game.Board;

import jakarta.transaction.Transactional;

import javax.persistence.Embeddable;
import java.beans.Transient;

@Embeddable
public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
