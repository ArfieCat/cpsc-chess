package model.log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code Event}.
 * From <a href="https://github.students.cs.ubc.ca/CPSC210/AlarmSystem">Alarm System</a>.
 */
public class EventTest {
    private Event event;
    private Date date;

    /**
     * @EFFECTS: Initializes the event for testing.
     * @MODIFIES: {@code this}
     */
    @BeforeEach
    public void init() {
        event = new Event("This is an event.");
        date = Calendar.getInstance().getTime();
    }

    /**
     * @EFFECTS: Tests {@code Event.new}.
     */
    @Test
    public void initTest() {
        assertEquals("This is an event.", event.getDescription());
        assertTrue(Math.abs(date.getTime() - event.getDate().getTime()) < 10);
    }

    /**
     * @EFFECTS: Tests {@code Event.equals} and {@code Event.hashCode}.
     */
    @Test
    public void equalsTest() {
        Event e = new Event("This is a different event.");

        assertNotEquals(event, e);
        assertNotEquals(event.hashCode(), e.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(event.getDate().toString() + "\nThis is an event.", event.toString());
    }
}