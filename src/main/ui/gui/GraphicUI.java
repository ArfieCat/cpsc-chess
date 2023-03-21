package ui.gui;

import com.formdev.flatlaf.FlatLightLaf;
import persistence.JsonUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Represents the graphical user interface.
 */
public class GraphicUI {
    private final JFrame frame;
    private GamePanel gamePanel;

    /**
     * @EFFECTS: Starts the graphical user interface on the event dispatch thread.
     */
    public static void start() {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(GraphicUI::new);
    }

    /**
     * @EFFECTS: Constructs a new graphical UI and displays it on-screen.
     */
    private GraphicUI() {
        this.gamePanel = new GamePanel();
        this.frame = setupFrame();
        frame.setVisible(true);
    }

    /**
     * @EFFECTS: Sets attributes and adds components to a new frame, and returns it.
     */
    private JFrame setupFrame() {
        JFrame frame = new JFrame("CPSC Program Similar to Chess");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(setupToolBar(), BorderLayout.PAGE_START);
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.pack();
        return frame;
    }

    /**
     * @EFFECTS: Sets attributes and adds components to a new toolbar, and returns it.
     */
    private JToolBar setupToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // Disgusting use of magic strings to get bundled icons here...
        JButton newButton = new JButton(UIManager.getIcon("FileChooser.listViewIcon"));
        newButton.addActionListener(e -> newGame());

        JButton saveButton = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));
        saveButton.addActionListener(e -> saveFile());

        JButton loadButton = new JButton(UIManager.getIcon("Tree.openIcon"));
        loadButton.addActionListener(e -> loadFile());

        toolBar.add(newButton);
        toolBar.addSeparator();
        toolBar.add(saveButton);
        toolBar.add(loadButton);

        return toolBar;
    }

    /**
     * @EFFECTS: Starts a new game and replaces the current game panel.
     * @MODIFIES: {@code this}
     */
    private void newGame() {
        if (JOptionPane.showConfirmDialog(frame, "Unsaved changes will be discarded.", "New Game",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
            frame.remove(gamePanel);
            gamePanel = new GamePanel();

            frame.add(gamePanel);
            frame.revalidate();
        }
    }

    /**
     * @EFFECTS: Saves the game to a JSON file.
     */
    private void saveFile() {
        String input = JOptionPane.showInputDialog(frame, "Input a file name.", "Save Game",
                JOptionPane.QUESTION_MESSAGE);
        if (input != null) {
            try {
                JsonUtils.save(input, gamePanel.getBoardHistory());
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
        String input = JOptionPane.showInputDialog(frame, "Input a file name.", "Load Game",
                JOptionPane.QUESTION_MESSAGE);
        if (input != null) {
            try {
                frame.remove(gamePanel);
                gamePanel = new GamePanel();
                gamePanel.loadFile(input);

                frame.add(gamePanel);
                frame.revalidate();
            } catch (IOException e) {
                showWarningDialog("File does not exist: " + input);
            } catch (RuntimeException e) {
                showWarningDialog("Something went wrong.");
            }
        }
    }

    /**
     * @EFFECTS: Displays a warning dialog after a short delay (because instant popups are unnerving).
     */
    private void showWarningDialog(String message) {
        Timer timer = new Timer(100, e -> JOptionPane.showMessageDialog(frame, message, null,
                JOptionPane.WARNING_MESSAGE));
        timer.setRepeats(false);
        timer.start();
    }
}