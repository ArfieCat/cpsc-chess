package model.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a log of program events as a singleton.
 * From <a href="https://github.students.cs.ubc.ca/CPSC210/AlarmSystem">Alarm System</a>.
 */
public class EventLog implements Iterable<Event> {
    private static EventLog theLog;
    private final Collection<Event> events;

    /**
     * @EFFECTS: Constructs a new event log.
     * @REQUIRES: External instantiation of a singleton is not allowed.
     */
    private EventLog() {
        this.events = new ArrayList<>();
    }

    /**
     * @EFFECTS: Lazily instantiates and returns the singleton.
     */
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }
        return theLog;
    }

    /**
     * @EFFECTS: Adds an event to the event log.
     */
    public void logEvent(Event e) {
        events.add(e);
    }

    /**
     * @EFFECTS: Clears the event log and logs the event.
     */
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    /**
     * @EFFECTS: See {@code Iterable.iterator}.
     */
    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}