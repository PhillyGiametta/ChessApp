package Backend.ChessApp.Game.Board;

import javax.persistence.Embeddable;

@Embeddable
public class Position
{
    private int r;
    private int c;

    public Position(int row, int column) {
        this.r = row;
        this.c = column;
    }

    public int getRow() {
        return r;
    }

    public int getColumn() {
        return c;
    }
    @Override
    public String toString(){
        return "("+ r + ", " + c + ")";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return r == position.r && c == position.c;
    }
}
