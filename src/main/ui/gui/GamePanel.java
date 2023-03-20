package ui.gui;

import model.Move;
import model.board.Board;
import persistence.JsonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

/**
 * Represents a game of chess graphically.
 */
public class GamePanel extends JPanel {
    private final Board board;
    private SquarePanel selection;

    private final JPanel boardPanel;
    private final JPanel infoPanel;

    public GamePanel() {
        this.board = new Board();
        this.selection = null;

        this.boardPanel = new JPanel();
        this.infoPanel = new JPanel();
        setup();
    }

    /**
     * @EFFECTS: Loads an existing game from a JSON file.
     * @MODIFIES: {@code this}
     */
    public void loadFile(String fileName) throws IOException {
        for (Move move : JsonUtils.load(fileName, board)) {
            board.doMove(move);
        }
    }

    public List<Move> getBoardHistory() {
        return board.getHistory();
    }

    /**
     * @EFFECTS: Sets properties and adds components.
     * @MODIFIES: {@code this}
     */
    private void setup() {
        setLayout(new BorderLayout());

        add(setupBoardPanel(), BorderLayout.LINE_START);
        add(setupInfoPanel(), BorderLayout.LINE_END);
    }

    /**
     * @EFFECTS: Sets attributes and adds components to a new board panel, and returns it.
     * @MODIFIES: {@code this}
     */
    private JPanel setupBoardPanel() {
        boardPanel.setLayout(new GridLayout(Board.SIZE, Board.SIZE));

        for (int y = Board.SIZE - 1; y >= 0; y--) {
            for (int x = 0; x < Board.SIZE; x++) {
                SquarePanel squarePanel = new SquarePanel(board.getSquare(x, y));
                squarePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        validateMove((SquarePanel) e.getComponent());
                        repaint();
                    }
                });
                boardPanel.add(squarePanel);
            }
        }
        return boardPanel;
    }

    /**
     * @EFFECTS: Sets attributes and adds components to a new info panel, and returns it.
     */
    private JPanel setupInfoPanel() {
        return infoPanel;
    }

    /**
     * @EFFECTS: Ensures that the user input for a move is valid and updates the game.
     * @MODIFIES: {@code this}
     */
    private void validateMove(SquarePanel panel) {
        if (!board.isGameOver()) {
            if (selection == null) {
                panel.setHighlighted(true);
                selection = panel;
            } else {
                Move move = new Move(selection.getSquare(), panel.getSquare());
                if (move.getStart().hasPiece() && move.getStart().getPiece().getColour() == board.getCurrentPlayer()
                        && move.isValid(board)) {
                    board.doMove(move);
                }
                selection.setHighlighted(false);
                selection = null;
            }
        }
    }
}