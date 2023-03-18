package model.piece;

import model.Colour;
import model.board.Board;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code Pawn}.
 */
public class PawnTest {
    private Pawn piece;
    private Board board;

    /**
     * @EFFECTS: Initializes the pawn and board for testing.
     * @MODIFIES: {@code this}
     */
    @BeforeEach
    public void init() {
        piece = new Pawn(Colour.WHITE);
        board = new Board();

        // Clear the board to allow for custom positions.
        for (int i = 0; i < Board.SIZE * Board.SIZE; i++) {
            board.getSquare(i % Board.SIZE, i / Board.SIZE).setPiece(null);
        }
    }

    /**
     * @EFFECTS: Tests {@code Pawn.new}.
     */
    @Test
    public void initTest() {
        assertEquals(Colour.WHITE, piece.getColour());
        assertEquals("", piece.getPrefix());
    }

    /**
     * @EFFECTS: Tests {@code Pawn.getValidSquares}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void getValidSquaresTest() {
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));
        assertEquals(2, validSquares.size());
        assertTrue(validSquares.contains(board.getSquare(4, 2)));
    }

    /**
     * @EFFECTS: Tests {@code Pawn.getValidSquares} by trying to move two squares with a pawn that has already moved.
     * @MODIFIES: {@code this}
     */
    @Test
    public void getValidSquaresTestMoved() {
        piece.setHasMoved();
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(1, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(4, 2)));
    }

    /**
     * @EFFECTS: Tests {@code Pawn.getValidSquares} by trying to move two squares with a pawn that is blocked by another
     * piece from the front.
     * @MODIFIES: {@code this}
     */
    @Test
    public void getValidSquaresTestBlocked() {
        board.getSquare(4, 2).setPiece(new Pawn(Colour.WHITE));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(1, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(4, 2)));
    }

    /**
     * @EFFECTS: Tests {@code Pawn.getValidSquares} by trying to move a pawn that is completely blocked from the front.
     * @MODIFIES: {@code this}
     */
    @Test
    public void getValidSquaresTestVeryBlocked() {
        board.getSquare(4, 1).setPiece(new Pawn(Colour.WHITE));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));
        assertEquals(0, validSquares.size());
    }

    /**
     * @EFFECTS: Tests {@code Pawn.getValidSquares} by trying to move a pawn off the board (should never happen).
     */
    @Test
    public void getValidSquaresTestOutOfBounds() {
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 6));
        assertEquals(1, validSquares.size());
        assertTrue(validSquares.contains(board.getSquare(4, 7)));

        piece.setHasMoved();
        validSquares = piece.getValidSquares(board, board.getSquare(4, 7));
        assertEquals(0, validSquares.size());
    }

    /**
     * @EFFECTS: Tests {@code Pawn.addCaptureSquares}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void addCaptureSquaresTest() {
        board.getSquare(5, 1).setPiece(new Pawn(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(3, validSquares.size());
        assertTrue(validSquares.contains(board.getSquare(5, 1)));
    }

    /**
     * @EFFECTS: Tests {@code Pawn.addCaptureSquares} by trying to capture a piece of the wrong colour.
     * @MODIFIES: {@code this}
     */
    @Test
    public void addCaptureSquaresTestWrongPiece() {
        board.getSquare(5, 1).setPiece(new Pawn(Colour.WHITE));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(2, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(5, 1)));
    }

    /**
     * @EFFECTS: Tests {@code Pawn.addCaptureSquares} by capturing en passant.
     * @MODIFIES: {@code this}
     */
    @Test
    public void addCaptureSquaresTestEnPassant() {
        Pawn pawn = new Pawn(Colour.BLACK);
        pawn.setEnPassable(true);

        board.getSquare(5, 0).setPiece(pawn);
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(3, validSquares.size());
        assertTrue(validSquares.contains(board.getSquare(5, 1)));
    }

    /**
     * @EFFECTS: Tests {@code Pawn.addCaptureSquares} by trying to capture a piece that is not a pawn en passant.
     * @MODIFIES: {@code this}
     */
    @Test
    public void addCaptureSquaresTestEnPassantWithoutPawn() {
        board.getSquare(5, 0).setPiece(new Rook(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(2, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(5, 1)));
    }

    /**
     * @EFFECTS: Tests {@code Pawn.addCaptureSquares} by trying to capture a pawn that cannot be captured en passant...
     * en passant.
     * @MODIFIES: {@code this}
     */
    @Test
    public void addCaptureSquaresTestEnPassantWrongPawn() {
        board.getSquare(5, 0).setPiece(new Pawn(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(2, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(5, 1)));
    }

    /**
     * @EFFECTS: Tests {@code Pawn.addCaptureSquares} by trying to capture a piece of the wrong colour en passant.
     * @MODIFIES: {@code this}
     */
    @Test
    public void addCaptureSquaresTestEnPassantWrongPiece() {
        Pawn pawn = new Pawn(Colour.WHITE);
        pawn.setEnPassable(true);

        board.getSquare(5, 1).setPiece(pawn);
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(2, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(5, 1)));
    }
}
