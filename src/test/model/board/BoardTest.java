package model.board;

import model.Colour;
import model.Move;
import model.piece.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void init() {
        board = new Board();
        board.setupPieces(Colour.WHITE);
    }

    @Test
    public void initTest() {
        for (int x = 0; x < 8; x++) {
            assertTrue(board.getSquare(x, 0).hasPiece());
            assertTrue(board.getSquare(x, 1).getPiece() instanceof Pawn);
        }
    }

    @Test
    public void doMoveTest() {
        Move move = new Move(board.getSquare(6, 0), board.getSquare(5, 2));
        assertFalse(board.doMove(move));
        assertFalse(move.getStart().hasPiece());
        assertSame(move.getStartPiece(), move.getEnd().getPiece());
    }

    @Test
    public void doMoveTestGameOver() {
        // This move is actually super illegal, but we're not testing that right now.
        board.getSquare(4, 7).setPiece(new King(Colour.BLACK));
        Move move = new Move(board.getSquare(4, 0), board.getSquare(4, 7));
        assertTrue(board.doMove(move));
    }

    @Test
    public void doEnPassantTest() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        Move setup = new Move(board.getSquare(4, 1), board.getSquare(4, 3));
        Move move = new Move(board.getSquare(3, 3), board.getSquare(4, 2));

        board.doMove(setup);
        assertTrue(((Pawn) setup.getStartPiece()).getEnPassable());

        board.doMove(move);
        assertFalse(setup.getEnd().hasPiece());
        assertSame(move.getStartPiece(), move.getEnd().getPiece());
    }

    @Test
    public void doEnPassantTestWithoutPiece() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        Move setup = new Move(board.getSquare(4, 1), board.getSquare(4, 2));
        Move move = new Move(board.getSquare(3, 3), board.getSquare(4, 2));

        board.doMove(setup);
        assertFalse(board.getSquare(4, 3).hasPiece());

        board.doMove(move);
        assertFalse(board.getSquare(4, 3).hasPiece());
    }

    @Test
    public void doEnPassantTestWithoutPawn() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        board.getSquare(4, 3).setPiece(new Rook(Colour.WHITE));
        Move setup = new Move(board.getSquare(4, 1), board.getSquare(4, 2));
        Move move = new Move(board.getSquare(3, 3), board.getSquare(4, 2));

        board.doMove(setup);
        assertTrue(board.getSquare(4, 3).hasPiece());

        board.doMove(move);
        assertTrue(board.getSquare(4, 2).hasPiece());
    }

    @Test
    public void doEnPassantTestWrongPawn() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        board.getSquare(4, 3).setPiece(new Pawn(Colour.WHITE));
        Move setup = new Move(board.getSquare(4, 1), board.getSquare(4, 2));
        Move move = new Move(board.getSquare(3, 3), board.getSquare(4, 2));

        board.doMove(setup);
        assertTrue(board.getSquare(4, 3).hasPiece());

        board.doMove(move);
        assertTrue(board.getSquare(4, 2).hasPiece());
    }

    @Test
    public void doEnPassantTestWrongPiece() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        board.getSquare(4, 3).setPiece(new Pawn(Colour.BLACK));
        Move setup = new Move(board.getSquare(4, 1), board.getSquare(4, 2));
        Move move = new Move(board.getSquare(3, 3), board.getSquare(4, 2));

        board.doMove(setup);
        assertTrue(board.getSquare(4, 3).hasPiece());

        board.doMove(move);
        assertTrue(board.getSquare(4, 2).hasPiece());
    }

    @Test
    public void doPromotionTest() {
        board.getSquare(4, 6).setPiece(new Pawn(Colour.WHITE));
        Move move = new Move(board.getSquare(4, 6), board.getSquare(4, 7));

        board.doMove(move);
        assertTrue(move.getEnd().getPiece() instanceof Queen);
    }

    @Test
    public void doPromotionTestWrongPawn() {
        // This move is also super illegal.
        board.getSquare(4, 6).setPiece(new Pawn(Colour.BLACK));
        Move move = new Move(board.getSquare(4, 6), board.getSquare(4, 7));

        board.doMove(move);
        assertTrue(move.getEnd().getPiece() instanceof Pawn);
    }

    @Test
    public void doCastlingTestKingSide() {
        board.getSquare(5, 0).setPiece(null);
        board.getSquare(6, 0).setPiece(null);
        Move move = new Move(board.getSquare(4, 0), board.getSquare(6, 0));

        board.doMove(move);
        assertFalse(board.getSquare(7, 0).hasPiece());
        assertTrue(board.getSquare(5, 0).getPiece() instanceof Rook);
    }

    @Test
    public void doCastlingTestQueenSide() {
        board.getSquare(3, 0).setPiece(null);
        board.getSquare(2, 0).setPiece(null);
        board.getSquare(1, 0).setPiece(null);
        Move move = new Move(board.getSquare(4, 0), board.getSquare(2, 0));

        board.doMove(move);
        assertFalse(board.getSquare(0, 0).hasPiece());
        assertTrue(board.getSquare(3, 0).getPiece() instanceof Rook);
    }

    @Test
    public void getDisplayStringTest() {
        // Wayward Queen attack.
        board.setupPieces(Colour.BLACK);
        board.doMove(new Move(board.getSquare(4, 1), board.getSquare(4, 3)));
        board.doMove(new Move(board.getSquare(4, 6), board.getSquare(4, 4)));
        board.doMove(new Move(board.getSquare(3, 0), board.getSquare(7, 4)));

        String string = "8                          \n"
                + "7                 p     p  \n"
                + "6  .                 .  .  \n"
                + "5     .        p  .  .  Q  \n"
                + "4  .  .  .  .  P  .  .  .  \n"
                + "3  .  .  .  .     .  .  .  \n"
                + "2  P  P  P  P  .  P  P  P  \n"
                + "1  R  N  B  .  K  B  N  R  \n"
                + "   a  b  c  d  e  f  g  h";
        assertEquals(string, board.getDisplayString(Colour.WHITE));
    }

    @Test
    public void clearPiecesTest() {
        Pawn pawn = new Pawn(Colour.BLACK);
        board.getSquare(4, 3).setPiece(new Pawn(Colour.WHITE));
        board.getSquare(3, 4).setPiece(pawn);
        board.setupPieces(Colour.WHITE);

        assertFalse(board.getSquare(4, 3).hasPiece());
        assertSame(pawn, board.getSquare(3, 4).getPiece());
    }
}
