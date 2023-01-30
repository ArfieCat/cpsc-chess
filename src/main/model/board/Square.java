package model.board;

import model.pieces.Piece;

/**
 * Represents a square on the board.
 */
public class Square {
    private final int positionX;
    private final int positionY;
    private Piece piece;

    /**
     * EFFECTS: Constructs a new Square with given params.
     * REQUIRES: x, y in [0, 7]
     */
    public Square(int x, int y) {
        this.positionX = x;
        this.positionY = y;
        this.piece = null;
    }

    public int getX() {
        return positionX;
    }

    public int getY() {
        return positionY;
    }

    public Piece getPiece() {
        return piece;
    }

    /**
     * EFFECTS: Sets piece.
     * MODIFIES: this
     */
    public void setPiece(Piece to) {
        piece = to;
    }
}
