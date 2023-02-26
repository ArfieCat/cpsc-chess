package model;

import model.board.Board;
import model.board.Square;

/**
 * Represents a move of a piece on the board from one square to another.
 */
public class Move {
    private final Square start;
    private final Square end;

    /**
     * @EFFECTS: Constructs a new Move with given params.
     * @REQUIRES: {@code start.hasPiece()}
     */
    public Move(Square start, Square end) {
        this.start = start;
        this.end = end;
    }

    /**
     * @EFFECTS: Returns {@code true} if the start piece can move to the end square.
     */
    public boolean isValid(Board board) {
        return start.getPiece().getValidSquares(board, start).contains(end);
    }

    public Square getStart() {
        return start;
    }

    public Square getEnd() {
        return end;
    }
}
