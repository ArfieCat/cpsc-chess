package ui.gui;

import model.Colour;
import model.Move;
import model.board.Board;
import model.board.Square;
import persistence.JsonUtils;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Represents a game of chess graphically.
 */
public class GamePanel extends JPanel {
    private static final String PATH = "./data/.resources/";
    private static final int[] SIZE = {800, 600};

    private final Board board;
    private Colour displayedPlayer;

    private final BoardDisplayPanel boardDisplayPanel;

    /**
     * @EFFECTS: Constructs a new game panel.
     */
    public GamePanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        this.board = new Board();
        this.displayedPlayer = board.getCurrentPlayer();

        this.boardDisplayPanel = new BoardDisplayPanel();
        add(boardDisplayPanel);
        add(new InfoDisplayPanel());
    }

    /**
     * @EFFECTS: Saves the game to a JSON file.
     */
    public void saveFile(String fileName) throws IOException {
        JsonUtils.save(fileName, board.getHistory());
    }

    /**
     * @EFFECTS: Loads an existing game from a JSON file.
     * @MODIFIES: {@code this}
     */
    public void loadFile(String fileName) throws IOException {
        for (Move move : JsonUtils.load(fileName, board)) {
            board.doMove(move);
        }
        boardDisplayPanel.nextOrientation();
    }

    /**
     * @EFFECTS: Returns a PGN-like string representation of the current game.
     */
    public String getDisplayString() {
        List<Move> history = board.getHistory();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < history.size(); i++) {
            if (i % Colour.values().length == 0) {
                stringBuilder.append(i / Colour.values().length + 1).append(". ");
            }
            stringBuilder.append(getMoveDisplayString(history.get(i))).append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * @EFFECTS: Returns a PGN-like string representation of the given move.
     * @REQUIRES: {@code Board.doMove(move)} called
     */
    private String getMoveDisplayString(Move move) {
        // Irregular notation for castling.
        if (move.getFlag(Move.CASTLE)) {
            return move.getEnd().getX() > move.getStart().getX() ? "O-O" : "O-O-O";
        }

        // It's really quite simple.
        return move.getMovedPiece().getPrefix()
                + (move.getStart().getX() != move.getEnd().getX() ? (char) (move.getStart().getX() + 'a') : "")
                + (move.getFlag(Move.CAPTURE) ? "x" : "") + (char) (move.getEnd().getX() + 'a')
                + (char) (move.getEnd().getY() + '1') + (move.getFlag(Move.PROMOTE) ? "=Q" : "");
    }

    /**
     * @EFFECTS: Returns an image icon with the sprite at the given path.
     */
    private static Icon getIconResource(String fileName, int width, int height) {
        ImageIcon icon = new ImageIcon(Paths.get(PATH + fileName + ".png").toString());
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    /**
     * @EFFECTS: Loads and plays the sound effect at the given path.
     */
    private static void playSoundResource(String fileName) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Paths.get(PATH + "sound/" + fileName + ".wav")
                    .toUri().toURL()));
            clip.start();
        } catch (Exception e) { /* Whatever, man. */ }
    }

    /**
     * Represents the board graphically.
     */
    private class BoardDisplayPanel extends JPanel {
        private final SquarePanel[] squarePanels;
        private SquarePanel selection;

        /**
         * @EFFECTS: Constructs a new board display panel with the given params.
         */
        public BoardDisplayPanel() {
            setLayout(new GridLayout(Board.SIZE, Board.SIZE));
            setPreferredSize(new Dimension(GamePanel.SIZE[1], GamePanel.SIZE[1]));

            this.squarePanels = getSquarePanels();
            this.selection = null;

            nextOrientation();
            playSoundResource("game-start");
        }

        /**
         * @EFFECTS: See {@code JComponent.paintComponent}.
         * @MODIFIES: {@code this}
         */
        @Override
        protected void paintComponent(Graphics g) {
            // Determine and set the visibility of each square on the board.
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
                squarePanels[i].setHighlighted(false);
                add(squarePanels[i]);
            }
            revalidate();
        }

        /**
         * @EFFECTS: Ensures that the user input for a move is valid and updates the game.
         * @MODIFIES: {@code this}
         */
        private void validateMove(SquarePanel panel) {
            if (board.isGameOver() || board.getCurrentPlayer() != displayedPlayer) {
                return;
            }

            if (selection == null) {
                panel.setHighlighted(true);
                selection = panel;
            } else {
                Move move = new Move(selection.getSquare(), panel.getSquare());
                if (move.getStart().hasPiece() && move.getStart().getPiece().getColour() == board.getCurrentPlayer()
                        && move.isValid(board)) {
                    displayedPlayer = board.getCurrentPlayer();
                    board.doMove(move);
                    playMoveSoundResource(move);
                }
                selection.setHighlighted(false);
                selection = null;
            }
        }

        /**
         * @EFFECTS: Loads and plays one or more sound effects corresponding to the given move.
         */
        private void playMoveSoundResource(Move move) {
            playSoundResource(board.isGameOver() ? "game-end" : move.getFlag(Move.PROMOTE) ? "promote"
                    : move.getFlag(Move.CASTLE) ? "castle" : move.getFlag(Move.CAPTURE) ? "capture" : "move");
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
    }

    /**
     * Represents game information graphically.
     */
    private class InfoDisplayPanel extends JPanel {
        private final JTextArea historyTextArea;
        private final JButton continueButton;

        /**
         * @EFFECTS: Constructs a new info display panel.
         */
        public InfoDisplayPanel() {
            setLayout(new GridBagLayout());
            setPreferredSize(new Dimension(SIZE[0] - SIZE[1], SIZE[1]));

            this.historyTextArea = new JTextArea(1, 1);
            this.continueButton = new JButton("Continue");
            addNewMessagePanel();
            addComponents();
        }

        /**
         * @EFFECTS: See {@code JComponent.paintComponent}.
         * @MODIFIES: {@code this}
         */
        @Override
        protected void paintComponent(Graphics g) {
            List<Move> history = board.getHistory();
            historyTextArea.setText(null);

            // Disable the continue button if the current player has not moved yet.
            continueButton.setEnabled(board.getCurrentPlayer() != displayedPlayer);

            // Set the text to match the current move history.
            for (int i = 0; i < history.size(); i++) {
                if (history.get(i).getMovedPiece().getColour() == displayedPlayer) {
                    historyTextArea.append((i / Colour.values().length + 1) + ". "
                            + getMoveDisplayString(history.get(i)) + "\n");
                }
            }
            super.paintComponent(g);
        }

        /**
         * @EFFECTS: Creates and adds a new panel for locally sending chat messages.
         */
        private void addNewMessagePanel() {
            JTextArea messageArea = new JTextArea(1, 1);
            messageArea.setEditable(false);
            messageArea.setLineWrap(true);
            messageArea.setWrapStyleWord(true);

            JTextField messageField = new JTextField(1);
            messageField.addActionListener(e -> {
                if (messageField.getText().trim().length() > 0) {
                    messageArea.append(displayedPlayer + ": " + messageField.getText() + "\n");
                    messageField.setText(null);
                }
            });

            JPanel messagePanel = new JPanel(new BorderLayout());
            messagePanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);
            messagePanel.add(messageField, BorderLayout.PAGE_END);

            add(messagePanel, getConstraints(1, 0.5));
        }

        /**
         * @EFFECTS: Adds miscellaneous information-related components.
         * @MODIFIES: {@code this}
         */
        private void addComponents() {
            historyTextArea.setEditable(false);
            historyTextArea.setLineWrap(true);
            historyTextArea.setWrapStyleWord(true);

            continueButton.setIcon(UIManager.getIcon("PasswordField.revealIcon"));
            continueButton.addActionListener(e -> delay());

            add(new JScrollPane(historyTextArea), getConstraints(0, 0.5));
            add(continueButton, getConstraints(2, 0.1));
        }

        /**
         * @EFFECTS: Hides the display panels momentarily as a rudimentary anti-screen-cheating measure.
         * @MODIFIES: {@code this}
         */
        private void delay() {
            setVisible(false);
            boardDisplayPanel.setVisible(false);

            // Update the orientation of the board while it is hidden.
            boardDisplayPanel.nextOrientation();
            Timer timer = new Timer(1000, t -> {
                setVisible(true);
                boardDisplayPanel.setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        }

        /**
         * @EFFECTS: Returns a new layout constraint with the given params.
         */
        private GridBagConstraints getConstraints(int index, double weight) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridy = index;
            constraints.weighty = weight;

            constraints.weightx = 1.0;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(index == 0 ? 0 : Board.SIZE, 0, 0, 0);
            return constraints;
        }
    }

    /**
     * Represents a square on the board graphically.
     */
    private static class SquarePanel extends JPanel {
        private static final Color[] DEFAULT_COLOURS = {new Color(0xB98761), new Color(0xEDD6B0)};
        private static final Color[] HIGHLIGHT_COLOURS = {new Color(0xDCC431), new Color(0xF7EB58)};

        private final Square square;
        private final Color[] colours;
        private boolean isHighlighted;

        private final JLabel iconLabel;

        /**
         * @EFFECTS: Constructs a new square panel.
         */
        public SquarePanel(Square square) {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(SIZE[1] / Board.SIZE, SIZE[1] / Board.SIZE));

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
            // Set the background colour and icon to match the current square.
            setBackground(isHighlighted ? colours[1] : colours[0]);
            iconLabel.setIcon(square.hasPiece() ? getSquareIconResource(square) : null);

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
         * @EFFECTS: Returns an image icon matching the given square.
         */
        private Icon getSquareIconResource(Square square) {
            return getIconResource("piece/" + (square.getPiece().getColour() + "/"
                    + square.getPiece().getClass().getSimpleName()).toLowerCase(Locale.ROOT), getWidth(), getHeight());
        }
    }
}