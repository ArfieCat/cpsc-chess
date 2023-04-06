package model.log;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents a program event.
 * From <a href="https://github.students.cs.ubc.ca/CPSC210/AlarmSystem">Alarm System</a>.
 */
public class Event {
    private static final int HASH_CONSTANT = 13;
    private final Date dateLogged;
    private final String description;

    /**
     * @EFFECTS: Constructs a new event with the given params.
     */
    public Event(String description) {
        this.dateLogged = Calendar.getInstance().getTime();
        this.description = description;
    }

    public Date getDate() {
        return dateLogged;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @EFFECTS: See {@code Object.equals}.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Event otherEvent = (Event) other;

        return (this.dateLogged.equals(otherEvent.dateLogged) && this.description.equals(otherEvent.description));
    }

    /**
     * @EFFECTS: See {@code Object.hashCode}.
     */
    @Override
    public int hashCode() {
        return (HASH_CONSTANT * dateLogged.hashCode() + description.hashCode());
    }

    /**
     * @EFFECTS: See {@code Object.toString}.
     */
    @Override
    public String toString() {
        return dateLogged.toString() + "\n" + description;
    }
}