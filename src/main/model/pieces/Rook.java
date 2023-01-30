package model.pieces;

import model.Colour;
import model.Direction;
import model.board.Board;
import model.board.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rook piece.
 */
public class Rook extends Piece {
    private static final String PREFIX = "R";

    /**
     * EFFECTS: Constructs a new Rook with given params.
     */
    public Rook(Colour colour) {
        super(colour);
    }

    @Override
    public List<Square> getValidSquares(Board board, Square start) {
        List<Square> validSquares = new ArrayList<>();

        validSquares.addAll(getValidDirectionalSquares(board, start, Direction.EAST));
        validSquares.addAll(getValidDirectionalSquares(board, start, Direction.NORTH));
        validSquares.addAll(getValidDirectionalSquares(board, start, Direction.WEST));
        validSquares.addAll(getValidDirectionalSquares(board, start, Direction.SOUTH));

        return validSquares;
    }

    /**
     * EFFECTS: Returns a list of valid squares in the given direction only.
     */
    private List<Square> getValidDirectionalSquares(Board board, Square start, Direction direction) {
        List<Square> validSquares = new ArrayList<>();

        // Apply offset based on direction to starting square.
        for (int i = 1, x = start.getX() + direction.getX() * i, y = start.getY() + direction.getY() * i;
                !board.isOutOfBounds(x, y); i++) {
            Square square = board.getSquare(x, y);

            // Check if the square is empty.
            if (square.getPiece() == null) {
                validSquares.add(square);
                continue;
            }

            // If the square is occupied, check if that piece is of the opposite colour.
            if (square.getPiece().getColour() != getColour()) {
                validSquares.add(square);
            }

            break;
        }

        return validSquares;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
