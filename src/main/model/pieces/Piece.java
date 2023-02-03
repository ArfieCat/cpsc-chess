package model.pieces;

import model.Colour;
import model.board.Board;
import model.board.Square;

import java.util.Set;

/**
 * Represents a generic piece.
 */
public abstract class Piece {
    private final Colour colour;

    /**
     * EFFECTS: Constructs a new Piece with given params.
     */
    public Piece(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    /**
     * EFFECTS: Returns the set of all squares on the board that the piece can move to.
     */
    public abstract Set<Square> getValidSquares(Board board, Square start);

    public abstract String getPrefix();
}
