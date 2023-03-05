package ui;

/**
 * The entry point for the program.
 */
public class Main {
    /**
     * @EFFECTS: Starts CPSC Program Similar to Chess.
     */
    public static void main(String[] args) {
        new Menu().displayIntro().displayHelp().start();
    }
}
