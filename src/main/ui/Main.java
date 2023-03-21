package ui;

import ui.cli.ConsoleUI;
import ui.gui.GraphicUI;

/**
 * The entry point for the program.
 */
public class Main {
    /**
     * @EFFECTS: Starts CPSC Program Similar to Chess.
     */
    public static void main(String[] args) {
        GraphicUI.start();
        new ConsoleUI().displayIntro().displayHelp().start();
    }
}
