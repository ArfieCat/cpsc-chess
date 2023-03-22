package ui.gui;

import model.Move;
import model.board.Board;
import persistence.JsonUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Represents a game of chess graphically.
 */
public class GamePanel extends JPanel {
    private static final double SIDEBAR_RATIO = 0.618;

    private final Board board;
    private final BoardDisplayPanel boardDisplayPanel;

    /**
     * @EFFECTS: Constructs a new game panel.
     */
    public GamePanel() {
        super(new GridBagLayout());
        this.board = new Board();
        this.boardDisplayPanel = new BoardDisplayPanel(board);

        add(boardDisplayPanel, getConstraints(0, 0, 3, 1.0, 1.0));
        addNewInfoDisplayPanels();
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
    }

    /**
     * @EFFECTS: Creates and adds several new panels to display game information.
     * @MODIFIES: {@code this}
     */
    private void addNewInfoDisplayPanels() {
        JPanel chatPanel = new JPanel(new BorderLayout());

        JTextArea chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);

        JTextField chatTextField = new JTextField();
        chatTextField.addActionListener(e -> {
            chatTextArea.append(boardDisplayPanel.getDisplayedPlayer() + ": " + chatTextField.getText() + "\n");
            chatTextField.setText(null);
        });

        chatPanel.add(new JScrollPane(chatTextArea), BorderLayout.CENTER);
        chatPanel.add(chatTextField, BorderLayout.PAGE_END);

        // ...

        add(chatPanel, getConstraints(1, 2, 1, SIDEBAR_RATIO + 1, 1.0));
    }

    /**
     * @EFFECTS: Returns a layout constraints object with the given params.
     */
    private GridBagConstraints getConstraints(int x, int y, int h, double rx, double ry) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridheight = h;
        constraints.weightx = rx;
        constraints.weighty = ry;

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;

        return constraints;
    }
}