package ui.gui;

import model.board.Square;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SquarePanel extends JPanel {
    private static final int SQUARE_SIZE = 50;

    private final Square square;

    public SquarePanel(Square square) {
        this.square = square;

        inflate();
    }

    public void setIcon() {
    }

    public Square getSquare() {
        return square;
    }

    /**
     * @EFFECTS: Sets properties and adds components to the square panel.
     * @MODIFIES: {@code this}
     */
    private void inflate() {
        setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        setBackground(new Color(Color.HSBtoRGB(0, 0, new Random().nextFloat())));
    }
}
