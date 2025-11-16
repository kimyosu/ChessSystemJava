package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;
import chess.pieces.Queen;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class ChessMatch {
    private ChessPiece promoted;
    private boolean checkMate;
    private List<Piece> piecesOnTheBoard = new ArrayList<>(); //Peças que estão no tabuleiro
    private List<Piece> capturedPieces = new ArrayList<>(); //Peças capturadas
    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private ChessPiece enPassantVulnerable;

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();

    }

    public int getTurn() {
        return turn;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    public boolean getCheckMate() {
        return checkMate;

    }

    public boolean getCheck() {
        return check;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()]; //matriz de peças de xadrez
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j); //casting para ChessPiece
            }
        }
        return mat;
    }

    //Método para realizar um movimento de xadrez
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);
        if (testCheck(currentPlayer)) { //caso o jogador se mova para um lugar onde se coloque em xeque
            undoMove(source, target, capturedPiece); //retorne a peça para a posição original
            throw new ChessException("You can't put yourself in check");
        }

        //Pegamos a peça que se moveu acima
        ChessPiece movedPiece = (ChessPiece) board.piece(target);
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if (movedPiece.getColor() == Color.WHITE && target.getRow() == 0 || movedPiece.getColor() == Color.BLACK && target.getRow() == 7) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }
        check = testCheck(opponent(currentPlayer));

        /*Caso o inimigo esteja em xeque, check vai ser true, caso contrario vai ser false
        opponent retorna a cor contraria, se eu passar branco ele retorna preto, currentPlayer = player turno atual
         */
        if (testCheckMate(opponent(currentPlayer))) { //Caso seja xeque mate  da cor contraria da atual
            //opponent retorna a cor contraria
            checkMate = true; //checkMate vai ser true
        } else { //se não, passa pro proximo turno
            nextTurn();
        }
        //Verificamos se a peça andou duas casas, seja para cima ou para baixo(peça branca ou preta)
        if (movedPiece instanceof Pawn && target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }
        return (ChessPiece) capturedPiece;
    }

    public  ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }

        if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")){
            throw new InvalidParameterException("Invalid type for promotion");
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        return switch (type) {
            case "B" -> new Bishop(board, color);
            case "N" -> new Knight(board, color);
            case "Q" -> new Queen(board, color);
            default -> new Rook(board, color);
        };
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void validateSourcePosition(Position position) {

        if (!board.thereIsAPiece(position)) throw new ChessException("There is no piece on source position");
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) throw new ChessException("There is no possible moves " +
                " for the chosen piece");
    }


    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
    //currentPlayer = (currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE);
        /*
        currentPlayer = Caso o jogador atual seja igual a Color.WHITE
        Agora ele vai ser Color.black, caso o contrario ele vai continuar Color.WHITE
         */

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source); //Retira a peça na posição inicial
        p.increaseMoveCount(); //Aumente o moveCount
        Piece capturedPiece = board.removePiece(target); //Tiramos a possivel peça que estava na posição de destino
        //da peça movida
        board.placePiece(p, target); //Movemos a peça ate a Position(posição) de destino
        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }


        //Caso  o King tenha se movido duas casas para a direita, então foi um roque pequeno
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            //Pega a posição inicial do rei(antes do roque)
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            //Pega a posição inicial do rei(antes do roque) + 1 casa para a direita(do lado direito do rei)
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT); //Remove o King
            board.placePiece(rook, targetT); //Coloca a peça na posição do targetT
            rook.increaseMoveCount();
        }
        //Caso  o King tenha se movido três casas para a esquerda, então foi um roque grande
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            //Pega a posição inicial do rei(antes do roque)
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            //Pega a posição inicial do rei(antes do roque) + 1 casa para a direita(do lado direito do rei)
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT); //Remove o King
            board.placePiece(rook, targetT); //Coloca a peça na posição do targetT
            rook.increaseMoveCount();
        }
        // #specialMove en passant
        if (p instanceof Pawn) {
            //Caso a coluna antes de mover seja diferente da coluna depois do movimento e não tenha capturado nenhuma peça
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawPosition;
                if (p.getColor() == Color.WHITE) { //caso a peça que se moveu seja branca
                    pawPosition = new Position(target.getRow() + 1, target.getColumn());
                    //getRow + 1 se trata da linha acima
                } else {
                    pawPosition = new Position(target.getRow() - 1, target.getColumn());
                    //getRow - 1 se trata da linha de baixo
                }
                capturedPiece = board.removePiece(pawPosition); //remove a peça  e salva em capturedPiece
                capturedPieces.add(capturedPiece); //Adiciona na lista de peças capturadas a capturedPiece
                piecesOnTheBoard.remove(capturedPiece); //Remove da lista de peças no tabuleiro a peça capturada

            }
        }
        return capturedPiece;
    }

    //Método para desfazer os movimentos
    public void undoMove(Position source, Position target, Piece capturedPiece) {
        //Remove a peça onde estava
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount(); //Diminua o moveCount
        //Coloca a peça onde estava
        board.placePiece(p, source);
        if (capturedPiece != null) {//Se a peça foi capturada
            board.placePiece(capturedPiece, target); //Coloque a peça de volta onde estava
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        //Caso  o King tenha se movido duas casas para a direita, então foi um roque pequeno
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            //Pega a posição inicial do rei(antes do roque)
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            //Pega a posição inicial do rei(antes do roque) + 1 casa para a direita(do lado direito do rei)
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT); //Remove o King
            board.placePiece(rook, sourceT); //Coloca a peça na posição do targetT
            rook.decreaseMoveCount();
        }
        //Caso  o King tenha se movido três casas para a esquerda, então foi um roque grande
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            //Pega a posição inicial do rei(antes do roque)
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            //Pega a posição inicial do rei(antes do roque) + 1 casa para a direita(do lado direito do rei)
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(target); //Remove o King
            board.placePiece(rook, sourceT); //Coloca a peça na posição do sourceT
            rook.decreaseMoveCount();
        }
        // #specialMove en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawPosition;
                if (p.getColor() == Color.WHITE) {
                    pawPosition = new Position(3, target.getColumn());

                } else {
                    pawPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawPosition);
                capturedPiece = board.removePiece(pawPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);

            }
        }
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE ? Color.BLACK : Color.WHITE);
        //Caso color for igual a WHITE retorne Color.BLACK, caso contrario retorne Color.WHITE
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream()
                .filter(x -> ((ChessPiece) x).getColor() == color)
                .toList(); //Fazemos downcast já que Piece não tem uma cor e sim o ChessPiece
        for (Piece p : list) {
            if (p instanceof King) {
                return ((ChessPiece) p);
            }
        }

        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        /*
       king() = Retorna um ChessPiece
       .getChessPosition() =  Apartir do ChessPiece do king() retornado retorna um ChessPosition, esse getChesPosition
            Chama um método fromPosition que converte  de matriz para posição xadrez
        .toPosition() = Apartir desse getChessPosition() chama o método .toPosition() que converte de
            Posição xadrez para matriz
         */
        //Lista para guardas as peças inimigas
        List<Piece> opponentPiece = piecesOnTheBoard.stream()
                .filter(x -> ((ChessPiece) x).getColor() == opponent(color)).toList();
        //opponent = Retorna a cor contraria da cor passada, exemplo, se passei a cor branca devolva a cor preta

        for (Piece p : opponentPiece) {
            boolean[][] mat = p.possibleMoves();
            /*
            Array de boolean, já que os movimentos possiveis
                Indicam qual casa é true(pode ir e ser capturada) e qual casa é false(não podem ir)
            */

            /*
            Verifica se a casas na linha do rei é true, ou seja, se a peça adversaria pode ir/capturar
            Então significa que o rei está em xeque
            */
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true; //Retorna true, indicando que a peça está em xeque
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) return false; //Caso a peça NÃO esteja em xeque, retorne false, indicando que não tem
        //xeque mate


        List<Piece> list = piecesOnTheBoard.stream()
                .filter(x -> ((ChessPiece) x).getColor() == color).toList();
        //Filtra a lista pegando apenas as cores que foram passadas pelo parametro

        for (Piece x : list) {
            boolean[][] mat = x.possibleMoves();
            /*
            Array de boolean, já que os movimentos possiveis
                Indicam qual casa é true(pode ir e ser capturada) e qual casa é false(não podem ir)
            */

            //For para percorrer linhas e colunas
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {

                    if (mat[i][j]) { //Caso na linha i de coluna j seja verdadeiro
                        Position source = ((ChessPiece) x).getChessPosition().toPosition();
                         /*
                        king() = Retorna um ChessPiece
                        .getChessPosition() =  Apartir do ChessPiece do king() retornado retorna um ChessPosition,
                            esse getChesPosition Chama um método fromPosition que converte  de matriz para posição xadrez
                        .toPosition() = Apartir desse getChessPosition() chama o método .toPosition() que converte de
                        Posição xadrez para matriz
                        */
                        Position target = new Position(i, j); //Target = posição de destino
                        Piece capturedPiece = makeMove(source, target);
                        //makeMove retorna a peça que foi capturada caso alguma tenha sido
                        boolean testCheck = testCheck(color); //testCheck verifica se o game ainda está em xeque
                        undoMove(source, target, capturedPiece); //volta para as posições originais
                        if (!testCheck) return false; //Caso check seja falso retorne falso
                        //Indicando que não está em xeque mate
                        /*
                        Basicamente movemos a peça para saber se tem como o rei andar para evitar o xeque
                        e voltamos para a posição original para tentar mover em outra direção, após tudo isso
                        Caso ele não consiga sair(fique preso) o jogo sinaliza como xeque-mate!
                         */
                    }
                }
            }
        }
        return true; //Caso tudo acima de errado, retorne true, indicando que o jogo está em xeque mate!
    }

    //Método para planejar uma nova peça no tabuleiro
    private void placeNewPiece(char column, int row, ChessPiece piece) { //Método para planejar uma nova peça
        board.placePiece(piece, new ChessPosition(row, column).toPosition());
        //.ToPosition() retorna um Position
        piecesOnTheBoard.add(piece);
    }


    private void initialSetup() { //Método para configurar as peças iniciais no tabuleiro
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }
}
