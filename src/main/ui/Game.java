package ui;

import model.Colour;
import model.Move;
import model.board.Board;
import model.board.Square;

import java.util.Scanner;

/**
 * Represents a game of chess.
 */
public class Game {
    private static final Colour[] PLAYERS = {Colour.WHITE, Colour.BLACK};
    private final Scanner scanner;
    private final Board board;
    private int moveCount;
    private boolean isGameOver;

    /**
     * EFFECTS: Constructs a new Game with given params.
     */
    public Game(Scanner scanner) {
        this.scanner = scanner;
        this.board = new Board();
        this.moveCount = 0;
        this.isGameOver = false;
    }

    /**
     * EFFECTS: Starts the game command line interface.
     */
    @SuppressWarnings({"methodlength"}) // This method can't get any shorter.
    public void start() {
        while (true) {
            // Split user input into command and args.
            String[] input = scanner.nextLine().trim().toLowerCase().split(" ", 2);

            switch (input[0]) {
                case "move": {
                    parseMove(input);
                    break;
                }
                case "help": {
                    displayHelp();
                    break;
                }
                case "save": {
                    saveFile(input);
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
        System.out.println(board);

        if (isGameOver) {
            System.out.println("King captured. " + PLAYERS[(moveCount - 1) % PLAYERS.length] + " wins.");
        } else {
            System.out.println(PLAYERS[moveCount % PLAYERS.length] + " to play.");
        }

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
        // TODO: instantiate moves from json
        int die = (new int[1])[1];
        return this;
    }

    /**
     * EFFECTS: Ensures that the user input for a move is well-formed.
     */
    private void parseMove(String[] input) {
        if (isGameOver) {
            System.out.println("[!] The game has ended.");
            return;
        }

        // Check if the user input has exactly two args.
        if (input.length != 2 || input[1].split(" ", 2).length != 2) {
            System.out.println("[!] Command did not match: move <start> <end>");
            return;
        }

        validateMove(input[1].split(" ", 2));
    }

    /**
     * EFFECTS: Ensures that the user input for a move is valid.
     */
    private void validateMove(String[] args) {
        for (String arg : args) {
            if (arg.length() != 2 || board.isOutOfBounds(arg.charAt(0) - 'a', arg.charAt(1) - '1')) {
                System.out.println("[!] Not a square: " + arg);
                return;
            }
        }

        Square start = board.getSquare(args[0].charAt(0) - 'a', args[0].charAt(1) - '1');
        Colour currentPlayer = PLAYERS[moveCount % PLAYERS.length];
        if (!start.hasPiece() || start.getPiece().getColour() != currentPlayer) {
            System.out.println("[!] Not a " + currentPlayer + " piece.");
            return;
        }

        Move move = new Move(start, board.getSquare(args[1].charAt(0) - 'a', args[1].charAt(1) - '1'));
        if (!move.isValid(board)) {
            System.out.println("[!] Illegal move: " + start.getPiece().toString() + " " + args[0] + " " + args[1]);
            return;
        }

        doMove(move);
    }

    /**
     * EFFECTS: Updates the game according to the given move.
     */
    private void doMove(Move move) {
        isGameOver = board.doMove(move);
        moveCount++;
        displayBoard();
    }

    /**
     * EFFECTS: Saves the game to a JSON file.
     */
    private void saveFile(String[] input) {
        if (input.length != 2) {
            System.out.println("[!] Command did not match: save <file-name>");
            return;
        }
        // TODO: save to file
    }
}