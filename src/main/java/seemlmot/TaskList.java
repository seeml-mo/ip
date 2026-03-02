package seemlmot;

import java.time.*;
import java.util.ArrayList;

/**
 * Manages the list of tasks for the Seemlmot chatbot.
 * Provides functionality to add, delete, mark, and search for tasks.
 */
public class TaskList {
    public static final int MAX_TASKS = 100;
    private static final String PREFIX_LIST = "list";
    private static final String PREFIX_TODO = "todo ";
    private static final String PREFIX_DEADLINE = "deadline ";
    private static final String PREFIX_EVENT = "event ";
    private static final String PREFIX_FIND = "find ";
    public static final String PARAM_BY = "/by ";
    public static final String PARAM_FROM = "/from ";
    public static final String PARAM_TO = "/to ";

    /** The internal list used to store Task objects. */
    public static ArrayList<Task> cmdList = new ArrayList<>();

    /**
     * Adds a new task to the list based on user input and task type.
     * Supports ToDo (T), Deadline (D), and Event (E) types.
     *
     * @param currentDescription The full raw command string from the user.
     * @param type The type of task to add ("T", "D", or "E").
     * @param isTerminalCmd True if the operation should print feedback to the console.
     * @throws SeemlmotException If description is missing or date/time format is invalid.
     */
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

            String byString = currentDescription.substring(byPos + PARAM_BY.length()).trim();

            if (byString.isEmpty())
                throw new SeemlmotException("Do not leave deadline empty!!!");

            LocalDateTime by = Parser.guessFlexible(byString, null);

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

            String startString = currentDescription.substring(
                    fromPos + PARAM_FROM.length(), toPos
            ).trim();

            String endString = currentDescription.substring(
                    toPos + PARAM_TO.length()
            ).trim();

            if (startString.isEmpty())
                throw new SeemlmotException("Do not leave start time empty!!!");
            if(endString.isEmpty())
                throw new SeemlmotException("Do not leave end time empty!!!");

            LocalDateTime start = Parser.guessFlexible(startString, null);
            LocalDateTime end = Parser.guessFlexible(endString, start);

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

    /**
     * Displays all tasks currently in the list to the user.
     *
     * @param currentDescription The command string, expected to be just "list".
     * @throws SeemlmotException If extra arguments are provided after "list".
     */
    public static void list(String currentDescription) {
        if (currentDescription.trim().length() > PREFIX_LIST.length())
            throw new SeemlmotException(" No need to add additional description. Only \"list\" needed.");
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < (cmdList.size()); i++) {
            System.out.println(" " + (i + 1) + "." + cmdList.get(i));
        }
    }

    /**
     * Marks a specific task as done or undone.
     *
     * @param index The 0-based index of the task in the list.
     * @param markAsDone True to mark as done, false to mark as undone.
     * @throws SeemlmotException If the provided index is out of bounds.
     */
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

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index The 0-based index of the task to be deleted.
     * @throws SeemlmotException If the index is invalid.
     */
    public static void deleteTask(int index) {
        if (index >= cmdList.size())
            throw new SeemlmotException(" Task does not exist.");

        System.out.println(" Noted. I've removed this task:\n" +
                "  " + cmdList.get(index));

        cmdList.remove(index);

        System.out.println(" Now you have " + cmdList.size() + " tasks in the list.");
    }

    /**
     * Searches for tasks whose descriptions contain the specified keyword.
     * The search is case-insensitive.
     *
     * @param currentDescription The raw search command containing the keyword.
     * @throws SeemlmotException If the keyword is missing.
     */
    public static void findTask(String currentDescription) {
        if (currentDescription.trim().length() <= PREFIX_FIND.length()) {
            throw new SeemlmotException(" Description is empty. Please provide the content you are searching for.");
        }
        String keyword = currentDescription.substring(
                PREFIX_FIND.length()
        ).trim();

        int count = 1;
        for (Task task : cmdList) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                if (count == 1)
                    System.out.println(" Here are the matching tasks in your list:");
                System.out.println(count + ". " + task);
                count++;
            }
        }

        if (count == 1) {
            System.out.println(" No matching tasks found for: " + keyword);
        }
    }

    /**
     * Returns the current internal task list.
     *
     * @return An ArrayList of Task objects.
     */
    public static ArrayList<Task> getCmdList(){
        return cmdList;
    }

    /**
     * Initializes the task list with existing data, usually from a file.
     *
     * @param dataList The ArrayList of tasks to load.
     */
    public static void initialize(ArrayList<Task> dataList){
        cmdList = dataList;
    }
}
