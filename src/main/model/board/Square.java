package model.board;

import model.piece.Piece;

/**
 * Represents a square on the board.
 */
public class Square {
    private final int positionX;
    private final int positionY;
    private Piece piece;

    /**
     * @EFFECTS: Constructs a new square with the given params.
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

    /**
     * @EFFECTS: Returns {@code true} if the square is occupied by a piece.
     */
    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece to) {
        piece = to;
    }
}
