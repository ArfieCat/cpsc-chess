package model.piece;

import model.Colour;
import model.board.Board;
import model.board.Square;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a pawn piece.
 */
public class Pawn extends Piece implements FirstMove {
    private static final String PREFIX = "P";
    private static final int[] CAPTURE_OFFSETS_X = {1, -1};
    private boolean hasMoved;
    private boolean holyHell; // Google en passant... Holy Hell! :]

    /**
     * EFFECTS: Constructs a new Pawn with given params.
     */
    public Pawn(Colour colour) {
        super(colour);
        this.hasMoved = false;
        this.holyHell = false;
    }

    /**
     * EFFECTS: See Piece.getValidSquares.
     */
    @Override
    public Set<Square> getValidSquares(Board board, Square start) {
        Set<Square> validSquares = new HashSet<>();
        int x = start.getX();
        int y = start.getY() + getColour().getDirection();

        // Basic 1-square pawn push.
        if (!board.isOutOfBounds(x, y)) {
            Square square = board.getSquare(x, y);
            if (!square.hasPiece()) {
                validSquares.add(square);

                // 2-square pawn push on the first move.
                if (!hasMoved && !board.isOutOfBounds(x, y + getColour().getDirection())) {
                    Square doubleSquare = board.getSquare(x, y + getColour().getDirection());
                    if (!doubleSquare.hasPiece()) {
                        validSquares.add(doubleSquare);
                    }
                }
            }
        }

        addCaptureSquares(validSquares, board, start);
        return validSquares;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public void setHasMoved() {
        hasMoved = true;
    }

    public boolean getEnPassable() {
        return holyHell;
    }

    public void setEnPassable(boolean to) {
        holyHell = to;
    }

    /**
     * EFFECTS: Checks for valid squares by capture and adds them to the given set.
     * MODIFIES: set reference
     */
    private void addCaptureSquares(Set<Square> validSquares, Board board, Square start) {
        for (int offset : CAPTURE_OFFSETS_X) {
            // Apply offset to starting square based on preset capture offsets.
            int x = start.getX() + offset;
            int y = start.getY() + getColour().getDirection();

            if (!board.isOutOfBounds(x, y)) {
                Square diagonalSquare = board.getSquare(x, y);
                Square adjacentSquare = board.getSquare(x, start.getY());

                // Check for captures on diagonal squares or en passant on adjacent squares.
                if (diagonalSquare.hasPiece() && diagonalSquare.getPiece().getColour() != getColour()) {
                    validSquares.add(diagonalSquare);
                } else if (adjacentSquare.hasPiece() && adjacentSquare.getPiece().getColour() != getColour()) {
                    if (adjacentSquare.getPiece() instanceof Pawn
                            && ((Pawn) adjacentSquare.getPiece()).getEnPassable()) {
                        validSquares.add(diagonalSquare);
                    }
                }
            }
        }
    }
}
