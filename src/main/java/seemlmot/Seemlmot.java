package seemlmot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Seemlmot {
    public static final int MAX_TASKS = 100;
    public static ArrayList<Task> cmdList = new ArrayList<>();
    public static final String HORIZONTAL_LINE = "____________________________________________________________";

    public static final String FILE_PATH = "./data/seemlmot.txt";

    public static final int REQUIREMENT_TODO = 3;
    public static final int REQUIREMENT_DEADLINE = 4;
    public static final int REQUIREMENT_EVENT = 5;

    private static final String PREFIX_SAVE = "save";
    private static final String PREFIX_LIST = "list";
    private static final String PREFIX_TODO = "todo ";
    private static final String PREFIX_DEADLINE = "deadline ";
    private static final String PREFIX_EVENT = "event ";
    private static final String PARAM_BY = "/by ";
    private static final String PARAM_FROM = "/from ";
    private static final String PARAM_TO = "/to ";

    private static final String EXIT = """
            ____________________________________________________________
            Bye. Hope to see you again soon!
            ____________________________________________________________
            """;


    public static void main(String[] args) {
        initialize();

        processCmd();

        System.out.println(EXIT);
    }

    public static void initialize() {
        System.out.println(HORIZONTAL_LINE);

        loadData();

        final String GREETING = """
                 Hello! I'm Seemlmot
                 What can I do for you?
                ____________________________________________________________
                """;
        System.out.println(GREETING);
    }

    public static void loadData() {
        Path path = Paths.get(FILE_PATH);

        if (Files.exists(path)) {
            try {
                List<String> lines = Files.readAllLines(path);

                for (String line : lines) {
                    createTaskFromType(line);
                }
                System.out.println(" (Loaded " + cmdList.size() + " tasks from file.)");

            } catch (SeemlmotException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(" (Error reading file: " + e.getMessage() + ")");
            }
        } else {
            System.out.println(" (No existing data file. Starting with empty list.)");
        }
    }

    private static void createTaskFromType(String line) throws IOException {
        String[] parts = line.split(" \\| ");
        String content;
        switch (parts[0]) {
        case "T":
            if (parts.length > REQUIREMENT_TODO)
                throw new SeemlmotException(" Error File: Unexpected additional info.");

            content = PREFIX_TODO + parts[2].trim();
            addTask(content, "T", false);

            if (Integer.parseInt(parts[1]) == 1)
                cmdList.get((cmdList.size() - 1)).markAsDone();

            break;
        case "D":
            if (parts.length > REQUIREMENT_DEADLINE)
                throw new SeemlmotException(" Error File: Unexpected additional info.");

            content = PREFIX_DEADLINE + parts[2].trim() + " " + PARAM_BY + parts[3].trim();
            addTask(content, "D", false);

            if (Integer.parseInt(parts[1]) == 1)
                cmdList.get((cmdList.size() - 1)).markAsDone();

            break;
        case "E":
            if (parts.length > REQUIREMENT_EVENT)
                throw new SeemlmotException(" Error File: Unexpected additional info.");

            content = PREFIX_EVENT + parts[2].trim() + " " + PARAM_FROM + parts[3].trim() + " " + PARAM_BY + parts[4].trim();
            addTask(content, "E", false);

            if (Integer.parseInt(parts[1]) == 1)
                cmdList.get((cmdList.size() - 1)).markAsDone();

            break;
        default:
            throw new IOException(" Command type does not exist.");
        }
    }

    public static void processCmd() throws SeemlmotException {
        Scanner in = new Scanner(System.in);
        String currentDescription = in.nextLine();

        while (!currentDescription.trim().equals("bye") && cmdList.size() < MAX_TASKS) {
            String[] currentCmd = currentDescription.split(" ");

            System.out.println(HORIZONTAL_LINE);
            try {
                switch (currentCmd[0]) {
                case "list":
                    list(currentDescription);
                    break;
                case "mark":
                    mark(Integer.parseInt(currentCmd[1]) - 1, true);
                    break;
                case "unmark":
                    mark(Integer.parseInt(currentCmd[1]) - 1, false);
                    break;
                case "delete":
                    deleteTask(Integer.parseInt(currentCmd[1]) - 1);
                    break;
                case "todo":
                    addTask(currentDescription, "T", true);
                    break;
                case "deadline":
                    addTask(currentDescription, "D", true);
                    break;
                case "event":
                    addTask(currentDescription, "E", true);
                    break;
                case "save":
                    saveState(currentDescription);
                    break;
                default:
                    throw new SeemlmotException(" I'm sorry, but I don't know what that means :-(");
                }
            } catch (SeemlmotException e) {
                System.out.println(e.getMessage());
            } finally {
                System.out.println(HORIZONTAL_LINE);
                currentDescription = in.nextLine();
            }
        }
    }

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

    public static void saveState(String currentDescription) {
        if (currentDescription.trim().length() > PREFIX_SAVE.length())
            throw new SeemlmotException(" No need to add additional description. Only \"save\" needed.");

        try {
            StringBuilder content = new StringBuilder();
            for (Task task : cmdList) {
                content.append(task.getType()).append(" | ")
                        .append(task.isDone() ? 1 : 0).append(" | ")
                        .append(task.getDescription()).append("\n");
            }

            Path path = Paths.get(FILE_PATH);
            Path parentDir = path.getParent();

            if (parentDir != null && Files.notExists(parentDir)) {
                System.out.println(" No existing record. New file is created in ./data.");
                Files.createDirectories(parentDir);
            }

            Files.write(path, content.toString().getBytes());
            System.out.println(" File saved successfully.");

        } catch (IOException e) {
            System.out.println(" Error saving file: " + e.getMessage());
        }
    }
}
