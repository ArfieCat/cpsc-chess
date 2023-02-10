package model.piece;

import model.Colour;
import model.board.Board;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest {
    private King piece;
    private Board board;

    @BeforeEach
    public void init() {
        piece = new King(Colour.WHITE);
        board = new Board();
    }

    @Test
    public void initTest() {
        assertEquals(Colour.WHITE, piece.getColour());
        assertEquals("K", piece.getPrefix());
    }

    @Test
    public void getValidSquaresTest() {
        board.getSquare(5, 1).setPiece(new Pawn(Colour.WHITE));
        board.getSquare(3, 0).setPiece(new Pawn(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(4, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(5, 1)));
        assertTrue(validSquares.contains(board.getSquare(3, 0)));
    }

    // Successful king-side castle.
    @Test
    public void getValidSquaresTestCastle() {
        board.getSquare(7, 0).setPiece(new Rook(Colour.WHITE));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(6, validSquares.size());
        assertTrue(validSquares.contains(board.getSquare(6, 0)));
    }

    // Attempting to castle with a king that has already moved.
    @Test
    public void getValidSquaresTestCastleMovedKing() {
        piece.setHasMoved();

        board.getSquare(7, 0).setPiece(new Rook(Colour.WHITE));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(5, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(6, 0)));
    }

    // Attempting to castle with a rook that has already moved.
    @Test
    public void getValidSquaresTestCastleMovedRook() {
        Rook rook = new Rook(Colour.WHITE);
        rook.setHasMoved();

        board.getSquare(7, 0).setPiece(rook);
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(5, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(6, 0)));
    }

    // Attempting to castle with a piece that is not a rook.
    @Test
    public void getValidSquaresTestCastleWithoutRook() {
        board.getSquare(7, 0).setPiece(new Pawn(Colour.WHITE));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(5, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(6, 0)));
    }

    // Attempting to castle with a rook of the wrong colour.
    @Test
    public void getValidSquaresTestCastleWrongRook() {
        board.getSquare(7, 0).setPiece(new Rook(Colour.BLACK));
        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(5, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(6, 0)));
    }
}
