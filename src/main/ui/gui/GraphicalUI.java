package ui.gui;

import persistence.JsonUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Represents the graphical user interface.
 */
public class GraphicalUI {
    private static final String THEME = "javax.swing.plaf.nimbus.NimbusLookAndFeel";

    private final JFrame frame;
    private GamePanel gamePanel;

    /**
     * @EFFECTS: Starts the graphical user interface on the event dispatch thread.
     */
    public static void start() {
        try {
            UIManager.setLookAndFeel(THEME);
        } catch (Exception e) { /* Whatever, man. */ }
        SwingUtilities.invokeLater(GraphicalUI::new);
    }

    /**
     * @EFFECTS: Constructs a new graphical UI and displays it on-screen.
     */
    private GraphicalUI() {
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

        JButton newButton = new JButton("New Game");
        newButton.addActionListener(e -> newGame());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveFile());

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadFile());

        toolBar.add(newButton);
        toolBar.add(saveButton);
        toolBar.add(loadButton);

        return toolBar;
    }

    private void newGame() {
        int input = JOptionPane.showConfirmDialog(null, "Unsaved changes will be discarded.",
                "New Game", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (input == JOptionPane.OK_OPTION) {
            frame.remove(gamePanel);
            gamePanel = new GamePanel();

            frame.add(gamePanel);
            frame.revalidate();
        }
    }

    private void saveFile() {
        String input = JOptionPane.showInputDialog(null, "Input a file name.",
                "Save", JOptionPane.PLAIN_MESSAGE);

        if (input != null) {
            try {
                JsonUtils.save(input, gamePanel.getBoardHistory());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Illegal file name: " + input,
                        "Save", JOptionPane.PLAIN_MESSAGE);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null, "Something went wrong.",
                        "Save", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    private void loadFile() {
        String input = JOptionPane.showInputDialog(null, "Input a file name.",
                "Load", JOptionPane.PLAIN_MESSAGE);

        if (input != null) {
            try {
                frame.remove(gamePanel);
                gamePanel = new GamePanel();
                gamePanel.loadFile(input);

                frame.add(gamePanel);
                frame.revalidate();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "File does not exist: " + input,
                        "Load", JOptionPane.PLAIN_MESSAGE);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null, "Something went wrong.",
                        "Load", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
}