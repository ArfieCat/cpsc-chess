package ui.gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import model.board.Board;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Represents the graphical user interface.
 */
public class GraphicUI extends JFrame {
    private GamePanel currentGamePanel;

    /**
     * @EFFECTS: Starts the graphical user interface on the event dispatch thread.
     */
    public static void start() {
        FlatMacLightLaf.setup();
        SwingUtilities.invokeLater(GraphicUI::new);
    }

    /**
     * @EFFECTS: Constructs a new graphical UI and displays it on-screen.
     */
    private GraphicUI() {
        super("CPSC Program Similar to Chess");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        addNewToolbar();
        replaceGamePanel(null);

        pack();
        setVisible(true);
    }

    /**
     * @EFFECTS: Creates and adds a new toolbar containing menu options.
     * @MODIFIES: {@code this}
     */
    private void addNewToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // Disgusting use of magic strings to get bundled icons here...
        JButton newButton = new JButton(UIManager.getIcon("FileChooser.listViewIcon"));
        newButton.addActionListener(e -> newGame());

        JButton saveButton = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));
        saveButton.addActionListener(e -> saveFile());

        JButton loadButton = new JButton(UIManager.getIcon("Tree.openIcon"));
        loadButton.addActionListener(e -> loadFile());

        JButton copyButton = new JButton(UIManager.getIcon("FileChooser.detailsViewIcon"));
        copyButton.addActionListener(e -> copyGame());

        toolBar.add(newButton);
        toolBar.addSeparator();
        toolBar.add(saveButton);
        toolBar.add(loadButton);
        toolBar.add(copyButton);

        add(toolBar, BorderLayout.PAGE_START);
    }

    /**
     * @EFFECTS: Replaces the current game panel with a new one using the given path, if applicable.
     * @MODIFIES: {@code this}
     */
    private void replaceGamePanel(String fileName) {
        GamePanel newGamePanel = new GamePanel();

        // Check if the given file can be loaded before replacing the current one.
        if (fileName != null) {
            try {
                newGamePanel.loadFile(fileName);
            } catch (IOException e) {
                showWarningDialog("File does not exist: " + fileName);
            } catch (RuntimeException e) {
                showWarningDialog("Something went wrong.");
            }
        }

        // Replace the current game panel with the new one.
        if (currentGamePanel != null) {
            remove(currentGamePanel);
        }
        currentGamePanel = newGamePanel;
        add(currentGamePanel, BorderLayout.CENTER);
        revalidate();
    }

    /**
     * @EFFECTS: Starts a new game and replaces the current game panel.
     * @MODIFIES: {@code this}
     */
    private void newGame() {
        if (JOptionPane.showConfirmDialog(this, "Unsaved changes will be discarded.",
                "New Game", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
            replaceGamePanel(null);
        }
    }

    /**
     * @EFFECTS: Saves the game to a JSON file.
     */
    private void saveFile() {
        String input = JOptionPane.showInputDialog(this, "Input a file name.",
                "Save Game", JOptionPane.QUESTION_MESSAGE);
        if (input != null) {
            try {
                currentGamePanel.saveFile(input);
            } catch (IOException e) {
                showWarningDialog("Illegal file name: " + input);
            } catch (RuntimeException e) {
                showWarningDialog("Something went wrong.");
            }
        }
    }

    /**
     * @EFFECTS: Loads an existing game from a JSON file and replaces the current game panel.
     * @MODIFIES: {@code this}
     */
    private void loadFile() {
        String input = JOptionPane.showInputDialog(this, "Input a file name.",
                "Load Game", JOptionPane.QUESTION_MESSAGE);
        if (input != null) {
            replaceGamePanel(input);
        }
    }

    /**
     * @EFFECTS: Displays a PGN-like string representation of the current game.
     * @MODIFIES: {@code this}
     */
    private void copyGame() {
        JTextArea textArea = new JTextArea(Board.SIZE, 1);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(currentGamePanel.getDisplayString());

        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Copy Game",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * @EFFECTS: Displays a warning dialog after a short delay (because instant popups are unnerving).
     */
    private void showWarningDialog(String message) {
        Timer timer = new Timer(100, t -> JOptionPane.showMessageDialog(this, message,
                null, JOptionPane.WARNING_MESSAGE));
        timer.setRepeats(false);
        timer.start();
    }
}