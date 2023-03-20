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

    public static final int SIZE = 60;
    private static final Color[] DEFAULT_COLOURS = {new Color(0xB98761), new Color(0xEDD6B0)};
    private static final Color HIGHLIGHT_COLOUR = Color.YELLOW;

    private final Square square;
    private final Color[] colours;
    private boolean isHighlighted;

    private final JLabel iconLabel;

    /**
     * @EFFECTS: Constructs a new square panel.
     */
    public SquarePanel(Square square) {
        this.square = square;
        this.colours = getBackgroundColours(DEFAULT_COLOURS[(square.getX() + square.getY()) % DEFAULT_COLOURS.length]);
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
        iconLabel.setIcon(getIconResource(square));

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
     * @EFFECTS: Returns the sprite matching the current piece occupying the square.
     */
    private static Icon getIconResource(Square square) {
        if (square.hasPiece()) {
            ImageIcon icon = new ImageIcon(Paths.get((PATH + square.getPiece().getColour().toString() + "/"
                    + square.getPiece().getClass().getSimpleName() + EXT).toLowerCase(Locale.ROOT)).toString());
            return new ImageIcon(icon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
        }
        return null;
    }

    /**
     * @EFFECTS: Returns an array of the default and highlighted background colours.
     */
    private static Color[] getBackgroundColours(Color colour) {
        float[] defaultComponents = colour.getRGBColorComponents(null);
        float[] highlightComponents = HIGHLIGHT_COLOUR.getRGBColorComponents(null);
        float[] components = new float[3];

        for (int i = 0; i < components.length; i++) {
            components[i] = Math.min(1.0f, 0.4f * highlightComponents[i] + 0.6f * defaultComponents[i]);
        }
        return new Color[]{colour, new Color(components[0], components[1], components[2])};
    }
}