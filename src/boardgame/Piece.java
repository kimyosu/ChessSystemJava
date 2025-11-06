package boardgame;

public abstract class Piece {
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

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(Position position){
        //Verifica se a peça pode se mover para a posição especificada
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    public boolean isThereAnyPossibleMove(){ //Checa se existe algum movimento possivel
        boolean[][] math = possibleMoves();
        for (int i = 0; i < math.length; i++) {
            for (int j = 0; j < math.length; j++) {
                 if (math[i][j]) return true; //Caso tenha um movimento possivel retorne true
            }
        }
        return false;
    }
}
