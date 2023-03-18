package ui.gui;

import model.Colour;
import model.Move;
import model.board.Board;
import model.board.Square;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a game of chess with graphics.
 */
public class GamePanel extends JPanel {
    private static final Colour[] PLAYERS = {Colour.WHITE, Colour.BLACK};

    private final Board board;
    private boolean isGameOver;

    private final JPanel boardDisplay;
    private Square selection;

    /**
     * @EFFECTS: Constructs a new game panel.
     */
    public GamePanel() {
        this.board = new Board();
        this.isGameOver = false;
        this.boardDisplay = new JPanel();

        for (Colour colour : PLAYERS) {
            board.setupPieces(colour);
        }

        inflate();
    }

    /**
     * @EFFECTS: Sets properties and adds components to the game panel.
     * @MODIFIES: {@code this}
     */
    private void inflate() {
        boardDisplay.setLayout(new GridLayout(Board.SIZE, Board.SIZE));

        for (int y = 0; y < Board.SIZE; y++) {
            for (int x = 0; x < Board.SIZE; x++) {
                boardDisplay.add(new SquarePanel(board.getSquare(x, y)));
            }
        }

        add(boardDisplay, BorderLayout.WEST);
    }

    /**
     * @EFFECTS: Ensures that the user input for a move is valid and updates the game.
     * @MODIFIES: {@code this}
     */
    private void validateMove(Square start, Square end) {
        // This may violate a "REQUIRES" clause.
        Move move = new Move(start, end);
        if (!start.hasPiece() || start.getPiece().getColour() != getCurrentPlayer() || !move.isValid(board)) {
            return;
        }

        isGameOver = board.doMove(move);
    }

    /**
     * @EFFECTS: Returns the player colour whose turn it currently is.
     */
    private Colour getCurrentPlayer() {
        return PLAYERS[board.getHistory().size() % PLAYERS.length];
    }
}