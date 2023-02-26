package persistence;

import model.Colour;
import model.Move;
import model.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilsTest {
    private static final String FILE_NAME = "json-utils-test";
    private Board board;
    private List<Move> moves;

    @BeforeEach
    public void loadTest() {
        board = new Board();
        board.setupPieces(Colour.WHITE);
        board.setupPieces(Colour.BLACK);

        try {
            moves = JsonUtils.load(FILE_NAME, board);
            assertEquals(14, moves.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void saveTest() {
        try {
            JsonUtils.save(FILE_NAME, moves);
            List<Move> loadedMoves = JsonUtils.load(FILE_NAME, board);
            for (int i = 0; i < moves.size(); i++) {
                assertEquals(loadedMoves.get(i).getStart().getPiece().getClass(),
                        moves.get(i).getStart().getPiece().getClass());
            }
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void loadTestException() {
        assertThrows(Exception.class, () -> JsonUtils.load("", board));
    }

    @Test
    public void saveTestException() {
        assertThrows(Exception.class, () -> JsonUtils.save("\0", moves));
    }
}
