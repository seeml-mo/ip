public class ToDo extends Task{
    public  ToDo(String description){
        super(description);
    }

    @Override
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    @Override
    public String getType() {
        return "T";
    }

    @Override
    public String toString(){
        return ("[" + getType() + "][" + getStatusIcon() + "] " + getDescription());
    }
}
