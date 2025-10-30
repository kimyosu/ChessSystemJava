import chess.ChessPiece;

public class UI {
    public static void printBoard(ChessPiece[][] pieces) {

        for (int i = 0; i < pieces.length; i++){
            //8 - i vai diminuindo de 8 até 1
            System.out.print(8 - i + " ");
            for (int j = 0; j < pieces[i].length; j++){
                printOnePiece(pieces[i][j]); //imprime uma peça por vez
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h" );
    }

    private static void printOnePiece(ChessPiece piece) {
        if (piece == null){
            System.out.print("-");
        }else {
            System.out.println(piece);
        }
        System.out.print(" ");
    }
}
