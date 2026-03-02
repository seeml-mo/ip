package seemlmot;

/**
 * Represents a simple task without any date or time constraints.
 * A <code>ToDo</code> object only contains a description and a completion status.
 */
public class ToDo extends Task{
    /**
     * Creates a new ToDo task with the specified description.
     *
     * @param description The textual description of the task.
     */
    public  ToDo(String description){
        super(description);
    }

    /**
     * Returns the type identifier for this task.
     *
     * @return The string "T" representing a ToDo task.
     */
    @Override
    public String getType() {
        return "T";
    }

    /**
     * Returns a formatted string representation of the ToDo task.
     * Includes the task type, status icon, and description.
     *
     * @return A string describing the ToDo task.
     */
    @Override
    public String toString(){
        return ("[" + getType() + "][" + getStatusIcon() + "] " + getDescription());
    }
}
