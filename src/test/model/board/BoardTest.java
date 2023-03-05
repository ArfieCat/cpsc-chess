package model.board;

import model.Colour;
import model.Move;
import model.piece.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code Board}.
 */
public class BoardTest {
    private Board board;

    /**
     * @EFFECTS: Initializes the board for testing.
     * @MODIFIES: {@code this}
     */
    @BeforeEach
    public void init() {
        board = new Board();
        board.setupPieces(Colour.WHITE);
    }

    /**
     * @EFFECTS: Tests {@code Board.new} and {@code Board.setupPieces}.
     */
    @Test
    public void initTest() {
        for (int x = 0; x < 8; x++) {
            assertTrue(board.getSquare(x, 0).hasPiece());
            assertTrue(board.getSquare(x, 1).getPiece() instanceof Pawn);
        }
        assertEquals(0, board.getHistory().size());
    }

    /**
     * @EFFECTS: Tests {@code Board.doMove}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doMoveTest() {
        Move move = new Move(board.getSquare(6, 0), board.getSquare(5, 2));
        Piece piece = move.getStart().getPiece();

        assertFalse(board.doMove(move));
        assertFalse(move.getStart().hasPiece());
        assertSame(piece, move.getEnd().getPiece());

        assertEquals(1, board.getHistory().size());
        assertSame(move, board.getHistory().get(0));
    }

    /**
     * @EFFECTS: Tests {@code Board.doMove} by making a move that ends the game.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doMoveTestGameOver() {
        // This move is actually super illegal, but we're not testing that right now.
        board.getSquare(4, 7).setPiece(new King(Colour.BLACK));
        Move move = new Move(board.getSquare(4, 0), board.getSquare(4, 7));
        assertTrue(board.doMove(move));
    }

    /**
     * @EFFECTS: Tests {@code Board.doEnPassant}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doEnPassantTest() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        Move setup = new Move(board.getSquare(4, 1), board.getSquare(4, 3));
        Move move = new Move(board.getSquare(3, 3), board.getSquare(4, 2));

        board.doMove(setup);
        assertTrue(((Pawn) setup.getEnd().getPiece()).getEnPassable());

        board.doMove(move);
        assertFalse(setup.getEnd().hasPiece());
    }

    /**
     * @EFFECTS: Tests {@code Board.doEnPassant} by trying to capture en passant with nothing on the adjacent square.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doEnPassantTestWithoutPiece() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        board.doMove(new Move(board.getSquare(4, 1), board.getSquare(4, 2)));
        board.doMove(new Move(board.getSquare(3, 3), board.getSquare(4, 2)));
        assertFalse(board.getSquare(4, 3).hasPiece());
    }

    /**
     * @EFFECTS: Tests {@code Board.doEnPassant} by trying to capture en passant with a piece that is not a pawn on the
     *           adjacent square.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doEnPassantTestWithoutPawn() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        board.getSquare(4, 3).setPiece(new Rook(Colour.WHITE));

        board.doMove(new Move(board.getSquare(4, 1), board.getSquare(4, 2)));
        board.doMove(new Move(board.getSquare(3, 3), board.getSquare(4, 2)));
        assertTrue(board.getSquare(4, 3).hasPiece());
    }

    /**
     * @EFFECTS: Tests {@code Board.doEnPassant} by trying to capture en passant with a pawn that cannot be captured en
     *           passant on the adjacent square.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doEnPassantTestWrongPawn() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        board.getSquare(4, 3).setPiece(new Pawn(Colour.WHITE));
        Move setup = new Move(board.getSquare(4, 1), board.getSquare(4, 2));
        Move move = new Move(board.getSquare(3, 3), board.getSquare(4, 2));

        board.doMove(setup);
        board.doMove(move);
        assertTrue(board.getSquare(4, 3).hasPiece());
    }

    /**
     * @EFFECTS: Tests {@code Board.doEnPassant} by trying to capture en passant with a pawn of the wrong colour on the
     *           adjacent square.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doEnPassantTestWrongPiece() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        board.getSquare(4, 3).setPiece(new Pawn(Colour.BLACK));

        board.doMove(new Move(board.getSquare(4, 1), board.getSquare(4, 2)));
        board.doMove(new Move(board.getSquare(3, 3), board.getSquare(4, 2)));
        assertTrue(board.getSquare(4, 2).hasPiece());
    }

    /**
     * @EFFECTS: Tests {@code Board.doEnPassant} by trying to capture en passant with a pawn that was previously, but
     *           can no longer be captured en passant on the adjacent square.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doEnPassantTestDisabled() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        board.doMove(new Move(board.getSquare(4, 1), board.getSquare(4, 3)));
        board.doMove(new Move(board.getSquare(4, 3), board.getSquare(4, 2)));
        board.doMove(new Move(board.getSquare(3, 3), board.getSquare(4, 2)));
        assertTrue(board.getSquare(4, 2).hasPiece());
    }

    /**
     * @EFFECTS: Tests {@code Board.doPromotion}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doPromotionTest() {
        board.getSquare(4, 6).setPiece(new Pawn(Colour.WHITE));
        Move move = new Move(board.getSquare(4, 6), board.getSquare(4, 7));

        board.doMove(move);
        assertTrue(move.getEnd().getPiece() instanceof Queen);
    }

    /**
     * @EFFECTS: Tests {@code Board.doPromotion} by trying to promote a pawn of the wrong colour.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doPromotionTestWrongPawn() {
        // This move is also super illegal.
        board.getSquare(4, 6).setPiece(new Pawn(Colour.BLACK));
        Move move = new Move(board.getSquare(4, 6), board.getSquare(4, 7));

        board.doMove(move);
        assertTrue(move.getEnd().getPiece() instanceof Pawn);
    }

    /**
     * @EFFECTS: Tests {@code Board.doCastling} kingside.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doCastlingTestKingSide() {
        board.getSquare(5, 0).setPiece(null);
        board.getSquare(6, 0).setPiece(null);
        Move move = new Move(board.getSquare(4, 0), board.getSquare(6, 0));

        board.doMove(move);
        assertFalse(board.getSquare(7, 0).hasPiece());
        assertTrue(board.getSquare(5, 0).getPiece() instanceof Rook);
    }

    /**
     * @EFFECTS: Tests {@code Board.doCastling} queenside.
     * @MODIFIES: {@code this}
     */
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

    /**
     * @EFFECTS: Tests {@code Board.getDisplayString} for WHITE.
     * @MODIFIES: {@code this}
     */
    @Test
    public void getDisplayStringTestWhite() {
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
                + "   a  b  c  d  e  f  g  h  ";
        assertEquals(string, board.getDisplayString(Colour.WHITE));
    }

    /**
     * @EFFECTS: Tests {@code Board.getDisplayString} for BLACK.
     * @MODIFIES: {@code this}
     */
    @Test
    public void getDisplayStringTestBlack() {
        board.setupPieces(Colour.BLACK);
        board.doMove(new Move(board.getSquare(4, 1), board.getSquare(4, 3)));
        board.doMove(new Move(board.getSquare(4, 6), board.getSquare(4, 4)));
        board.doMove(new Move(board.getSquare(3, 0), board.getSquare(7, 4)));

        String string = "1                          \n"
                + "2                          \n"
                + "3                       .  \n"
                + "4  .                 .     \n"
                + "5     .  .  P  .  .  .  .  \n"
                + "6  .  .  .     .  .  .  .  \n"
                + "7  P  P  P  .  P  P  P  P  \n"
                + "8  R  N  B  K  Q  B  N  R  \n"
                + "   h  g  f  e  d  c  b  a  ";
        assertEquals(string, board.getDisplayString(Colour.BLACK));
    }

    /**
     * @EFFECTS: Tests {@code Board.clearPieces}.
     * @MODIFIES: {@code this}
     */
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
