package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;

// Extende a classe ChessPiece para representar a peça Rei no xadrez
public class King extends ChessPiece {
    public King(Board board, chess.Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "\uED62"; // Unicode para o símbolo do rei
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        return mat;
    }
}
