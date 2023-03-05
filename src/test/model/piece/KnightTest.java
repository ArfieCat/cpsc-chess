package model.piece;

import model.Colour;
import model.board.Board;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code Knight}.
 */
public class KnightTest {
    private Knight piece;
    private Board board;

    /**
     * @EFFECTS: Initializes the knight and board for testing.
     * @MODIFIES: {@code this}
     */
    @BeforeEach
    public void init() {
        piece = new Knight(Colour.WHITE);
        board = new Board();
    }

    /**
     * @EFFECTS: Tests {@code Knight.new}.
     */
    @Test
    public void initTest() {
        assertEquals(Colour.WHITE, piece.getColour());
        assertEquals("N", piece.getPrefix());
    }

    /**
     * @EFFECTS: Tests {@code Knight.getValidSquares}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void getValidSquaresTest() {
        board.getSquare(6, 1).setPiece(new Pawn(Colour.WHITE));
        board.getSquare(3, 2).setPiece(new Pawn(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(3, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(6, 1)));
        assertTrue(validSquares.contains(board.getSquare(3, 2)));
    }
}
