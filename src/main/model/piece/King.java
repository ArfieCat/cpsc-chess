package model.piece;

import model.Colour;
import model.Direction;
import model.board.Board;
import model.board.Square;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a king piece.
 */
public class King extends Piece implements FirstMove {
    private static final String PREFIX = "K";
    private static final Direction[] MOVE_DIRECTIONS = {
            Direction.EAST, Direction.NORTHEAST, Direction.NORTH, Direction.NORTHWEST,
            Direction.WEST, Direction.SOUTHWEST, Direction.SOUTH, Direction.SOUTHEAST
    };
    private static final Direction[] CASTLE_DIRECTIONS = {Direction.EAST, Direction.WEST};
    private static final int[] CASTLE_OFFSETS_X = {2, -2};
    private boolean hasMoved;

    /**
     * EFFECTS: Constructs a new Pawn with given params.
     */
    public King(Colour colour) {
        super(colour);
        this.hasMoved = false;
    }

    /**
     * EFFECTS: See Piece.getValidSquares.
     */
    @Override
    public Set<Square> getValidSquares(Board board, Square start) {
        Set<Square> validSquares = new HashSet<>();

        for (Direction direction : MOVE_DIRECTIONS) {
            // Apply offset to starting square based on direction.
            int x = start.getX() + direction.getX();
            int y = start.getY() + direction.getY();

            if (!board.isOutOfBounds(x, y)) {
                Square square = board.getSquare(x, y);

                // Check if the square is empty or occupied by a piece of the opposite colour.
                if (!square.hasPiece() || square.getPiece().getColour() != getColour()) {
                    validSquares.add(square);
                }
            }
        }

        addCastleSquares(validSquares, board, start);
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

    /**
     * EFFECTS: Checks for valid squares by castling and adds them to the given set.
     * MODIFIES: set reference
     */
    private void addCastleSquares(Set<Square> validSquares, Board board, Square start) {
        if (!hasMoved) {
            for (int i = 0; i < CASTLE_DIRECTIONS.length; i++) {
                Direction direction = CASTLE_DIRECTIONS[i];

                // Apply offset to starting square based on direction until out of bounds.
                for (int x = start.getX() + direction.getX(), y = start.getY();
                        !board.isOutOfBounds(x, y); x += direction.getX()) {
                    Square square = board.getSquare(x, y);

                    // Check if the first occupied square has an unmoved rook of the same colour.
                    if (square.hasPiece()) {
                        if (square.getPiece().getColour() == getColour()) {
                            if (square.getPiece() instanceof Rook && !((Rook) square.getPiece()).getHasMoved()) {
                                validSquares.add(board.getSquare(start.getX() + CASTLE_OFFSETS_X[i], y));
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
}
