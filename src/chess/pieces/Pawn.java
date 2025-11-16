package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    private ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        //Peça branca
        if (getColor() == Color.WHITE) {
            p.setValues(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() - 2, position.getColumn());
            Position p2 = new Position(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getMoveCount() == 0) {
                if (getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                    mat[p.getRow()][p.getColumn()] = true;
                }
            }
            p.setValues(position.getRow() - 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            p.setValues(position.getRow() - 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // #SpecialMove en passant black
            //Left
            if (position.getRow() == 3) { //Caso a peça esteja na posição 3(array)
                Position left = new Position(position.getRow(), position.getColumn() - 1); //Peça da esquerda

                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow() - 1][left.getColumn()] = true;
                    //O peão pode se mover na mesma posição do outro peão(vuneravel ao en passant), porem acima dele
                }
                //Right
                Position right = new Position(position.getRow(), position.getColumn() + 1); //Peça da direita
                if (getBoard().positionExists(right) && getBoard().thereIsAPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow() - 1][right.getColumn()] = true;
                    //O peão pode se mover na mesma posição do outro peão(vuneravel ao en passant) porem acima dele
                }
            }
        /*
        positionExists = verifica se a posição existe e está dentro dos limites e retorna boolean
        thereIsAPiece = verifica e existe uma peça na posição passada e retorna boolean
        .piece = retorna a peça com base na Position passada como parametro
         */
        } else {
            p.setValues(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() + 2, position.getColumn());
            Position p2 = new Position(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getMoveCount() == 0) {
                if (getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                    mat[p.getRow()][p.getColumn()] = true;
                }
            }
            p.setValues(position.getRow() + 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            p.setValues(position.getRow() + 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // #SpecialMove en passant white
            //Left
            if (position.getRow() == 4) { //Caso a peça esteja na posição 3(array)
                Position left = new Position(position.getRow(), position.getColumn() - 1); //Peça da esquerda
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow() + 1][left.getColumn()] = true;
                    //O peão pode se mover na mesma posição do outro peão(vuneravel ao en passant), porem abaixo dele
                }
                //Right
                Position right = new Position(position.getRow(), position.getColumn() + 1); //Peça da direita
                if (getBoard().positionExists(right) && getBoard().thereIsAPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow() + 1][right.getColumn()] = true;
                    //O peão pode se mover na mesma posição do outro peão(vuneravel ao en passant) porem abaixo dele
                }
            }
        /*
        positionExists = verifica se a posição existe e está dentro dos limites e retorna boolean
        thereIsAPiece = verifica e existe uma peça na posição passada e retorna boolean
        .piece = retorna a peça com base na Position passada como parametro
         */

        }

        return mat;
    }

    @Override
    public String toString() {
        return "\uED64";
    }
}
