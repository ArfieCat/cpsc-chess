package model.pieces;

import model.Colour;
import model.Direction;
import model.board.Board;
import model.board.Square;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a king piece.
 */
public class King extends Piece {
    private static final String PREFIX = "K";
    private static final Direction[] MOVE_DIRECTIONS = {
            Direction.EAST, Direction.NORTHEAST, Direction.NORTH, Direction.NORTHWEST,
            Direction.WEST, Direction.SOUTHWEST, Direction.SOUTH, Direction.SOUTHEAST
    };

    /**
     * EFFECTS: Constructs a new Pawn with given params.
     */
    public King(Colour colour) {
        super(colour);
    }

    /**
     * EFFECTS: See Piece.getValidSquares.
     */
    @Override
    public Set<Square> getValidSquares(Board board, Square start) {
        Set<Square> validSquares = new HashSet<>();

        // Apply offset to starting square based on direction.
        for (int i = 0, x = start.getX() + MOVE_DIRECTIONS[i].getX(), y = start.getY() + MOVE_DIRECTIONS[i].getY();
                i < MOVE_DIRECTIONS.length; i++) {
            if (!board.isOutOfBounds(x, y)) {
                Square square = board.getSquare(x, y);

                // Check if the square is empty or occupied by a piece of the opposite colour.
                if (!square.hasPiece() || square.getPiece().getColour() != getColour()) {
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
