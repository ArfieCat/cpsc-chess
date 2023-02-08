package model.piece;

import model.Colour;
import model.board.Board;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BishopTest {
    private Bishop piece;
    private Board board;

    @BeforeEach
    public void init() {
        piece = new Bishop(Colour.WHITE);
        board = new Board();
    }

    @Test
    public void initTest() {
        assertEquals(Colour.WHITE, piece.getColour());
        assertEquals("B", piece.getPrefix());
    }

    @Test
    public void getValidSquaresTest() {
        board.getSquare(7, 3).setPiece(new Pawn(Colour.WHITE));
        board.getSquare(0, 4).setPiece(new Pawn(Colour.BLACK));

        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(8, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(7, 3)));
        assertTrue(validSquares.contains(board.getSquare(0, 4)));
    }
}
