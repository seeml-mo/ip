public class Event extends Deadline{
    private String start;
    public Event(String description, String start, String by){
        super(description, by);
        this.start = start;
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String listFormat(){
        return ("[" + getType() + "][" + getStatusIcon() + "] " + description + " (from: " + start + " to: " + by + ")");
    }
}
