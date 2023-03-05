package model.board;

import model.Colour;
import model.piece.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code Square}.
 */
public class SquareTest {
    private Square square;

    /**
     * @EFFECTS: Initializes the square for testing.
     * @MODIFIES: {@code this}
     */
    @BeforeEach
    public void init() {
        square = new Square(4, 1);
    }

    /**
     * @EFFECTS: Tests {@code Square.new}.
     */
    @Test
    public void initTest() {
        assertEquals(4, square.getX());
        assertEquals(1, square.getY());

        assertFalse(square.hasPiece());
        assertNull(square.getPiece());
    }

    /**
     * @EFFECTS: Tests {@code Square.setPiece}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void setPieceTest() {
        Pawn piece = new Pawn(Colour.WHITE);
        square.setPiece(piece);

        assertTrue(square.hasPiece());
        assertSame(piece, square.getPiece());
    }
}
