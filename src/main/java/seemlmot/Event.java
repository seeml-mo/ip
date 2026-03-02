package seemlmot;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task that occurs within a specific time range.
 * An <code>Event</code> object includes a description, a start time,
 * and an end (by) time.
 */
public class Event extends Deadline{
    private final LocalDateTime start;

    /**
     * Creates a new Event task with the specified description, start time, and end time.
     *
     * @param description The textual description of the event.
     * @param start The LocalDateTime representing when the event begins.
     * @param by The LocalDateTime representing when the event ends.
     */
    public Event(String description, LocalDateTime start, LocalDateTime by){
        super(description, by);
        this.start = start;
    }

    /**
     * Returns the type identifier for this task.
     *
     * @return The string "E" representing an Event task.
     */
    @Override
    public String getType() {
        return "E";
    }

    /**
     * Returns a formatted string representation of the event task.
     * Includes the task type, status icon, description, start time, and end time.
     * Dates are formatted as "MMM dd yyyy HHmm".
     *
     * @return A string describing the event duration and details.
     */
    @Override
    public String toString(){
        DateTimeFormatter out = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
        return ("[" + getType() + "][" + getStatusIcon() + "] " + getDescription() + " (from: " + start.format(out) + " to: " + by.format(out) + ")");
    }
}
