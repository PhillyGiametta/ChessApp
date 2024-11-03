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
        ChessBoard.getTile(7, 1).getTile().move(ChessBoard.getTile(5,2));
        ChessBoard.getTile(6,0).getTile().setPossibleMoves();
        System.out.println(ChessBoard.getTile(6,0).getTile().printPossibleMoves());
        ChessBoard.getTile(6,0).getTile().move(ChessBoard.getTile(5,0));
        ChessBoard.getTile(1,3).getTile().setPossibleMoves();
        System.out.println(ChessBoard.getTile(1,3).getTile().printPossibleMoves());
        ChessBoard.getTile(1,3).getTile().move(ChessBoard.getTile(3,3));
        ChessBoard.getTile(6,4).getTile().setPossibleMoves();
        System.out.println(ChessBoard.getTile(6,4).getTile().printPossibleMoves());
        ChessBoard.getTile(6,4).getTile().move(ChessBoard.getTile(4,4));
        System.out.println(ChessBoard.getTile(3,3));
        ChessBoard.getTile(3,3).getTile().setPossibleMoves();
        System.out.println(ChessBoard.getTile(3,3).getTile().printPossibleMoves());
        ChessBoard.getTile(3,3).getTile().move(ChessBoard.getTile(4,4));
        System.out.println(ChessBoard);
    }
}
