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
     * @EFFECTS: Constructs a new Game with given params.
     */
    public Game(Scanner scanner) {
        this.scanner = scanner;
        this.board = new Board();
        this.moveCount = 0;
        this.isGameOver = false;
    }

    /**
     * @EFFECTS: Starts the game command line interface.
     */
    public void start() {
        while (true) {
            // Split user input into command and args.
            String[] input = scanner.nextLine().trim().toLowerCase().split(" ", 2);

            switch (input[0]) {
                case "move":
                    parseMove(input);
                    break;
                case "help":
                    displayHelp();
                    break;
                case "save":
                    saveFile(input);
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
    public Game displayHelp() {
        String string = "move <start> <end> | Move a piece. \n"
                + "help               | See valid commands. \n"
                + "save <file-name>   | Save the current game. \n"
                + "quit               | Quit.";

        System.out.println(string);
        return this;
    }

    /**
     * @EFFECTS: Prints out the board, and returns {@code this} for chaining.
     */
    public Game displayBoard() {
        Colour currentPlayer = PLAYERS[moveCount % PLAYERS.length];
        System.out.println(board.getDisplayString(currentPlayer));
        if (isGameOver) {
            System.out.println("King captured. " + PLAYERS[(moveCount - 1) % PLAYERS.length] + " wins.");
        } else {
            System.out.println(currentPlayer + " to play.");
        }
        return this;
    }

    /**
     * @EFFECTS: Sets all pieces in their starting positions, and returns {@code this} for chaining.
     * @MODIFIES: {@code this}
     */
    public Game setupBoard() {
        for (Colour colour : PLAYERS) {
            board.setupPieces(colour);
        }
        return this;
    }

    /**
     * @EFFECTS: Loads an existing game from a JSON file, and returns {@code this} for chaining.
     * @MODIFIES: {@code this}
     */
    public Game loadFile(String path) {
        // TODO: instantiate moves from json
        int die = (new int[1])[1];
        return this;
    }

    /**
     * @EFFECTS: Ensures that the user input for a move is well-formed and updates the game.
     * @MODIFIES: {@code this}
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
     * @EFFECTS: Ensures that the user input for a move is valid and updates the game.
     * @MODIFIES: {@code this}
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
            System.out.println("[!] Illegal move: " + start.getPiece().getPrefix() + " " + args[0] + " " + args[1]);
            return;
        }

        // As a side effect this updates the board.
        isGameOver = board.doMove(move);
        moveCount++;
        if (!isGameOver) {
            delay();
        }
        displayBoard();
    }

    /**
     * @EFFECTS: Prints out whitespace and waits for any input as a rudimentary anti-screen-cheating measure.
     */
    private void delay() {
        System.out.println(new String(new char[50]).replace("\0", "\n"));
        System.out.println("Pass the device to " + PLAYERS[moveCount % PLAYERS.length]
                + ", then press ENTER to continue.");
        scanner.nextLine();
    }

    /**
     * @EFFECTS: Saves the game to a JSON file.
     */
    private void saveFile(String[] input) {
        if (input.length != 2) {
            System.out.println("[!] Command did not match: save <file-name>");
            return;
        }
        // TODO: save to file
    }
}
