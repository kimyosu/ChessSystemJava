package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;

// Extende a classe ChessPiece para representar a peça Rei no xadrez
public class King extends ChessPiece {
    private ChessMatch chessMatch;

    public King(Board board, chess.Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    //Método para verificar se a torre está apta para o roque
    private boolean testRookCastling(Position position) {
        //A classe Piece tem um atributo board, (ChesPiece) fazemos um cast para o tipo ChessPiece
        ChessPiece p = (ChessPiece) getBoard().piece(position); //.piece retorna a peça na posição especificada
        return p != null && p instanceof Rook && p.getColor() == getColor() && getMoveCount() == 0;
        /*
        instanceof = verifica se p é da mesma classe que Rook
        getColor = Método da classe Piece que retorna a cor do proprio objeto
         */
    }

    @Override
    public String toString() {
        return "\uED62"; // Unicode para o símbolo do rei
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);
        //cima
        p.setValues(position.getRow() - 1, position.getColumn());
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        //baixo
        p.setValues(position.getRow() + 1, position.getColumn());
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        //esquerda
        p.setValues(position.getRow(), position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        //direita
        p.setValues(position.getRow(), position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        //noroeste
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        //nordeste
        p.setValues(position.getRow() - 1, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        //sudoeste
        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        //sudoeste
        p.setValues(position.getRow() + 1, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        // #SpecialMove Castling
        if (getMoveCount() == 0 && !chessMatch.getCheck()) {
            //Aqui pegamo a posição onde a torre fica, 3 casas a direita
            Position posRookRight = new Position(position.getRow(), position.getColumn() + 3);
            if (testRookCastling(posRookRight)) {
                Position p1 = new Position(position.getRow(), position.getColumn() + 1); //1 casa a direita
                Position p2 = new Position(position.getRow(), position.getColumn() + 2); //2 casa a direita
                //Caso as casas especificadas estejam vazias(null)
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null){
                    mat[p2.getRow()][p2.getColumn()] = true;
                }
            }
        }

        if (getMoveCount() == 0 && !chessMatch.getCheck()){
            //Aqui, pegamos a posição da torre, que fica a 4 casas a esquerda
            Position posRookLeft = new Position(position.getRow(), position.getColumn() - 4);
            if (testRookCastling(posRookLeft)){
                Position p1 = new Position(position.getRow(), position.getColumn() - 1); //1 casa a esquerda
                Position p2 = new Position(position.getRow(), position.getColumn() - 2); //2 casa a esquerda
                Position p3 = new Position(position.getRow(), position.getColumn() - 3); //3 casa a esquerda
                //Caso as casas especificas estejam vazias(null)
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null){
                    mat[p3.getRow()][p2.getColumn()] = true; //marque como movimeno possivel
                }
            }
        }


        return mat;
    }
}
