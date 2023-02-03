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
    private boolean gameOver;

    /**
     * EFFECTS: Constructs a new Game with given params.
     */
    public Game(Scanner scanner) {
        this.scanner = scanner;
        this.board = new Board();
        this.moveCount = 0;
        this.gameOver = false;
    }

    /**
     * EFFECTS: Starts the game command line interface.
     */
    public void start() {
        while (true) {
            // Split user input into command and args.
            String[] input = scanner.nextLine().trim().toLowerCase().split(" ", 2);

            switch (input[0]) {
                case "move": {
                    parseMove(input[1].split(" "));
                    break;
                }
                case "help": {
                    displayHelp();
                    break;
                }
                case "save": {
                    saveFile(input[1]);
                    break;
                }
                case "quit": {
                    System.exit(0); // die
                    break;
                }
                default: {
                    System.out.println("[!] Input a valid command.");
                }
            }
        }
    }

    /**
     * EFFECTS: Prints out a list of valid commands and returns this game for chaining.
     */
    public Game displayHelp() {
        System.out.println("move <start> <end> | Move a piece.");
        System.out.println("help               | See valid commands.");
        System.out.println("save <file-name>   | Save the current game.");
        System.out.println("quit               | Quit.");

        return this;
    }

    /**
     * EFFECTS: Prints out the board and returns this game for chaining.
     */
    public Game displayBoard() {
        // TODO: board to string
        System.out.print(board);
        System.out.println(PLAYERS[moveCount % PLAYERS.length] + " to play.");

        return this;
    }

    /**
     * EFFECTS: Sets all pieces in their starting positions and returns this game for chaining.
     * MODIFIES: this
     */
    public Game setupBoard() {
        for (Colour colour : PLAYERS) {
            board.setupPieces(colour);
        }

        return this;
    }

    /**
     * EFFECTS: Loads an existing game from a JSON file and returns this game for chaining.
     * MODIFIES: this
     */
    public Game loadFile(String path) {
        // TODO: load from json
        return this;
    }

    /**
     * EFFECTS: Constructs a move from command represents a valid move and makes that move.
     */
    private void parseMove(String[] args) {
        if (gameOver) {
            System.out.println("[!] The game has ended.");
            return;
        }

        // TODO: check that the input is well formed
    }

    /**
     * EFFECTS: Saves the game to a JSON file.
     */
    private void saveFile(String path) {
        // TODO: save game
    }
}