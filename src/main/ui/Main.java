package ui;

import ui.cli.ConsoleUI;
import ui.gui.GraphicalUI;

/**
 * The entry point for the program.
 */
public class Main {
    /**
     * @EFFECTS: Starts CPSC Program Similar to Chess.
     */
    public static void main(String[] args) {
        GraphicalUI.start();
        new ConsoleUI().displayIntro().displayHelp().start();
    }
}
