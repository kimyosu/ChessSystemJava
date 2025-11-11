package chess;

import boardgame.Position;

public class ChessPosition {
    private int row;
    private char column;

    public ChessPosition(int row, char column) {
        defensive(column, row);
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public char getColumn() {
        return column;
    }

    private void defensive(char column, int row){
        if (column < 'a' || column > 'h' || row < 1 || row > 8){
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
        }
    }

    protected Position toPosition() { //converte a posição de xadrez para a posição da matriz
        return new Position(8 - row, column - 'a');
        /*
        "8 - row" converte a linha de xadrez (1 a 8) para a linha da matriz (0 a 7)
            Exemplo: 8 -2 = 6 (linha 2 de xadrez corresponde à linha 6 da matriz)
        "column - 'a'" =  converte a coluna de xadrez ('a' a 'h') para a coluna da matriz (0 a 7)
            Exemplo: 'c' - 'a' = 2 (coluna 'c' de xadrez corresponde à coluna 2 da matriz)
         */
    }

    //converte a posição da matriz para a posição de xadrez
    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition(8 - position.getRow(), (char)('a' + position.getColumn()));
        /*
        "8 - position.getRow()" converte a linha da matriz (0 a 7) para a linha de xadrez (1 a 8)
            Exemplo: 8 - 6 = 2 (linha 6 da matriz corresponde à linha 2 de xadrez)
        "(char) ('a' + position.getColumn())" converte a coluna da matriz (0 a 7) para a coluna de xadrez ('a' a 'h')
            Exemplo: 'a' + 2 = 'c' (coluna 2 da matriz corresponde à coluna 'c' de xadrez)
         */
    }

    @Override
    public String toString(){
        return "" + column + row;
    }

}
