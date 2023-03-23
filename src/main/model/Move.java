package model;

import model.board.Board;
import model.board.Square;
import model.piece.Piece;

/**
 * Represents a move of a piece on the board from one square to another.
 */
public class Move {
    public static final int CAPTURE = 0x1;
    public static final int CASTLE = 0x2;
    public static final int PROMOTE = 0x4;

    private final Square start;
    private final Square end;
    private Piece movedPiece;
    private int flags;

    /**
     * @EFFECTS: Constructs a new move with the given params.
     * @REQUIRES: {@code start.hasPiece()}
     */
    public Move(Square start, Square end) {
        this.start = start;
        this.end = end;
        this.movedPiece = null;
        this.flags = 0x0;
    }

    /**
     * @EFFECTS: Returns {@code true} if the start piece can move to the end square.
     * @REQUIRES: before {@code Board.doMove(this)}
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

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public void setMovedPiece(Piece to) {
        movedPiece = to;
    }

    public boolean getFlag(int flag) {
        return (flags & flag) != 0;
    }

    public void setFlag(int flag) {
        flags |= flag;
    }
}
