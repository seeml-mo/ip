public class Event extends Deadline{
    private final String start;
    public Event(String description, String start, String by){
        super(description, by);
        this.start = start;
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String toString(){
        return ("[" + getType() + "][" + getStatusIcon() + "] " + getDescription() + " (from: " + start + " to: " + by + ")");
    }
}
