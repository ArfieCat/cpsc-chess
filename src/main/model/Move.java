package model;

import model.board.Board;
import model.board.Square;
import model.pieces.Piece;

/**
 * Represents a move of a piece on the board from one square to another.
 */
public class Move {
    private final Piece piece;
    private final Square start;
    private final Square end;

    /**
     * EFFECTS: Constructs a new Move with given params.
     */
    public Move(Piece piece, Square from, Square to) {
        this.piece = piece;
        this.start = from;
        this.end = to;
    }

    /**
     * EFFECTS: Returns true if the move is valid.
     */
    public boolean isValid(Board board) {
        return piece.isValidMove(board, start, end);
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getStart() {
        return start;
    }

    public Square getEnd() {
        return end;
    }
}
