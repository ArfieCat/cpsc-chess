package ui.cli;

import model.Colour;
import model.Move;
import model.board.Board;
import model.board.Square;
import model.piece.Pawn;
import persistence.JsonUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

/**
 * Represents a game of chess via command line interface.
 */
public class ConsoleGame {
    private final Scanner scanner;
    private final Board board;

    /**
     * @EFFECTS: Constructs a new game.
     */
    public ConsoleGame(Scanner scanner) {
        this.scanner = scanner;
        this.board = new Board();
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
    public ConsoleGame displayHelp() {
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
    public ConsoleGame displayBoard() {
        System.out.println(getDisplayString(board.getCurrentPlayer()));

        if (board.isGameOver()) {
            Colour previousPlayer = Colour.values()[(board.getHistory().size() - 1) % Colour.values().length];
            System.out.println("[@] King captured. " + previousPlayer + " wins.");
        } else {
            System.out.println("[@] " + board.getCurrentPlayer() + " to play.");
        }
        return this;
    }

    /**
     * @EFFECTS: Loads an existing game from a JSON file, and returns {@code this} for chaining.
     * @MODIFIES: {@code this}
     */
    public ConsoleGame loadFile(String fileName) throws IOException {
        for (Move move : JsonUtils.load(fileName, board)) {
            board.doMove(move);
        }
        return this;
    }

    /**
     * @EFFECTS: Returns a string representation of the board for display.
     */
    private String getDisplayString(Colour colour) {
        Set<Square> visibleSquares = board.getVisibleSquares(colour);
        StringBuilder stringBuilder = new StringBuilder();

        // Iterate up or down depending on the current player to visually "flip" the board.
        for (int y = colour.getDirection() < 0 ? 0 : Board.SIZE - 1; !board.isOutOfBounds(0, y);
                y -= colour.getDirection()) {
            stringBuilder.append(y + 1).append("  ");
            for (int x = colour.getDirection() > 0 ? 0 : Board.SIZE - 1; !board.isOutOfBounds(x, 0);
                    x += colour.getDirection()) {
                stringBuilder.append(getDisplaySymbol(visibleSquares, board.getSquare(x, y), colour)).append("  ");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("   ");
        for (int x = colour.getDirection() > 0 ? 0 : Board.SIZE - 1; !board.isOutOfBounds(x, 0);
                x += colour.getDirection()) {
            stringBuilder.append((char) (x + 'a')).append("  ");
        }
        return stringBuilder.toString();
    }

    /**
     * @EFFECTS: Returns a string representation of the given square.
     */
    private String getDisplaySymbol(Set<Square> visibleSquares, Square square, Colour colour) {
        if (visibleSquares.contains(square) && square.hasPiece()) {
            String symbol = square.getPiece() instanceof Pawn ? "P" : square.getPiece().getPrefix();
            return square.getPiece().getColour() == colour ? symbol.toUpperCase() : symbol.toLowerCase();
        }
        return visibleSquares.contains(square) ? "." : " ";
    }

    /**
     * @EFFECTS: Ensures that the user input for a move is well-formed and updates the game.
     * @MODIFIES: {@code this}
     */
    private void parseMove(String[] input) {
        if (board.isGameOver()) {
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
        if (!start.hasPiece() || start.getPiece().getColour() != board.getCurrentPlayer()) {
            System.out.println("[!] Not a " + board.getCurrentPlayer() + " piece.");
            return;
        }

        Move move = new Move(start, board.getSquare(args[1].charAt(0) - 'a', args[1].charAt(1) - '1'));
        if (!move.isValid(board)) {
            System.out.println("[!] Illegal move: " + start.getPiece().getPrefix() + " " + args[0] + " " + args[1]);
            return;
        }

        board.doMove(move);
        if (!board.isGameOver()) {
            delay();
        }

        displayBoard();
    }

    /**
     * @EFFECTS: Prints out whitespace and waits for any input as a rudimentary anti-screen-cheating measure.
     */
    private void delay() {
        System.out.println(new String(new char[50]).replace("\0", "\n"));
        System.out.println("[@] Pass the device to " + board.getCurrentPlayer() + ", then press ENTER to continue.");
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

        try {
            JsonUtils.save(input[1], board.getHistory());
            System.out.println("[@] Game successfully saved as: " + input[1]);
        } catch (IOException e) {
            System.out.println("[!] Illegal file name: " + input[1]);
        } catch (RuntimeException e) {
            System.out.println("[!] Something went wrong.");
        }
    }
}