package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch {
    private boolean checkMate;
    private List<Piece> piecesOnTheBoard = new ArrayList<>(); //Peças que estão no tabuleiro
    private List<Piece> capturedPieces = new ArrayList<>(); //Peças capturadas
    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();

    }

    public int getTurn() {
        return turn;
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

        /*Caso o inimigo esteja em xeque, check vai ser true, caso contrario vai ser false
        opponent retorna a cor contraria, se eu passar branco ele retorna preto, currentPlayer = player turno atual
         */
        check = (testCheck(opponent(currentPlayer))) ? true : false;
        if (testCheckMate(opponent(currentPlayer))) { //Caso seja xeque mate  da cor contraria da atual
            //opponent retorna a cor contraria
            checkMate = true; //checkMate vai ser true
        } else { //se não, passa pro proximo turno
            nextTurn();
        }
        return (ChessPiece) capturedPiece;
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
        placeNewPiece('h', 7, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));

        placeNewPiece('b', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 8, new King(board, Color.BLACK));
    }
}
