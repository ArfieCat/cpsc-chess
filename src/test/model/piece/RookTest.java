package model.piece;

import model.Colour;
import model.board.Board;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code Rook}.
 */
public class RookTest {
    private Rook piece;
    private Board board;

    /**
     * @EFFECTS: Initializes the rook and board for testing.
     * @MODIFIES: {@code this}
     */
    @BeforeEach
    public void init() {
        piece = new Rook(Colour.WHITE);
        board = new Board();
    }

    /**
     * @EFFECTS: Tests {@code Rook.new}.
     */
    @Test
    public void initTest() {
        assertEquals(Colour.WHITE, piece.getColour());
        assertEquals("R", piece.getPrefix());
        assertFalse(piece.getHasMoved());
    }

    /**
     * @EFFECTS: Tests {@code Rook.getValidSquares}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void getValidSquaresTest() {
        board.getSquare(4, 7).setPiece(new Pawn(Colour.WHITE));
        board.getSquare(0, 0).setPiece(new Pawn(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(13, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(4, 7)));
        assertTrue(validSquares.contains(board.getSquare(0, 0)));
    }

    /**
     * @EFFECTS: Tests {@code Rook.getHasMoved} and {@code Rook.setHasMoved}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void hasMovedTest() {
        piece.setHasMoved();
        assertTrue(piece.getHasMoved());
    }
}
