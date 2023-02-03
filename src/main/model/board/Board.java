package model.board;

import model.Colour;
import model.pieces.*;

/**
 * Represents the game board.
 */
public class Board {
    private static final int SIZE = 8;
    private final Square[] gameState;

    /**
     * EFFECTS: Constructs a new empty Board.
     */
    public Board() {
        gameState = new Square[SIZE * SIZE];

        // Initialize an empty board.
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = new Square(i % SIZE, i / SIZE);
        }
    }

    /**
     * EFFECTS: Sets all pieces of the given colour in their starting positions.
     * MODIFIES: this
     */
    public void setupPieces(Colour colour) {
        int y = colour.getDirection().getY() > 0 ? 0 : SIZE - 1;

        // Remove any pieces of the given colour first.
        clearPieces(colour);

        // No better way than to hard-code this...
        getSquare(0, y).setPiece(new Rook(colour));
        getSquare(1, y).setPiece(new Knight(colour));
        getSquare(2, y).setPiece(new Bishop(colour));
        getSquare(3, y).setPiece(new Queen(colour));
        getSquare(SIZE - 4, y).setPiece(new King(colour));
        getSquare(SIZE - 3, y).setPiece(new Bishop(colour));
        getSquare(SIZE - 2, y).setPiece(new Knight(colour));
        getSquare(SIZE - 1, y).setPiece(new Rook(colour));

        for (int i = 0; i < SIZE; i++) {
            getSquare(i, y + colour.getDirection().getY()).setPiece(new Pawn(colour));
        }
    }

    /**
     * EFFECTS: Removes all pieces of the given colour from the board.
     * MODIFIES: this
     */
    public void clearPieces(Colour colour) {
        for (Square square : gameState) {
            if (square.getPiece() != null && square.getPiece().getColour() == colour) {
                square.setPiece(null);
            }
        }
    }

    /**
     * EFFECTS: Returns true if the given coordinate is off the board.
     */
    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || y < 0 || x > SIZE - 1 || y > SIZE - 1;
    }

    /**
     * EFFECTS: Returns the square at the given position.
     * REQUIRES: x, y are not out of bounds
     */
    public Square getSquare(int x, int y) {
        return gameState[y * SIZE + x];
    }
}
