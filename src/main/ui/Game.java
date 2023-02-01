package ui;

import model.Colour;
import model.board.Board;

import java.util.Scanner;

/**
 * Represents a game of chess.
 */
public class Game {
    private static final Colour[] PLAYERS = {Colour.WHITE, Colour.BLACK};
    private final Scanner scanner;
    private final Board board;
    private int moveCount;

    /**
     * EFFECTS: Constructs a new Game with given params.
     */
    public Game(Scanner scanner) {
        this.scanner = scanner;
        this.board = new Board();
        this.moveCount = 0;
    }

    /**
     * EFFECTS: Sets all pieces in their starting positions on the board.
     * MODIFIES: this
     */
    public Game setupBoard() {
        for (Colour colour : PLAYERS) {
            board.setupPieces(colour);
        }
        return this;
    }

    /**
     * EFFECTS: Starts the game command line interface.
     */
    public void start() {
        String[] command = scanner.nextLine().trim().toLowerCase().split(" ");

        switch (command[0]) {
            case "m": case "move": {
                nextMove();
                return;
            }
            case "h": case "help": {
                displayHelp();
                return;
            }
            case "s": case "save": {
                save();
                return;
            }
            default: {
                System.out.println("[!] Input a valid command.");
                start();
            }
        }
    }

    /**
     * EFFECTS: Starts the next move for the current player.
     */
    public void nextMove() {
        Colour currentPlayer = PLAYERS[moveCount % PLAYERS.length];

        // TODO: handle moves
    }

    /**
     * EFFECTS: Loads an existing game from a JSON file.
     * MODIFIES: this
     */
    public Game load() {
        // TODO: load game
        return this;
    }

    /**
     * EFFECTS: Saves the game to a JSON file.
     */
    public void save() {
        // TODO: save game
    }

    /**
     * EFFECTS: Prints out a list of valid commands.
     */
    public Game displayHelp() {
        System.out.println("move <start> <end> | Move a piece.");
        System.out.println("help               | See valid commands.");
        System.out.println("save <file-name>   | Save and quit.");

        return this;
    }
}