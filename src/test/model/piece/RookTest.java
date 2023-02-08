package model.piece;

import model.Colour;
import model.Move;
import model.board.Board;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RookTest {
    private Rook piece;
    private Board board;

    @BeforeEach
    public void init() {
        piece = new Rook(Colour.WHITE);
        board = new Board();
    }

    @Test
    public void initTest() {
        assertEquals(Colour.WHITE, piece.getColour());
        assertEquals("R", piece.getPrefix());
        assertFalse(piece.getHasMoved());
    }

    @Test
    public void getValidSquaresTest() {
        board.getSquare(4, 7).setPiece(new Pawn(Colour.WHITE));
        board.getSquare(0, 0).setPiece(new Pawn(Colour.BLACK));

        Set<Square> validSquares = piece.getValidSquares(board, board.getSquare(4, 0));

        assertEquals(13, validSquares.size());
        assertFalse(validSquares.contains(board.getSquare(4, 7)));
        assertTrue(validSquares.contains(board.getSquare(0, 1)));
    }

    @Test
    public void hasMovedTest() {
        board.doMove(new Move(board.getSquare(4, 1), board.getSquare(0, 1)));

        assertTrue(piece.getHasMoved());
    }
}
