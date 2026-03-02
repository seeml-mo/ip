package seemlmot;

/**
 * Represents a generic task in the Seemlmot application.
 * This serves as a base class for specific task types like ToDo, Deadline, and Event.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new Task with the specified description.
     * By default, the task is initialized as not done.
     *
     * @param description The content of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone(){
        isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void markAsUndone(){
        isDone = false;
    }

    /**
     * Returns the description of the task.
     *
     * @return The task description string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns an icon representing the completion status.
     * Returns "X" if done, and a space " " if not done.
     *
     * @return A status icon string.
     */
    public String getStatusIcon() {
        return (isDone? "X" : " "); // mark done task with X
    }

    /**
     * Checks if the task is completed.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the type identifier for a generic task.
     * Defaults to "T0" for the base Task class.
     *
     * @return The type string.
     */
    public String getType() {
        return "T0";
    }
}
