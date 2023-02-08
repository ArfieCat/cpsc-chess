package model.piece;

import model.Colour;
import model.board.Board;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class QueenTest {
    private Queen piece;
    private Board board;

    @BeforeEach
    public void init() {
        piece = new Queen(Colour.WHITE);
        board = new Board();
    }

    @Test
    public void initTest() {
        assertEquals(Colour.WHITE, piece.getColour());
        assertEquals("Q", piece.getPrefix());
    }

    @Test
    public void getValidSquaresTest() {
        board.getSquare(7, 3).setPiece(new Pawn(Colour.WHITE));
        board.getSquare(4, 7).setPiece(new Pawn(Colour.WHITE));
        board.getSquare(0, 4).setPiece(new Pawn(Colour.BLACK));
        board.getSquare(0, 0).setPiece(new Pawn(Colour.BLACK));

        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(19, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(7, 3)));
        assertFalse(validSquares.contains(board.getSquare(4, 7)));
        assertTrue(validSquares.contains(board.getSquare(0, 4)));
        assertTrue(validSquares.contains(board.getSquare(0, 0)));
    }
}
