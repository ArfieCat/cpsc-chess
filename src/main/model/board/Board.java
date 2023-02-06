package model.board;

import model.Colour;
import model.Move;
import model.pieces.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Represents the game board.
 */
public class Board {
    private static final int SIZE = 8;
    private final Square[] gameState;
    private final List<Move> history;

    /**
     * EFFECTS: Constructs a new empty Board.
     */
    public Board() {
        this.gameState = new Square[SIZE * SIZE];
        this.history = new LinkedList<>();

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
        int y = colour.getDirection() > 0 ? 0 : SIZE - 1;

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
            getSquare(i, y + colour.getDirection()).setPiece(new Pawn(colour));
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
        boolean isGameOver = move.getEndPiece() instanceof King;

        // Change the position of the piece on the start square.
        move.getStart().setPiece(null);
        move.getEnd().setPiece(move.getStartPiece());
        history.add(move);

        // Handle "special" moves.
        if (move.getStartPiece() instanceof Pawn) {
            doEnPassant(move);
            doPromotion(move);
        } else if (move.getStartPiece() instanceof King) {
            doCastling(move);
        }

        if (move.getStartPiece() instanceof HasMovedRule) {
            ((HasMovedRule) move.getStartPiece()).setHasMoved();
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
                Piece piece = getSquare(x, y).getPiece();
                String string = !visibleSquares.contains(getSquare(x, y)) ? " "
                        : piece == null ? "."
                        : piece.getColour() == colour ? piece.toString()
                        : piece.toString().toLowerCase();
                stringBuilder.append(string).append("  ");
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append("   a  b  c  d  e  f  g  h");
        return stringBuilder.toString();
    }

    /**
     * EFFECTS: Handles en passant if applicable.
     * MODIFIES: this
     */
    private void doEnPassant(Move move) {
        Pawn pawn = (Pawn) move.getStartPiece();

        // Enable en passant after a 2-square pawn push.
        if (move.getEnd().getY() - move.getStart().getY() == pawn.getColour().getDirection() * 2) {
            pawn.setCanBeCapturedEnPassant(true);
        }

        if (move.getStart().getX() != move.getEnd().getX()) {
            // Backtrack one square to determine if the current move is en passant.
            Square square = getSquare(move.getEnd().getX(), move.getEnd().getY() - pawn.getColour().getDirection());
            boolean isEnPassant = square.hasPiece()
                    && square.getPiece().getColour() != pawn.getColour()
                    && square.getPiece() instanceof Pawn
                    && ((Pawn) square.getPiece()).canBeCapturedEnPassant();
            if (isEnPassant) {
                square.setPiece(null);
            }
        }
    }

    /**
     * EFFECTS: Promotes a pawn on its last rank to a queen if applicable.
     * MODIFIES: this
     */
    private void doPromotion(Move move) {
        Pawn pawn = (Pawn) move.getStartPiece();

        // Check if the pawn is on its last rank.
        if (move.getEnd().getY() == (pawn.getColour().getDirection() < 0 ? 0 : SIZE - 1)) {
            move.getEnd().setPiece(new Queen(pawn.getColour()));
        }
    }

    private void doCastling(Move move) {
        // TODO: do this
    }
}
