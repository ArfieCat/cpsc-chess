package model.board;

import model.Colour;
import model.pieces.Bishop;
import model.pieces.Queen;
import model.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board.
 */
public class Board {
    private static final int SIZE = 8;
    private final List<Square> gameState;

    /**
     * EFFECTS: Constructs a new Board with all pieces in their starting positions.
     */
    public Board() {
        gameState = new ArrayList<>();

        // Initialize an empty board.
        for (int i = 0; i < SIZE * SIZE; i++) {
            gameState.add(new Square(i % SIZE, i / SIZE));
        }

        resetPieces();
    }

    /**
     * EFFECTS: Removes all pieces from the board.
     * MODIFIES: this
     */
    public void clearPieces() {
        for (int i = 0; i < SIZE * SIZE; i++) {
            gameState.get(i).setPiece(null);
        }
    }

    /**
     * EFFECTS: Sets all pieces in their starting positions.
     * MODIFIES: this
     */
    public void resetPieces() {
        clearPieces();

        for (int i = 0; i < SIZE; i++) {
            getSquare(i, 1); // white pawn
            getSquare(i, 7); // black pawn
        }

        // No better way than to hard-code this...
        getSquare(0, 0).setPiece(new Rook(Colour.WHITE));
        getSquare(1, 0); // white knight
        getSquare(2, 0).setPiece(new Bishop(Colour.WHITE));
        getSquare(3, 0).setPiece(new Queen(Colour.WHITE));
        getSquare(4, 0); // white king
        getSquare(5, 0).setPiece(new Bishop(Colour.WHITE));
        getSquare(6, 0); // white knight
        getSquare(7, 0).setPiece(new Rook(Colour.WHITE));

        getSquare(0, 7).setPiece(new Rook(Colour.WHITE));
        getSquare(1, 7); // black knight
        getSquare(2, 7).setPiece(new Bishop(Colour.BLACK));
        getSquare(3, 7).setPiece(new Queen(Colour.BLACK));
        getSquare(4, 7); // black king
        getSquare(5, 7).setPiece(new Bishop(Colour.BLACK));
        getSquare(6, 7); // black knight
        getSquare(7, 7).setPiece(new Rook(Colour.BLACK));
    }

    /**
     * EFFECTS: Returns true if the given coordinate is off the board.
     */
    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || y < 0 || x > SIZE || y > SIZE;
    }

    /**
     * EFFECTS: Returns the square at the given position.
     * REQUIRES: x, y in [0, 7]
     */
    public Square getSquare(int x, int y) {
        return gameState.get(y * SIZE + x);
    }
}
