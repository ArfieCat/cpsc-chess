package model.board;

import model.Colour;
import model.Move;
import model.piece.Pawn;
import model.piece.Piece;
import model.piece.Queen;
import model.piece.Rook;
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
    }

    /**
     * @EFFECTS: Tests {@code Board.new}.
     */
    @Test
    public void initTest() {
        for (int x = 0; x < Board.SIZE; x++) {
            assertTrue(board.getSquare(x, 0).hasPiece());
            assertTrue(board.getSquare(x, 1).getPiece() instanceof Pawn);
        }

        assertEquals(32, board.getVisibleSquares(board.getCurrentPlayer()).size());
        assertEquals(Colour.WHITE, board.getCurrentPlayer());

        assertEquals(0, board.getHistory().size());
        assertFalse(board.isGameOver());
    }

    /**
     * @EFFECTS: Tests {@code Board.doMove}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doMoveTest() {
        Move move = new Move(board.getSquare(6, 0), board.getSquare(5, 2));
        Piece piece = move.getStart().getPiece();

        board.doMove(move);
        assertFalse(move.getStart().hasPiece());
        assertSame(piece, move.getEnd().getPiece());

        assertEquals(1, board.getHistory().size());
        assertSame(move, board.getHistory().get(0));
        assertFalse(board.isGameOver());
    }

    /**
     * @EFFECTS: Tests {@code Board.doMove} by making a move that ends the game.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doMoveTestGameOver() {
        // This move is actually super illegal, but we're not testing that right now.
        board.doMove(new Move(board.getSquare(4, 0), board.getSquare(4, 7)));
        assertTrue(board.isGameOver());
    }

    /**
     * @EFFECTS: Tests {@code Board.doEnPassant}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doEnPassantTest() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));

        board.doMove(new Move(board.getSquare(4, 1), board.getSquare(4, 3)));
        assertTrue(((Pawn) board.getSquare(4, 3).getPiece()).getEnPassable());

        board.doMove(new Move(board.getSquare(3, 3), board.getSquare(4, 2)));
        assertFalse(board.getSquare(4, 3).hasPiece());
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
     * adjacent square.
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
     * passant on the adjacent square.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doEnPassantTestWrongPawn() {
        board.getSquare(3, 3).setPiece(new Pawn(Colour.BLACK));
        board.getSquare(4, 3).setPiece(new Pawn(Colour.WHITE));

        board.doMove(new Move(board.getSquare(4, 1), board.getSquare(4, 2)));
        board.doMove(new Move(board.getSquare(3, 3), board.getSquare(4, 2)));
        assertTrue(board.getSquare(4, 3).hasPiece());
    }

    /**
     * @EFFECTS: Tests {@code Board.doEnPassant} by trying to capture en passant with a pawn of the wrong colour on the
     * adjacent square.
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
     * can no longer be captured en passant on the adjacent square.
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

        board.doMove(new Move(board.getSquare(4, 6), board.getSquare(4, 7)));
        assertTrue(board.getSquare(4, 7).getPiece() instanceof Queen);
    }

    /**
     * @EFFECTS: Tests {@code Board.doPromotion} by trying to promote a pawn of the wrong colour.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doPromotionTestWrongPawn() {
        board.getSquare(4, 6).setPiece(new Pawn(Colour.BLACK));

        // This move is also super illegal.
        board.doMove(new Move(board.getSquare(4, 6), board.getSquare(4, 7)));
        assertTrue(board.getSquare(4, 7).getPiece() instanceof Pawn);
    }

    /**
     * @EFFECTS: Tests {@code Board.doCastling} kingside.
     * @MODIFIES: {@code this}
     */
    @Test
    public void doCastlingTestKingSide() {
        board.getSquare(5, 0).setPiece(null);
        board.getSquare(6, 0).setPiece(null);

        board.doMove(new Move(board.getSquare(4, 0), board.getSquare(6, 0)));
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

        board.doMove(new Move(board.getSquare(4, 0), board.getSquare(2, 0)));
        assertFalse(board.getSquare(0, 0).hasPiece());
        assertTrue(board.getSquare(3, 0).getPiece() instanceof Rook);
    }
}
