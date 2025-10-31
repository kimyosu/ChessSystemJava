package chess;

import boardgame.BoardException;

import java.io.Serial;
import java.io.Serializable;

public class ChessException extends BoardException implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public ChessException(String message) {
        super(message);
    }
}
