package ui.gui;

import model.Colour;
import model.Move;
import model.board.Board;
import model.board.Square;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;

/**
 * Represents the board graphically.
 */
public class BoardDisplayPanel extends JPanel {
    private static final String PATH = "./data/.resources/sound/";
    private static final String EXT = ".wav";

    private final Board board;
    private Colour displayedPlayer;
    private SquarePanel selection;

    private final SquarePanel[] squarePanels;

    /**
     * @EFFECTS: Constructs a new board display panel with the given params.
     */
    public BoardDisplayPanel(Board board) {
        super(new GridLayout(Board.SIZE, Board.SIZE));

        this.board = board;
        this.displayedPlayer = board.getCurrentPlayer();
        this.selection = null;

        this.squarePanels = getSquarePanels();
        nextOrientation();
        playSoundResource("game-start");
    }

    /**
     * @EFFECTS: See {@code JComponent.paintComponent}.
     * @MODIFIES: {@code this}
     */
    @Override
    protected void paintComponent(Graphics g) {
        Set<Square> visibleSquares = board.getVisibleSquares(displayedPlayer);
        for (SquarePanel squarePanel : squarePanels) {
            squarePanel.setVisible(visibleSquares.contains(squarePanel.getSquare()));
        }
        super.paintComponent(g);
    }

    /**
     * @EFFECTS: Reorders the square panels to flip the board for the current player.
     * @MODIFIES: {@code this}
     */
    public void nextOrientation() {
        displayedPlayer = board.getCurrentPlayer();
        removeAll();

        for (int i = displayedPlayer.getDirection() < 0 ? 0 : squarePanels.length - 1;
                i > -1 && i < squarePanels.length; i -= displayedPlayer.getDirection()) {
            add(squarePanels[i]);
        }
        revalidate();
    }

    public Colour getDisplayedPlayer() {
        return displayedPlayer;
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
                    playSoundResource("move");
                    displayedPlayer = board.getCurrentPlayer();
                    board.doMove(move);
                    if (board.isGameOver()) {
                        playSoundResource("game-end");
                    }
                }
                selection.setHighlighted(false);
                selection = null;
            }
        }
    }

    /**
     * @EFFECTS: Loads and plays the sound effect at the given path.
     */
    private void playSoundResource(String fileName) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Paths.get(PATH + fileName + EXT).toUri().toURL()));
            clip.start();
        } catch (Exception e) { /* Whatever, man. */ }
    }

    /**
     * @EFFECTS: Returns an array with a square panel representing each square on the board.
     */
    private SquarePanel[] getSquarePanels() {
        SquarePanel[] squarePanels = new SquarePanel[Board.SIZE * Board.SIZE];

        // Create a square panel for every square on the board.
        for (int i = 0; i < squarePanels.length; i++) {
            squarePanels[i] = new SquarePanel(board.getSquare(Board.SIZE - i % Board.SIZE - 1, i / Board.SIZE));
            squarePanels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    validateMove((SquarePanel) e.getComponent());
                    getParent().repaint();
                }
            });
        }
        return squarePanels;
    }

    /**
     * Represents a square on the board graphically.
     */
    private static class SquarePanel extends JPanel {
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
            super(new BorderLayout());
            setPreferredSize(new Dimension(SIZE, SIZE));

            this.square = square;
            this.colours = getBackgroundColours();
            this.isHighlighted = false;

            this.iconLabel = new JLabel();
            add(iconLabel, BorderLayout.CENTER);
        }

        /**
         * @EFFECTS: See {@code JComponent.paintComponent}.
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
            ImageIcon icon = new ImageIcon(Paths.get(PATH + (square.getPiece().getColour() + "/"
                    + square.getPiece().getClass().getSimpleName()).toLowerCase(Locale.ROOT) + EXT).toString());
            return new ImageIcon(icon.getImage().getScaledInstance(getWidth(), getWidth(), Image.SCALE_SMOOTH));
        }
    }
}
