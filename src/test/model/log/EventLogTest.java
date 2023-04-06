package model.log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@code EventLog}.
 * From <a href="https://github.students.cs.ubc.ca/CPSC210/AlarmSystem">Alarm System</a>.
 */
public class EventLogTest {
    private Event event;

    /**
     * @EFFECTS: Initializes the event for testing.
     * @MODIFIES: {@code this}
     */
    @BeforeEach
    public void init() {
        event = new Event("This is an event.");
    }

    /**
     * @EFFECTS: Tests {@code EventLog.logEvent}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void logEventTest() {
        EventLog.getInstance().logEvent(event);
        EventLog.getInstance().logEvent(event);

        for (Event e : EventLog.getInstance()) {
            assertSame(event, e);
        }
    }

    /**
     * @EFFECTS: Tests {@code EventLog.clear}.
     * @MODIFIES: {@code this}
     */
    @Test
    public void clearTest() {
        EventLog.getInstance().clear();
        Iterator<Event> iterator = EventLog.getInstance().iterator();

        assertTrue(iterator.hasNext());
        assertEquals("Event log cleared.", iterator.next().getDescription());

        assertFalse(iterator.hasNext());
    }
}