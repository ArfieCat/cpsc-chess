package model.pieces;

import model.Colour;
import model.Direction;
import model.board.Board;
import model.board.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bishop piece.
 */
public class Bishop extends Piece {
    private static final String PREFIX = "B";
    private static final Direction[] MOVE_DIRECTIONS = {
            Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHWEST, Direction.SOUTHEAST
    };

    /**
     * EFFECTS: Constructs a new Bishop with given params.
     */
    public Bishop(Colour colour) {
        super(colour);
    }

    /**
     * EFFECTS: See Piece.getValidSquares.
     */
    @Override
    public List<Square> getValidSquares(Board board, Square start) {
        List<Square> validSquares = new ArrayList<>();

        for (Direction direction : MOVE_DIRECTIONS) {
            // Apply offset to starting square based on direction until out of bounds.
            for (int i = 1, x = start.getX() + direction.getX() * i, y = start.getY() + direction.getY() * i;
                    !board.isOutOfBounds(x, y); i++) {
                Square square = board.getSquare(x, y);

                // Check if the square is occupied by another piece.
                if (square.getPiece() != null) {
                    if (square.getPiece().getColour() != getColour()) {
                        validSquares.add(square);
                    }
                    break;
                }
                validSquares.add(square);
            }
        }
        return validSquares;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
