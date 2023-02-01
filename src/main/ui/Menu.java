package ui;

import java.util.Scanner;

/**
 * Represents the main menu for the program.
 */
public class Menu {
    private final Scanner scanner;

    /**
     * EFFECTS: Constructs a new Menu.
     */
    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * EFFECTS: Starts the menu command line interface.
     */
    public void start() {
        String[] command = scanner.nextLine().trim().toLowerCase().split(" ");

        switch (command[0]) {
            case "p": case "play": {
                new Game(scanner).setupBoard().displayHelp().nextMove();
                break;
            }
            case "l": case "load": {
                new Game(scanner).load().displayHelp().nextMove();
                break;
            }
            case "h": case "help": {
                displayHelp();
                break;
            }
            case "q": case "quit": {
                System.exit(0); // die
                break;
            }
            default: {
                System.out.println("[!] Input a valid command.");
                start();
            }
        }
    }

    /**
     * EFFECTS: Prints out a list of valid commands.
     */
    public Menu displayHelp() {
        System.out.println("play               | Start a new game.");
        System.out.println("load <path-to-csf> | Load an existing game.");
        System.out.println("help               | See valid commands.");
        System.out.println("quit               | Quit.");

        return this;
    }

    /**
     * EFFECTS: Prints out the intro blurb.
     */
    public Menu displayIntro() {
        System.out.println("CPSC Program Similar to Chess");
        // TODO: cool title

        return this;
    }
}
