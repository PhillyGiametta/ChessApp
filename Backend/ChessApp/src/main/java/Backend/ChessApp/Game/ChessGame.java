package Backend.ChessApp.Game;

import Backend.ChessApp.Game.Board.Board;

public class ChessGame {

    public static void main (String[] args){
        Board ChessBoard = new Board();
        Board ChessBoard2 = new Board(ChessBoard);
        System.out.println(ChessBoard);
        System.out.println(ChessBoard2);
        ChessBoard.getTile(7,1).getTile().setPossibleMoves();
        System.out.println(ChessBoard.getTile(7,1).getTile().printPossibleMoves());
        System.out.println(ChessBoard.getTile(7,1));
        ChessBoard.getTile(7, 1).getTile().move(ChessBoard.getTile(6,2));
        System.out.println(ChessBoard);
    }
}
