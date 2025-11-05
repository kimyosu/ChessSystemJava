package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;
//Extende a classe ChessPiece para representar a peça Torre no xadrez
public class Rook extends ChessPiece {

    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "\uED66"; // Unicode para o símbolo da torre
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        return mat;
    }
}
