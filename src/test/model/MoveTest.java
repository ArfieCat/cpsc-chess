package model;

import model.board.Board;
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

        assertTrue(move.isValid(board));
        assertFalse(new Move(board.getSquare(4, 0), board.getSquare(4, 1)).isValid(board));
    }
}
