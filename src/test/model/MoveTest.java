package model;

import model.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {
    private Move move;
    private Move invalidMove;
    private Board board;

    @BeforeEach
    public void init() {
        board = new Board();
        board.setupPieces(Colour.WHITE);

        move = new Move(board.getSquare(4, 1), board.getSquare(4, 3));
        invalidMove = new Move(board.getSquare(4, 0), board.getSquare(4, 1));
    }

    @Test
    public void initTest() {
        assertTrue(move.isValid(board));
        assertFalse(invalidMove.isValid(board));

        assertSame(board.getSquare(4, 1), move.getStart());
        assertSame(board.getSquare(4, 3), move.getEnd());

        assertSame(board.getSquare(4, 0), invalidMove.getStart());
        assertSame(board.getSquare(4, 1), invalidMove.getEnd());
    }
}
