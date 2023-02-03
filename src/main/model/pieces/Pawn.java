package model.pieces;

import model.Colour;
import model.Direction;
import model.board.Board;
import model.board.Square;

import java.util.ArrayList;
import java.util.List;

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
    public List<Square> getValidSquares(Board board, Square start) {
        List<Square> validSquares = new ArrayList<>();
        int x = start.getX();
        int y = start.getY() + getColour().getDirection().getY();

        // Basic 1-square pawn push.
        if (!board.isOutOfBounds(x, y)) {
            Square square = board.getSquare(x, y);

            // Check if the square is empty.
            if (square.getPiece() == null) {
                validSquares.add(square);
            }
        }

        // A 2-square pawn push on the first move enables en passant.
        if (!hasMoved && !board.isOutOfBounds(x, y + getColour().getDirection().getY())) {
            Square square = board.getSquare(x, y + getColour().getDirection().getY());

            // Check if the square is empty.
            if (square.getPiece() == null) {
                validSquares.add(square);
            }
        }

        addCaptureSquares(validSquares, board, start);
        return validSquares;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
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
     * EFFECTS: Checks for valid squares by capture and adds them to the given list.
     * MODIFIES: list reference
     */
    private void addCaptureSquares(List<Square> validSquares, Board board, Square start) {
        // Apply offset to starting square based on direction.
        for (int i = 0, x = start.getX() + CAPTURE_DIRECTIONS[i].getX(), y = start.getY()
                + getColour().getDirection().getY(); i < CAPTURE_DIRECTIONS.length; i++) {
            if (!board.isOutOfBounds(x, y)) {
                Square diagonalSquare = board.getSquare(x, y);
                Square adjacentSquare = board.getSquare(x, start.getY());

                // Check if the diagonal square is occupied by a piece of the opposite colour.
                boolean canCapture = diagonalSquare.getPiece() != null
                        && diagonalSquare.getPiece().getColour() != getColour();

                // Check if the adjacent square is occupied by a pawn that can be captured en passant.
                boolean canCaptureEnPassant = adjacentSquare.getPiece() != null
                        && adjacentSquare.getPiece().getColour() != getColour()
                        && adjacentSquare.getPiece().getClass() == Pawn.class
                        && ((Pawn) diagonalSquare.getPiece()).canBeCapturedEnPassant();

                if (canCapture || canCaptureEnPassant) {
                    validSquares.add(diagonalSquare);
                }
            }
        }
    }
}
