package seemlmot;

import java.util.ArrayList;

public class TaskList {
    public static final int MAX_TASKS = 100;
    public static final String PREFIX_LIST = "list";
    public static final String PREFIX_TODO = "todo ";
    public static final String PREFIX_DEADLINE = "deadline ";
    public static final String PREFIX_EVENT = "event ";
    public static final String PARAM_BY = "/by ";
    public static final String PARAM_FROM = "/from ";
    public static final String PARAM_TO = "/to ";

    public static ArrayList<Task> cmdList = new ArrayList<>();

    public static void addTask(String currentDescription, String type, boolean isTerminalCmd) {
        String description;

        switch (type) {
        case "T": {
            if (currentDescription.trim().length() <= PREFIX_TODO.length()) {
                throw new SeemlmotException(" OOPS!!! The description of a todo cannot be empty.");
            }

            description = currentDescription.substring(
                    PREFIX_TODO.length()
            ).trim();

            cmdList.add(new ToDo(description));
            break;
        }

        case "D": {
            int byPos = currentDescription.indexOf(PARAM_BY);

            if (byPos == -1)
                throw new SeemlmotException(" Deadline not found. Please add '/by'.");

            description = currentDescription.substring(
                    PREFIX_DEADLINE.length(), byPos
            ).trim();

            if (description.isEmpty()) {
                throw new SeemlmotException(" OOPS!!! The description of a deadline cannot be empty.");
            }

            String by = currentDescription.substring(byPos + PARAM_BY.length()).trim();

            if (by.isEmpty())
                throw new SeemlmotException("Do not leave deadline empty!!!");

            cmdList.add(new Deadline(description, by));
            break;
        }

        case "E": {
            int fromPos = currentDescription.indexOf(PARAM_FROM);
            int toPos = currentDescription.indexOf(PARAM_TO);

            if (fromPos == -1)
                throw new SeemlmotException(" Start time not found. Please add '/to'.");

            if (toPos == -1)
                throw new SeemlmotException(" End time not found. Please add '/from'.");

            description = currentDescription.substring(
                    PREFIX_EVENT.length(), fromPos
            ).trim();

            if (description.isEmpty()) {
                throw new SeemlmotException(" OOPS!!! The description of an event cannot be empty.");
            }

            String start = currentDescription.substring(
                    fromPos + PARAM_FROM.length(), toPos
            ).trim();

            String end = currentDescription.substring(
                    toPos + PARAM_TO.length()
            ).trim();

            if (start.isEmpty())
                throw new SeemlmotException("Do not leave start time empty!!!");
            if(end.isEmpty())
                throw new SeemlmotException("Do not leave end time empty!!!");

            cmdList.add(new Event(description, start, end));
            break;
        }

        default: {
            cmdList.add(new Task(currentDescription));
        }
        }

        if (isTerminalCmd) {
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + cmdList.get(cmdList.size() - 1));
            String taskExpression = (cmdList.size() == 1) ? "task" : "tasks";
            System.out.println(" Now you have " + cmdList.size() + " " + taskExpression + " in the list.");
        }
    }

    public static void list(String currentDescription) {
        if (currentDescription.trim().length() > PREFIX_LIST.length())
            throw new SeemlmotException(" No need to add additional description. Only \"list\" needed.");
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < (cmdList.size()); i++) {
            System.out.println(" " + (i + 1) + "." + cmdList.get(i));
        }
    }

    public static void mark(int index, boolean markAsDone) {
        if (index >= cmdList.size())
            throw new SeemlmotException(" Task does not exist.");

        if (markAsDone) {
            cmdList.get(index).markAsDone();
            System.out.println(" Nice! I've marked this task as done:\n"
                    + "   " + cmdList.get(index));
        } else {
            cmdList.get(index).markAsUndone();
            System.out.println(" OK, I've marked this task as not done yet:\n"
                    + "   " + cmdList.get(index));
        }
    }

    public static void deleteTask(int index) {
        if (index >= cmdList.size())
            throw new SeemlmotException(" Task does not exist.");

        System.out.println(" Noted. I've removed this task:\n" +
                "  " + cmdList.get(index));

        cmdList.remove(index);

        System.out.println(" Now you have " + cmdList.size() + " tasks in the list.");
    }

    public static ArrayList<Task> getCmdList(){
        return cmdList;
    }

    public static void initialize(ArrayList<Task> dataList){
        cmdList = dataList;
    }
}
