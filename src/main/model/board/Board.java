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
    private Pawn lastEnPassantTarget;
    private boolean isGameOver;

    /**
     * @EFFECTS: Constructs a new empty board.
     */
    public Board() {
        this.gameState = new Square[SIZE * SIZE];
        this.history = new LinkedList<>();
        this.lastEnPassantTarget = null;
        this.isGameOver = false;

        // Initialize an empty board.
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = new Square(i % SIZE, i / SIZE);
        }
        setupPieces();
    }

    /**
     * @EFFECTS: Updates the board according to the given move.
     * @MODIFIES: {@code this}, {@code move}
     * @REQUIRES: {@code move.isValid()}
     */
    public void doMove(Move move) {
        isGameOver = move.getEnd().getPiece() instanceof King;
        move.setMovedPiece(move.getStart().getPiece());

        // Handle "special" moves.
        if (move.getMovedPiece() instanceof Pawn) {
            doEnPassant(move);
        } else if (move.getMovedPiece() instanceof King) {
            doCastling(move);
        }

        // Set move-related flags.
        if (move.getMovedPiece() instanceof FirstMove) {
            ((FirstMove) move.getMovedPiece()).setHasMoved();
        }
        if (move.getEnd().hasPiece()) {
            move.setFlag(Move.CAPTURE);
        }

        // Change the position of the piece on the start square.
        move.getEnd().setPiece(move.getMovedPiece());
        move.getStart().setPiece(null);
        history.add(move);

        if (move.getEnd().getPiece() instanceof Pawn) {
            doPromotion(move);
        }
    }

    /**
     * @EFFECTS: Returns the set of all squares visible to the given player colour.
     */
    public Set<Square> getVisibleSquares(Colour colour) {
        Set<Square> visibleSquares = new HashSet<>();

        for (Square square : gameState) {
            if (square.hasPiece() && square.getPiece().getColour() == colour) {
                visibleSquares.add(square);
                visibleSquares.addAll(square.getPiece().getValidSquares(this, square));
            }
        }
        return visibleSquares;
    }

    /**
     * @EFFECTS: Returns the player colour whose turn it currently is.
     */
    public Colour getCurrentPlayer() {
        return Colour.values()[history.size() % Colour.values().length];
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

    public List<Move> getHistory() {
        return history;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * @EFFECTS: Sets all pieces in their starting positions.
     * @MODIFIES: {@code this}
     */
    private void setupPieces() {
        for (Colour colour : Colour.values()) {
            int y = colour.getDirection() > 0 ? 0 : SIZE - 1;

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
    }

    /**
     * @EFFECTS: Captures a pawn en passant, if applicable.
     * @MODIFIES: {@code this}, {@code move}
     */
    private void doEnPassant(Move move) {
        Pawn pawn = (Pawn) move.getMovedPiece();

        if (move.getStart().getX() != move.getEnd().getX()) {
            // Backtrack one square to determine if the current move is en passant.
            Square square = getSquare(move.getEnd().getX(), move.getEnd().getY() - pawn.getColour().getDirection());

            if (square.hasPiece()) {
                if (square.getPiece().getColour() != pawn.getColour()) {
                    if (square.getPiece() instanceof Pawn && ((Pawn) square.getPiece()).getEnPassable()) {
                        square.setPiece(null);
                        move.setFlag(Move.CAPTURE);
                    }
                }
            }
        }

        // Disable en passant for the last-moved pawn after one move.
        if (lastEnPassantTarget != null) {
            lastEnPassantTarget.setEnPassable(false);
        }

        // Enable en passant after any 2-square pawn push.
        if (Math.abs(move.getEnd().getY() - move.getStart().getY()) == 2) {
            pawn.setEnPassable(true);
            lastEnPassantTarget = pawn;
        }
    }

    /**
     * @EFFECTS: Castles with the appropriate rook, if applicable.
     * @MODIFIES: {@code this}, {@code move}
     */
    private void doCastling(Move move) {
        if (Math.abs(move.getEnd().getX() - move.getStart().getX()) == 2) {
            // Determine if the castle was kingside or queenside.
            int x = move.getEnd().getX() < move.getStart().getX() ? 0 : SIZE - 1;
            int offset = move.getEnd().getX() < move.getStart().getX() ? 1 : -1;

            // Move the rook to the appropriate square.
            getSquare(move.getEnd().getX() + offset, move.getEnd().getY()).setPiece(getSquare(x,
                    move.getEnd().getY()).getPiece());
            getSquare(x, move.getEnd().getY()).setPiece(null);
            move.setFlag(Move.CASTLE);
        }
    }

    /**
     * @EFFECTS: Promotes a pawn on its last rank to a queen, if applicable.
     * @MODIFIES: {@code this}, {@code move}
     */
    private void doPromotion(Move move) {
        Pawn pawn = (Pawn) move.getEnd().getPiece();
        int y = pawn.getColour().getDirection() < 0 ? 0 : SIZE - 1;

        // Check if the pawn is on its last rank.
        if (move.getEnd().getY() == y) {
            move.getEnd().setPiece(new Queen(pawn.getColour()));
            move.setFlag(Move.PROMOTE);
        }
    }
}