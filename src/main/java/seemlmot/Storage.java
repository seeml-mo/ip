package seemlmot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading and saving tasks from/to a hard disk file.
 * This class ensures that the chatbot's state is preserved between sessions.
 */
public class Storage {
    public static final String FILE_PATH = "./data/seemlmot.txt";
    private static final String PREFIX_SAVE = "save";

    public static final int REQUIREMENT_TODO = 3;
    public static final int REQUIREMENT_DEADLINE = 4;
    public static final int REQUIREMENT_EVENT = 5;

    private static final ArrayList<Task> dataList = new ArrayList<>();

    /**
     * Loads tasks from the storage file defined in {@code FILE_PATH}.
     * If the file or directory does not exist, it starts with an empty list.
     *
     * @return An ArrayList of tasks retrieved from the file.
     */
    public static ArrayList<Task> loadData() {

        Path path = Paths.get(FILE_PATH);

        if (Files.exists(path)) {
            try {
                List<String> lines = Files.readAllLines(path);

                for (String line : lines) {
                    createTaskFromType(line);
                }
                System.out.println(" (Loaded " + dataList.size() + " tasks from file.)");

            } catch (SeemlmotException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(" (Error reading file: " + e.getMessage() + ")");
            }
        } else {
            System.out.println(" (No existing data file. Starting with empty list.)");
        }

        return dataList;
    }

    /**
     * Parses a single line from the storage file and adds the corresponding task to {@code dataList}.
     * This is a non-trivial private method that handles data validation and type-specific parsing.
     *
     * @param line A single line of text from the saved file.
     * @throws IOException If the task type is unrecognized or file format is corrupted.
     * @throws SeemlmotException If required parameters are missing in the data line.
     */
    private static void createTaskFromType(String line) throws IOException {
        String[] parts = line.split(" \\| ");
        String description;
        switch (parts[0]) {
        case "T":
            if (parts.length > REQUIREMENT_TODO)
                throw new SeemlmotException(" Error File: Unexpected additional info.");
            else if (parts.length < REQUIREMENT_TODO)
                throw new SeemlmotException(" Error File: Important parameters missing.");

            description = parts[2].trim();

            if (description.isEmpty())
                throw new SeemlmotException("Task " + ( dataList.size()+ 2) + "(ToDo): description lost.");

            dataList.add(new ToDo(description));

            if (Integer.parseInt(parts[1]) == 1)
                dataList.get((dataList.size() - 1)).markAsDone();

            break;
        case "D":
            if (parts.length > REQUIREMENT_DEADLINE)
                throw new SeemlmotException(" Error File: Unexpected additional info.");
            else if (parts.length < REQUIREMENT_DEADLINE)
                throw new SeemlmotException(" Error File: Important parameters missing.");

            description = parts[2].trim();

            String byString = parts[3].trim();

            LocalDateTime by = Parser.guessFlexible(byString, null);

            dataList.add(new Deadline(description, by));

            if (Integer.parseInt(parts[1]) == 1)
                dataList.get((dataList.size() - 1)).markAsDone();

            break;
        case "E":
            if (parts.length > REQUIREMENT_EVENT)
                throw new SeemlmotException(" Error File: Unexpected additional info.");
            else if (parts.length < REQUIREMENT_EVENT)
                throw new SeemlmotException(" Error File: Important parameters missing.");

            description = parts[2].trim();

            String startString, endString;
            startString = parts[3].trim();
            endString = parts[3].trim();

            LocalDateTime start = Parser.guessFlexible(startString, null);
            LocalDateTime end = Parser.guessFlexible(endString, start);
            dataList.add(new Event(description, start, end));

            if (Integer.parseInt(parts[1]) == 1)
                dataList.get((dataList.size() - 1)).markAsDone();

            break;
        default:
            throw new IOException(" Command type does not exist.");
        }
    }

    /**
     * Saves the current list of tasks to the hard disk.
     * Creates the directory structure if it does not already exist.
     *
     * @param currentDescription The command string, expected to be just "save".
     * @param cmdList The current list of tasks to be written to the file.
     * @throws SeemlmotException If additional arguments are provided after "save".
     */
    public static void saveState(String currentDescription, ArrayList<Task> cmdList) {
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
