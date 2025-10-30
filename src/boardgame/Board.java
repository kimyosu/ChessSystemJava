package boardgame;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces; //matriz para armazenar as peças do tabuleiro

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Piece piece(int row, int column) { //método para acessar a peça em uma posição específica
        return pieces[row][column]; //retorna a peça na posição especificada
    }

    public Piece piece(Position position) { //método para acessar a peça usando um objeto Position
        return pieces[position.getRow()][position.getColumn()]; //retorna a peça na posição especificada
    }
    public void placePiece(Piece piece, Position position){ //método para colocar uma peça no tabuleiro
        pieces[position.getRow()][position.getColumn()] = piece; //coloca a peça na posição especificada
        //pieces é a matriz de peças do tabuleiro(declarado no começo da classe)
        piece.position = position; //atualiza a posição da peça
    }
}
