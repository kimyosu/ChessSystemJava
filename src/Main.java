import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;

public class Main {
    public static void main(String[] args) {
        Position position = new Position(3, 5);
        System.out.println(position);
        Board board = new Board(8, 8);
        ChessMatch chessMatch = new ChessMatch();
        UI.printBoard(chessMatch.getPieces());
    }
}
