package persistence;

import model.Move;
import model.board.Board;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Static helper functions for reading and writing a list of moves to JSON.
 */
public class JsonUtil {
    private static final String PATH = "./data/";
    private static final String EXT = ".cpsc";

    /**
     * @EFFECTS: Loads a list of moves from the JSON file at the given path.
     */
    public static List<Move> load(String fileName, Board board) throws IOException {
        String jsonString = Files.readString(Path.of(PATH + fileName + EXT));
        return fromJson(new JSONObject(jsonString), board);
    }

    /**
     * @EFFECTS: Saves a list of moves to the JSON file at the given path.
     */
    public static void save(String fileName, List<Move> moves) throws IOException {
        Files.write(Path.of(PATH + fileName + EXT), toJson(moves).toString().getBytes());
    }

    /**
     * @EFFECTS: Converts the given JSON object into a list of moves.
     */
    private static List<Move> fromJson(JSONObject jsonObject, Board board) {
        JSONArray json = jsonObject.getJSONArray("moves");
        List<Move> moves = new LinkedList<>();
        for (int i = 0; i < json.length(); i++) {
            JSONArray data = json.getJSONArray(i);
            moves.add(new Move(board.getSquare(data.getInt(0), data.getInt(1)),
                    board.getSquare(data.getInt(2), data.getInt(3))));
        }
        return moves;
    }

    /**
     * @EFFECTS: Converts the given list of moves into a JSON object.
     */
    private static JSONObject toJson(List<Move> moves) {
        JSONArray json = new JSONArray();
        for (Move move : moves) {
            json.put(new JSONArray().put(move.getStart().getX()).put(move.getStart().getY())
                    .put(move.getEnd().getX()).put(move.getEnd().getY()));
        }
        return new JSONObject().put("moves", json);
    }
}
