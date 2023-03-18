package model.piece;

import model.Colour;
import model.board.Board;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code Queen}.
 */
public class QueenTest {
    private Queen piece;
    private Board board;

    /**
     * @EFFECTS: Initializes the queen and board for testing.
     * @MODIFIES: {@code this}
     */
    @BeforeEach
    public void init() {
        piece = new Queen(Colour.WHITE);
        board = new Board();

        // Clear the board to allow for custom positions.
        for (int i = 0; i < Board.SIZE * Board.SIZE; i++) {
            board.getSquare(i % Board.SIZE, i / Board.SIZE).setPiece(null);
        }
    }

    /**
     * @EFFECTS: Tests {@code Queen.new}.
     */
    @Test
    public void initTest() {
        assertEquals(Colour.WHITE, piece.getColour());
        assertEquals("Q", piece.getPrefix());
    }

    /**
     * @EFFECTS: Tests {@code Queen.getValidSquares}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void getValidSquaresTest() {
        board.getSquare(7, 3).setPiece(new Pawn(Colour.WHITE));
        board.getSquare(0, 4).setPiece(new Pawn(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(20, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(7, 3)));
        assertTrue(validSquares.contains(board.getSquare(0, 4)));
    }
}
