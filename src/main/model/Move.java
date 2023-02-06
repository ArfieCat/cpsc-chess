package model;

import model.board.Board;
import model.board.Square;
import model.pieces.Piece;

/**
 * Represents a move of a piece on the board from one square to another.
 */
public class Move {
    private final Square start;
    private final Piece startPiece;
    private final Square end;
    private final Piece endPiece;

    /**
     * EFFECTS: Constructs a new Move with given params.
     * REQUIRES: start.hasPiece()
     */
    public Move(Square start, Square end) {
        this.start = start;
        this.startPiece = start.getPiece();
        this.end = end;
        this.endPiece = end.getPiece();
    }

    /**
     * EFFECTS: Returns true if the move is valid.
     */
    public boolean isValid(Board board) {
        return start.getPiece().getValidSquares(board, start).contains(end);
    }

    public Square getStart() {
        return start;
    }

    public Piece getStartPiece() {
        return startPiece;
    }

    public Square getEnd() {
        return end;
    }

    public Piece getEndPiece() {
        return endPiece;
    }
}
