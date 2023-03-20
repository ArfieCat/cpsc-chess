package ui.gui;

import model.board.Square;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a square on the board graphically.
 */
public class SquarePanel extends JPanel {
    public static final int SIZE = 64;
    private static final Color[] DEFAULT_COLOURS = {new Color(0xB58863), new Color(0xF0D9B5)};
    private static final Color[] HIGHLIGHT_COLOURS = {new Color(0xACA249), new Color(0xCED17A)};

    private final Square square;
    private final JLabel iconLabel;

    /**
     * @EFFECTS: Constructs a new square panel.
     */
    public SquarePanel(Square square) {
        this.square = square;
        this.iconLabel = new JLabel();
        setup();
    }

    /**
     * @EFFECTS: Updates the square panel to match the current board state.
     * @MODIFIES: {@code this}
     */
    public void refresh() {
        if (square.hasPiece()) {
            iconLabel.setIcon(new ImageIcon("./data/res/" + (square.getPiece().getClass().getSimpleName()
                    + "-" + square.getPiece().getColour()).toLowerCase() + ".png"));
        } else {
            iconLabel.setIcon(null);
        }
    }

    /**
     * @EFFECTS: Updates the background colour to highlight the panel.
     * @MODIFIES: {@code this}
     */
    public void setSelected(boolean to) {
        setBackground(to ? HIGHLIGHT_COLOURS[(square.getX() + square.getY()) % HIGHLIGHT_COLOURS.length]
                : DEFAULT_COLOURS[(square.getX() + square.getY()) % DEFAULT_COLOURS.length]);
    }

    public Square getSquare() {
        return square;
    }

    /**
     * @EFFECTS: Sets properties and adds components.
     * @MODIFIES: {@code this}
     */
    private void setup() {
        setPreferredSize(new Dimension(SIZE, SIZE));
        setLayout(new BorderLayout());
        setSelected(false);

        add(iconLabel, BorderLayout.CENTER);
        refresh();
    }
}
