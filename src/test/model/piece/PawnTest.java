package model.piece;

import model.Colour;
import model.board.Board;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {
    private Pawn piece;
    private Board board;

    @BeforeEach
    public void init() {
        piece = new Pawn(Colour.WHITE);
        board = new Board();
    }

    @Test
    public void initTest() {
        assertEquals(Colour.WHITE, piece.getColour());
        assertEquals("P", piece.getPrefix());
    }

    @Test
    public void getValidSquaresTest() {
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));
        assertEquals(2, validSquares.size());
        assertTrue(validSquares.contains(board.getSquare(4, 2)));
    }

    // Successful capture.
    @Test
    public void getValidSquaresTestCapture() {
        board.getSquare(5, 1).setPiece(new Pawn(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(3, validSquares.size());
        assertTrue(validSquares.contains(board.getSquare(5, 1)));
    }

    // Successful capture en passant.
    @Test
    public void getValidSquaresTestCaptureEnPassant() {
        Pawn pawn = new Pawn(Colour.BLACK);
        pawn.setEnPassable(true);

        board.getSquare(5, 0).setPiece(pawn);
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(3, validSquares.size());
        assertTrue(validSquares.contains(board.getSquare(5, 1)));
    }

    // Attempting to move two squares with a pawn that has already moved.
    @Test
    public void getValidSquaresTestMoved() {
        piece.setHasMoved();
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(1, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(4, 2)));
    }

    // Attempting to move a pawn that is blocked from the front.
    @Test
    public void getValidSquaresTestBlocked() {
        board.getSquare(4, 1).setPiece(new Pawn(Colour.WHITE));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));
        assertEquals(0, validSquares.size());
    }

    // Attempting to move a pawn already on its last rank (should never happen).
    @Test
    public void getValidSquaresTestOutOfBounds() {
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 7));
        assertEquals(0, validSquares.size());
    }

    // Attempting to capture a piece that is not a pawn en passant.
    @Test
    public void getValidSquaresTestCaptureEnPassantWithoutPawn() {
        board.getSquare(5, 0).setPiece(new Rook(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(2, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(5, 1)));
    }

    // Attempting to capture a non-en-passable pawn en passant.
    @Test
    public void getValidSquaresTestCaptureWrongPawn() {
        board.getSquare(5, 0).setPiece(new Pawn(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(2, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(5, 1)));
    }

    // Attempting to capture a piece of the wrong colour.
    @Test
    public void getValidSquaresTestCaptureWrongPiece() {
        board.getSquare(5, 1).setPiece(new Pawn(Colour.WHITE));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(2, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(5, 1)));
    }

    // Attempting to capture a piece of the wrong colour en passant.
    @Test
    public void getValidSquaresTestCaptureEnPassantWrongPiece() {
        Pawn pawn = new Pawn(Colour.WHITE);
        pawn.setEnPassable(true);

        board.getSquare(5, 1).setPiece(new Pawn(Colour.WHITE));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(2, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(5, 1)));
    }
}
