package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

//Extende a classe ChessPiece para representar a peça Torre no xadrez
public class Queen extends ChessPiece {

    public Queen(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "\uED65"; // Unicode para o símbolo do rei
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);
        //#region Cima Baixo
        //Cima
        p.setValues(position.getRow() - 1, position.getColumn());
        /*
        position = posição da propria peça onde ela está
        Exemplo: se estivermos 7 e queremos saber o que tem acima, iremos diminuindo por 1
         */

        /*
        .positionExists = Retorna se existe uma peça especificando uma Position
        .thereIsAPiece = Verifica se os valores passados estão dentro dos limites(exemplo, valores negativosvoid
         */
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            //Ele vai andando para cima enquanto a posição existir e não tiver peça naquela posição
            mat[p.getRow()][p.getColumn()] = true; //Marca como movimento possivel
            p.setRow(p.getRow() - 1);
        }
        //Quando acabar o while, verifica se tem uma peça adversária para poder capturar
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            //isThereOpponentPiece = método verifica se há uma peça adversária na posição especificada pela cor
            mat[p.getRow()][p.getColumn()] = true;
        }
        /*
        .positionExists = Retorna se existe uma peça especificando uma Position
        .thereIsAPiece = Verifica se os valores passados estão dentro dos limites(exemplo, valores negativos)
         */

        //Baixo
        p.setValues(position.getRow() + 1, position.getColumn());
        /*
        position = posição da propria peça onde ela está
        Exemplo: se estivermos 7 e queremos saber o que tem acima, iremos diminuindo por 1
         */
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true; //Marca como movimento possivel
            p.setRow(p.getRow() + 1);
        }
        //Quando acabar o while, verifica se tem uma peça adversária para poder capturar
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
        /*
        .positionExists = Retorna se existe uma peça especificando uma Position
        .thereIsAPiece = Verifica se os valores passados estão dentro dos limites(exemplo, valores negativos)
         */
        //#endregion


        //#region Esquerda Direita
        //Esquerda
        p.setValues(position.getRow(), position.getColumn() - 1);
        /*
        position = posição da propria peça onde ela está
        Exemplo: se estivermos c(3) e queremos saber o que tem ao lado esquerdo, iremos diminuindo por 1
         */
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true; //Marca como movimento possivel
            p.setColumn(p.getColumn() - 1);
        }
        //Quando acabar o while, verifica se tem uma peça adversária para poder capturar
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
        /*
        .positionExists = Retorna se existe uma peça especificando uma Position
        .thereIsAPiece = Verifica se os valores passados estão dentro dos limites(exemplo, valores negativos)
         */

        //Direita
        p.setValues(position.getRow(), position.getColumn() + 1);
        /*
        position = posição da propria peça onde ela está
        Exemplo: se estivermos c(3) e queremos saber o que tem ao lado esquerdo, iremos diminuindo por 1
         */
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true; //Marca como movimento possivel
            p.setColumn(p.getColumn() + 1);
        }
        //Quando acabar o while, verifica se tem uma peça adversária para poder capturar
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
        /*
        .positionExists = Retorna se existe uma peça especificando uma Position
        .thereIsAPiece = Verifica se os valores passados estão dentro dos limites(exemplo, valores negativos)
         */

        //#endregion


        // ===============================
        // Movimentos diagonais do Bispo
        // ===============================

        // --- NOROESTE (diagonal superior esquerda) ---
        p.setValues(position.getRow() - 1, position.getColumn() - 1);

        // Enquanto a posição existir e estiver vazia, marca como movimento possível
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() - 1, p.getColumn() - 1);
        }

        // Se existir uma peça adversária na posição final, pode capturá-la
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }


        // --- NORDESTE (diagonal superior direita) ---
        p.setValues(position.getRow() - 1, position.getColumn() + 1);

        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() - 1, p.getColumn() + 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }


        // --- SUDESTE (diagonal inferior direita) ---
        p.setValues(position.getRow() + 1, position.getColumn() + 1);

        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() + 1, p.getColumn() + 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }


        // --- SUDOESTE (diagonal inferior esquerda) ---
        p.setValues(position.getRow() + 1, position.getColumn() - 1);

        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() + 1, p.getColumn() - 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        return mat;
    }
}
