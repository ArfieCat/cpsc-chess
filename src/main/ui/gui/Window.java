package ui.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the top-level container for the program.
 */
public class Window extends JFrame {
    private final JToolBar menuBar;
    private final JPanel gamePanel;

    /**
     * @EFFECTS: Constructs a new window.
     */
    public Window() {
        this.menuBar = new JToolBar();
        this.gamePanel = new GamePanel();

        inflate();
    }

    /**
     * @EFFECTS: Starts the graphical interface.
     * @MODIFIES: {@code this}
     */
    public void start() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pack();
        setVisible(true);
    }

    /**
     * @EFFECTS: Sets properties and adds components to the window.
     * @MODIFIES: {@code this}
     */
    private void inflate() {
        setTitle("CPSC Program Similar to Chess");
        add(menuBar, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
    }
}
