package ui.cli;

import java.io.IOException;
import java.util.Scanner;

/**
 * Represents the main menu via command line interface.
 */
public class ConsoleUI {
    private final Scanner scanner;

    /**
     * @EFFECTS: Constructs a new menu.
     */
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * @EFFECTS: Starts the menu command line interface.
     */
    public void start() {
        while (true) {
            String[] input = scanner.nextLine().trim().toLowerCase().split(" ", 2);

            switch (input[0]) {
                case "play":
                    new ConsoleGame(scanner).displayBoard().displayHelp().start();
                    break;
                case "load":
                    loadFile(input);
                    break;
                case "help":
                    displayHelp();
                    break;
                case "quit":
                    System.exit(0); // die
                    break;
                default:
                    System.out.println("[!] Input a valid command.");
            }
        }
    }

    /**
     * @EFFECTS: Prints out a list of valid commands, and returns {@code this} for chaining.
     */
    public ConsoleUI displayHelp() {
        String string = "play             | Start a new game. \n"
                + "load <file-name> | Load an existing game. \n"
                + "help             | See valid commands. \n"
                + "quit             | Quit.";

        System.out.println(string);
        return this;
    }

    /**
     * @EFFECTS: Prints out the intro blurb, and returns {@code this} for chaining.
     */
    public ConsoleUI displayIntro() {
        // It looks better without the extra backslashes...
        String string = "_________ __________  __________________ \n"
                + "\\_   ___ \\\\______   \\/   _____/\\_   ___ \\ \n"
                + "/    \\  \\/ |     ___/\\_____  \\ /    \\  \\/ \n"
                + "\\     \\____|    |    /        \\\\     \\____ \n"
                + " \\______  /|____|   /_______  / \\______  / \n"
                + "        \\/                  \\/         \\/";

        System.out.println(string);
        System.out.println("CPSC Program Similar to Chess.");
        return this;
    }

    /**
     * @EFFECTS: Loads an existing game from a JSON file.
     */
    private void loadFile(String[] input) {
        if (input.length != 2) {
            System.out.println("[!] Command did not match: load <file-name>");
            return;
        }

        try {
            new ConsoleGame(scanner).loadFile(input[1]).displayBoard().displayHelp().start();
        } catch (IOException e) {
            System.out.println("[!] File does not exist: " + input[1]);
        } catch (RuntimeException e) {
            System.out.println("[!] Something went wrong.");
        }
    }
}
