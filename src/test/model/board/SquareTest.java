package model.board;

import model.Colour;
import model.piece.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SquareTest {
    private Square square;

    @BeforeEach
    public void init() {
        square = new Square(4, 1);
    }

    @Test
    public void initTest() {
        assertEquals(4, square.getX());
        assertEquals(1, square.getY());

        assertFalse(square.hasPiece());
        assertNull(square.getPiece());
    }

    @Test
    public void setPieceTest() {
        Pawn piece = new Pawn(Colour.WHITE);
        square.setPiece(piece);

        assertTrue(square.hasPiece());
        assertSame(piece, square.getPiece());
    }
}
