package seemlmot;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class Event extends Deadline{
    private final LocalDateTime start;
    public Event(String description, LocalDateTime start, LocalDateTime by){
        super(description, by);
        this.start = start;
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String toString(){
        DateTimeFormatter out = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
        return ("[" + getType() + "][" + getStatusIcon() + "] " + getDescription() + " (from: " + start.format(out) + " to: " + by.format(out) + ")");
    }
}
