package seemlmot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends ToDo{
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by){
        super(description);
        this.by = by;
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String toString(){
        DateTimeFormatter out = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
        return ("[" + getType() + "][" + getStatusIcon() + "] " + getDescription() + " (by: " + by.format(out) + ")");
    }
}
