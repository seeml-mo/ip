package seemlmot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a specific deadline.
 * A <code>Deadline</code> object contains a description and a date/time by which
 * the task must be completed.
 */
public class Deadline extends ToDo{
    protected LocalDateTime by;

    /**
     * Creates a new Deadline task with the given description and due date.
     *
     * @param description The textual description of the task.
     * @param by The LocalDateTime representing the deadline.
     */
    public Deadline(String description, LocalDateTime by){
        super(description);
        this.by = by;
    }

    /**
     * Returns the type identifier for this task.
     *
     * @return The string "D" representing a Deadline task.
     */
    @Override
    public String getType() {
        return "D";
    }

    /**
     * Returns a formatted string representation of the deadline task.
     * Includes the task type, status icon, description, and the formatted
     * deadline date (e.g., MMM dd yyyy HHmm).
     *
     * @return A string describing the deadline task.
     */
    @Override
    public String toString(){
        DateTimeFormatter out = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
        return ("[" + getType() + "][" + getStatusIcon() + "] " + getDescription() + " (by: " + by.format(out) + ")");
    }
}
