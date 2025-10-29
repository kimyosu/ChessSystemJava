package boardgame;

public class Piece {
    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
        position = null; //null indica que a peça não está em nenhuma posição do tabuleiro inicialmente
    }

    protected Board getBoard() {
        return board;
    }
}
