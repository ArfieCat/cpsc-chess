package model.piece;

import model.Colour;
import model.board.Board;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code Bishop}.
 */
public class BishopTest {
    private Bishop piece;
    private Board board;

    /**
     * @EFFECTS: Initializes the bishop and board for testing.
     * @MODIFIES: {@code this}
     */
    @BeforeEach
    public void init() {
        piece = new Bishop(Colour.WHITE);
        board = new Board();
    }

    /**
     * @EFFECTS: Tests {@code Bishop.new}.
     */
    @Test
    public void initTest() {
        assertEquals(Colour.WHITE, piece.getColour());
        assertEquals("B", piece.getPrefix());
    }

    /**
     * @EFFECTS: Tests {@code Bishop.getValidSquares}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void getValidSquaresTest() {
        board.getSquare(7, 3).setPiece(new Pawn(Colour.WHITE));
        board.getSquare(0, 4).setPiece(new Pawn(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(6, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(7, 3)));
        assertTrue(validSquares.contains(board.getSquare(0, 4)));
    }
}
