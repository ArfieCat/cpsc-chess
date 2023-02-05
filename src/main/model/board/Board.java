package model.board;

import model.Colour;
import model.pieces.*;
import model.Move;

import java.util.HashSet;
import java.util.Set;

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
     * EFFECTS: Updates the board according to the given move and returns false if the game is over.
     * MODIFIES: this
     * REQUIRES: move.isValid()
     */
    public boolean doMove(Move move) {
        boolean isGameOver = move.getEnd().hasPiece() && move.getEnd().getPiece() instanceof King;
        Piece piece = move.getStart().getPiece();

        // Change the position of the piece on the start square.
        move.getStart().setPiece(null);
        move.getEnd().setPiece(piece);

        if (piece instanceof Pawn && !((Pawn) piece).hasMoved()) {
            ((Pawn) piece).setHasMoved(true);
        }

        return isGameOver;
    }

    /**
     * EFFECTS: Returns true if the given coordinate is off the board.
     */
    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || y < 0 || x > SIZE - 1 || y > SIZE - 1;
    }

    /**
     * EFFECTS: Returns the square at the given position.
     * REQUIRES: !isOutOfBounds(x, y)
     */
    public Square getSquare(int x, int y) {
        return gameState[y * SIZE + x];
    }

    /**
     * EFFECTS: Returns a string representation of the board for display.
     */
    public String getDisplayString(Colour colour) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<Square> visibleSquares = new HashSet<>();

        for (Square square : gameState) {
            if (square.hasPiece() && square.getPiece().getColour() == colour) {
                visibleSquares.add(square);
                visibleSquares.addAll(square.getPiece().getValidSquares(this, square));
            }
        }

        // Reverse iterate over rows to get the correct display order.
        for (int y = SIZE - 1; y >= 0; y--) {
            stringBuilder.append(y + 1).append("  ");
            for (int x = 0; x < SIZE; x++) {
                if (visibleSquares.contains(getSquare(x, y))) {
                    stringBuilder.append(getSquare(x, y).hasPiece() ? getSquare(x, y).getPiece() : ".");
                } else {
                    stringBuilder.append(" ");
                }
                stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append("   a  b  c  d  e  f  g  h");
        return stringBuilder.toString();
    }
}
