package model.pieces;

import model.Colour;
import model.Direction;
import model.board.Board;
import model.board.Square;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a pawn piece.
 */
public class Pawn extends Piece {
    private static final String PREFIX = "P";
    private static final Direction[] CAPTURE_DIRECTIONS = {Direction.EAST, Direction.WEST};
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
        int y = start.getY() + getColour().getDirection().getY();

        // Basic 1-square pawn push.
        if (!board.isOutOfBounds(x, y)) {
            Square square = board.getSquare(x, y);

            // Check if the square is empty.
            if (!square.hasPiece()) {
                validSquares.add(square);
            }
        }

        // A 2-square pawn push on the first move enables en passant.
        if (!hasMoved && !board.isOutOfBounds(x, y + getColour().getDirection().getY())) {
            Square square = board.getSquare(x, y + getColour().getDirection().getY());

            // Check if the square is empty.
            if (!square.hasPiece()) {
                validSquares.add(square);
            }
        }

        addCaptureSquares(validSquares, board, start);
        return validSquares;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean to) {
        hasMoved = to;
    }

    public boolean canBeCapturedEnPassant() {
        return holyHell;
    }

    public void setCanBeCapturedEnPassant(boolean to) {
        holyHell = to;
    }

    /**
     * EFFECTS: Checks for valid squares by capture and adds them to the given set.
     * MODIFIES: set reference
     */
    private void addCaptureSquares(Set<Square> validSquares, Board board, Square start) {
        // Apply offset to starting square based on direction.
        for (int i = 0, x = start.getX() + CAPTURE_DIRECTIONS[i].getX(), y = start.getY()
                + getColour().getDirection().getY(); i < CAPTURE_DIRECTIONS.length; i++) {
            if (!board.isOutOfBounds(x, y)) {
                Square diagonalSquare = board.getSquare(x, y);
                Square adjacentSquare = board.getSquare(x, start.getY());

                // Check if the diagonal square is occupied by a piece of the opposite colour.
                boolean canCapture = diagonalSquare.hasPiece()
                        && diagonalSquare.getPiece().getColour() != getColour();

                // Check if the adjacent square is occupied by a pawn that can be captured en passant.
                boolean canCaptureEnPassant = adjacentSquare.hasPiece()
                        && adjacentSquare.getPiece().getColour() != getColour()
                        && adjacentSquare.getPiece().getClass() == getClass()
                        && ((Pawn) diagonalSquare.getPiece()).canBeCapturedEnPassant();

                if (canCapture || canCaptureEnPassant) {
                    validSquares.add(diagonalSquare);
                }
            }
        }
    }

    @Override
    public String toString() {
        return PREFIX;
    }
}
