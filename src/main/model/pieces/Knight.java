package model.pieces;

import model.Colour;
import model.board.Board;
import model.board.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a knight piece.
 */
public class Knight extends Piece {
    private static final String PREFIX = "N";
    private static final int[] MOVE_OFFSETS_X = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] MOVE_OFFSETS_Y = {1, 2, 2, 1, -1, -2, -2, -1};

    /**
     * EFFECTS: Constructs a new Knight with given params.
     */
    public Knight(Colour colour) {
        super(colour);
    }

    /**
     * EFFECTS: See Piece.getValidSquares.
     */
    @Override
    public List<Square> getValidSquares(Board board, Square start) {
        List<Square> validSquares = new ArrayList<>();

        // Apply offset to starting square based on preset move offsets.
        for (int i = 0, x = start.getX() + MOVE_OFFSETS_X[i], y = start.getY() + MOVE_OFFSETS_Y[i];
                i < MOVE_OFFSETS_X.length; i++) {
            if (!board.isOutOfBounds(x, y)) {
                Square square = board.getSquare(x, y);

                // Check if the square is empty or occupied by a piece of the opposite colour.
                if (square.getPiece() == null || square.getPiece().getColour() != getColour()) {
                    validSquares.add(square);
                }
            }
        }

        return validSquares;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
