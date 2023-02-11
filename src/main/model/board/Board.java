package model.board;

import model.Colour;
import model.Move;
import model.piece.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Represents the game board.
 */
public class Board {
    public static final int SIZE = 8;
    private final Square[] gameState;
    private final List<Move> history;

    /**
     * @EFFECTS: Constructs a new empty Board.
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
     * @EFFECTS: Sets all pieces of the given colour in their starting positions.
     * @MODIFIES: this
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
     * @EFFECTS: Updates the board according to the given move, and returns {@code false} if the game is over.
     * @MODIFIES: {@code this}
     * @REQUIRES: {@code move.isValid()}
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

        if (move.getStartPiece() instanceof FirstMove) {
            ((FirstMove) move.getStartPiece()).setHasMoved();
        }

        return isGameOver;
    }

    /**
     * @EFFECTS: Returns {@code true} if the given coordinate is off the board.
     */
    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || y < 0 || x > SIZE - 1 || y > SIZE - 1;
    }

    /**
     * @EFFECTS: Returns the square at the given position.
     * @REQUIRES: {@code !isOutOfBounds(x, y)}
     */
    public Square getSquare(int x, int y) {
        return gameState[y * SIZE + x];
    }

    /**
     * @EFFECTS: Returns a String representation of the board for display.
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
                        : piece.getColour() == colour ? piece.getPrefix()
                        : piece.getPrefix().toLowerCase();
                stringBuilder.append(string).append("  ");
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append("   a  b  c  d  e  f  g  h");
        return stringBuilder.toString();
    }

    /**
     * @EFFECTS: Removes all pieces of the given colour from the board.
     * @MODIFIES: {@code this}
     */
    private void clearPieces(Colour colour) {
        for (Square square : gameState) {
            if (square.getPiece() != null && square.getPiece().getColour() == colour) {
                square.setPiece(null);
            }
        }
    }

    /**
     * @EFFECTS: Handles en passant, if applicable.
     * @MODIFIES: {@code this}
     */
    private void doEnPassant(Move move) {
        Pawn pawn = (Pawn) move.getStartPiece();

        // Enable en passant after a 2-square pawn push.
        if (Math.abs(move.getEnd().getY() - move.getStart().getY()) == 2) {
            pawn.setEnPassable(true);
        }

        if (move.getStart().getX() != move.getEnd().getX()) {
            // Backtrack one square to determine if the current move is en passant.
            Square square = getSquare(move.getEnd().getX(), move.getEnd().getY() - pawn.getColour().getDirection());

            if (square.hasPiece()) {
                if (square.getPiece().getColour() != pawn.getColour()) {
                    if (square.getPiece() instanceof Pawn && !((Pawn) square.getPiece()).getEnPassable()) {
                        square.setPiece(null);
                    }
                }
            }
        }
    }

    /**
     * @EFFECTS: Promotes a pawn on its last rank to a queen, if applicable.
     * @MODIFIES: {@code this}
     */
    private void doPromotion(Move move) {
        Pawn pawn = (Pawn) move.getStartPiece();
        int y = pawn.getColour().getDirection() < 0 ? 0 : SIZE - 1;

        // Check if the pawn is on its last rank.
        if (move.getEnd().getY() == y) {
            move.getEnd().setPiece(new Queen(pawn.getColour()));
        }
    }

    /**
     * @EFFECTS: Castles with the appropriate rook, if applicable.
     * @MODIFIES: {@code this}
     */
    private void doCastling(Move move) {
        if (Math.abs(move.getEnd().getX() - move.getStart().getX()) == 2) {
            // Determine if the castle was king- or queen-side.
            int x = move.getEnd().getX() - move.getStart().getX() < 0 ? 0 : SIZE - 1;
            int y = move.getEnd().getY();
            int offset = move.getEnd().getX() - move.getStart().getX() < 0 ? 1 : -1;

            // Move the rook to the appropriate square.
            getSquare(move.getEnd().getX() + offset, y).setPiece(getSquare(x, y).getPiece());
            getSquare(x, y).setPiece(null);
        }
    }
}

