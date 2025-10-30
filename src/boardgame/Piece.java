package boardgame;

public class Piece {
    //protected para que as subclasses possam acessar  e outras classes do pacote boardgame também possam acessar
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
