package model;

import model.board.Board;
import model.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code Move}.
 */
public class MoveTest {
    private Board board;
    private Move move;

    /**
     * @EFFECTS: Initializes the moves and board for testing.
     * @MODIFIES: {@code this}
     */
    @BeforeEach
    public void init() {
        board = new Board();
        move = new Move(board.getSquare(4, 1), board.getSquare(4, 3));
    }

    /**
     * @EFFECTS: Tests {@code Move.new}.
     */
    @Test
    public void initTest() {
        assertSame(board.getSquare(4, 1), move.getStart());
        assertSame(board.getSquare(4, 3), move.getEnd());

        assertNull(move.getMovedPiece());
        assertFalse(move.getFlag(Move.CAPTURE));
        assertFalse(move.getFlag(Move.CASTLE));
        assertFalse(move.getFlag(Move.PROMOTE));
    }

    /**
     * @EFFECTS: Tests {@code Move.isValid}.
     */
    @Test
    public void isValidTest() {
        Piece piece = move.getStart().getPiece();
        assertTrue(move.isValid(board));

        board.doMove(move);
        assertSame(piece, move.getMovedPiece());
    }

    /**
     * @EFFECTS: Tests {@code Move.setFlag}.
     */
    @Test
    public void setFlagTest() {
        move.setFlag(Move.CAPTURE);
        move.setFlag(Move.CASTLE);
        move.setFlag(Move.PROMOTE);

        assertTrue(move.getFlag(Move.CAPTURE));
        assertTrue(move.getFlag(Move.CASTLE));
        assertTrue(move.getFlag(Move.PROMOTE));
    }
}
