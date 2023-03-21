package ui.gui;

import model.board.Square;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Represents a square on the board graphically.
 */
public class SquarePanel extends JPanel {
    private static final String PATH = "./data/.resources/piece/";
    private static final String EXT = ".png";

    private static final Color[] DEFAULT_COLOURS = {new Color(0xB98761), new Color(0xEDD6B0)};
    private static final Color[] HIGHLIGHT_COLOURS = {new Color(0xDCC431), new Color(0xF7EB58)};
    private static final int SIZE = 64;

    private final Square square;
    private final Color[] colours;
    private boolean isHighlighted;

    private final JLabel iconLabel;

    /**
     * @EFFECTS: Constructs a new square panel.
     */
    public SquarePanel(Square square) {
        this.square = square;
        this.colours = getBackgroundColours();
        this.isHighlighted = false;

        this.iconLabel = new JLabel();
        setup();
    }

    /**
     * @EFFECTS: See {@code JPanel.paintComponent}.
     * @MODIFIES: {@code this}
     */
    @Override
    protected void paintComponent(Graphics g) {
        setBackground(isHighlighted ? colours[1] : colours[0]);
        iconLabel.setIcon(square.hasPiece() ? getIconResource() : null);

        super.paintComponent(g);
    }

    public Square getSquare() {
        return square;
    }

    public void setHighlighted(boolean to) {
        isHighlighted = to;
    }

    /**
     * @EFFECTS: Sets properties and adds components.
     * @MODIFIES: {@code this}
     */
    private void setup() {
        setPreferredSize(new Dimension(SIZE, SIZE));
        setLayout(new BorderLayout());

        add(iconLabel, BorderLayout.CENTER);
    }

    /**
     * @EFFECTS: Returns an array of the default and highlighted background colours.
     */
    private Color[] getBackgroundColours() {
        return new Color[]{DEFAULT_COLOURS[(square.getX() + square.getY()) % DEFAULT_COLOURS.length],
                HIGHLIGHT_COLOURS[(square.getX() + square.getY()) % HIGHLIGHT_COLOURS.length]};
    }

    /**
     * @EFFECTS: Returns an icon with the sprite at the given path.
     */
    public Icon getIconResource() {
        ImageIcon icon = new ImageIcon(Paths.get(PATH + (square.getPiece().getColour().toString() + "/"
                + square.getPiece().getClass().getSimpleName()).toLowerCase(Locale.ROOT) + EXT).toString());
        return new ImageIcon(icon.getImage().getScaledInstance(getWidth(), getWidth(), Image.SCALE_SMOOTH));
    }
}