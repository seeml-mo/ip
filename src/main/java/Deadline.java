public class Deadline extends ToDo{
    protected String by;

    public Deadline(String description, String by){
        super(description);
        this.by = by;
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String listFormat(){
        return ("[" + getType() + "][" + getStatusIcon() + "] " + description + " (by: " + by + ")");
    }
}
