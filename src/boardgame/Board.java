package boardgame;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces; //matriz para armazenar as peças do tabuleiro

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Error creating board: there must be at least 1 row and 1 column");

        }
        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns]; //inicializa a matriz de peças com o número de linhas e colunas especificado
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Piece piece(int row, int column) { //método para acessar a peça em uma posição específica
        //verificação defensiva para garantir que a posição está dentro dos limites do tabuleiro
        if (!positionExists(row, column)) throw new BoardException("Position not on the board");
        return pieces[row][column]; //retorna a peça na posição especificada
    }

    public Piece piece(Position position) { //método para acessar a peça usando um objeto Position
        //verificação defensiva para garantir que a posição está dentro dos limites do tabuleiro
        if (!positionExists(position)) throw new BoardException("Position not on the board");

        return pieces[position.getRow()][position.getColumn()]; //retorna a peça na posição especificada
    }

    public void placePiece(Piece piece, Position position) { //método para colocar uma peça no tabuleiro
        if (thereIsAPiece(position)) { //verifica se já existe uma peça na posição especificada
            throw new BoardException("There is already a piece on position " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece; //coloca a peça na posição especificada
        //pieces é a matriz de peças do tabuleiro(declarado no começo da classe)
        piece.position = position; //atualiza a posição da peça
    }

    //método defensivo para verificar se uma posição existe no tabuleiro
    private Boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
        /*
        "row >= 0" verifica se a linha é maior ou igual a 0 (não negativa)
        "row < rows" verifica se a linha é menor que o número total de linhas do tabuleiro
        "column >= 0" verifica se a coluna é maior ou igual a 0 (não negativa)
        "column < columns" verifica se a coluna é menor que o número total de colunas do tabuleiro
         */
    }

    //método para verificar se uma posição existe no tabuleiro usando um objeto Position
    public Boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    public Boolean thereIsAPiece(Position position) {
        //verificação defensiva para garantir que a posição está dentro dos limites do tabuleiro
        if (positionExists(position)) throw new BoardException("Position not on the board");
        //piece é o método que retorna a peça na posição especificada
        return piece(position) != null;  //caso haja uma peça na posição, retorna true
    }
}
