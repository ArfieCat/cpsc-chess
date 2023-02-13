package ui;

import java.util.Scanner;

/**
 * Represents the main menu for the program.
 */
public class Menu {
    private final Scanner scanner;

    /**
     * @EFFECTS: Constructs a new Menu.
     */
    public Menu() {
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
                    new Game(scanner).setupBoard().displayBoard().displayHelp().start();
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
    public Menu displayHelp() {
        String string = "play             | Start a new game. \n"
                + "load <file-path> | Load an existing game. \n"
                + "help             | See valid commands. \n"
                + "quit             | Quit.";

        System.out.println(string);
        return this;
    }

    /**
     * @EFFECTS: Prints out the intro blurb, and returns {@code this} for chaining.
     */
    public Menu displayIntro() {
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
            System.out.println("[!] Command did not match: load <file-path>");
            return;
        }

        // Catch any exceptions caused by a faulty path or JSON file.
        try {
            new Game(scanner).loadFile(input[1]).displayBoard().displayHelp().start();
        } catch (Exception e) {
            System.out.println("[!] Could not load file from path: " + input[1]);
        }
    }
}
