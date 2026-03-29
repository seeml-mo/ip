package seemlmot;

import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles the interpretation of user input and commands.
 * This class provides functionality to process main command loops and
 * parse flexible date/time strings into LocalDateTime objects.
 */
public class Parser {
    /**
     * Starts the main command processing loop.
     * Continuously reads user input from the console, identifies the command type,
     * and executes corresponding actions in TaskList or Storage.
     * The loop terminates when the user enters "bye" or the task limit is reached.
     *
     * @throws SeemlmotException If an unrecognized command is entered or task limits are exceeded.
     */
    public static void processCmd() throws SeemlmotException {
        Scanner in = new Scanner(System.in);
        String currentDescription = in.nextLine();
        int cmdListSize = TaskList.getCmdList().size();

        while (!currentDescription.trim().equals("bye") && cmdListSize < TaskList.MAX_TASKS) {
            String[] currentCmd = currentDescription.split(" ");

            Ui.showLine();
            try {
                switch (currentCmd[0]) {
                case "list":
                    TaskList.list(currentDescription);
                    break;
                case "mark":
                    TaskList.mark(currentDescription, true);
                    break;
                case "unmark":
                    TaskList.mark(currentDescription, false);
                    break;
                case "delete":
                    TaskList.deleteTask(currentDescription);
                    break;
                case "todo":
                    TaskList.addTask(currentDescription, "T", true);
                    break;
                case "deadline":
                    TaskList.addTask(currentDescription, "D", true);
                    break;
                case "event":
                    TaskList.addTask(currentDescription, "E", true);
                    break;
                case "save":
                    Storage.saveState(currentDescription, TaskList.getCmdList());
                    break;
                case "find":
                    TaskList.findTask(currentDescription);
                    break;
                default:
                    throw new SeemlmotException(" I'm sorry, but I don't know what that means :-(");
                }
            } catch (SeemlmotException e) {
                System.out.println(e.getMessage());
            } finally {
                Ui.showLine();
                currentDescription = in.nextLine();
            }
        }
    }

    /**
     * Interprets a date/time string and converts it into a {@link LocalDateTime} object
     * based on predefined strict formats.
     * <p>
     * This method strictly enforces input format matching using a set of hard-coded
     * patterns (e.g., "yyyy-MM-dd HH:mm", "MMM dd yyyy HHmm"). All formatters are
     * locale-locked to {@link Locale#ENGLISH} to ensure consistent parsing performance
     * across different system environments.
     * </p>
     * <p>
     * If the input does not match any of the supported formats, the method will not
     * attempt heuristic guessing. Instead, it prints a usage hint and returns {@code null},
     * allowing the calling method to handle the invalid input state.
     * </p>
     *
     * @param input     The raw date/time string provided by the user.
     * @param startTime A reference {@link LocalDateTime} used for context (if applicable).
     * @return A {@link LocalDateTime} object if parsing succeeds; {@code null} otherwise.
     */
    public static LocalDateTime guessFlexible(String input, LocalDateTime startTime) {
        String cleanInput = input.trim().toLowerCase();

        // 1. Try strict parsing first
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH),

                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("MMM dd yyyy HHmm")
                        .toFormatter(Locale.ENGLISH),

                DateTimeFormatter.ofPattern("d/MM/yyyy HHmm", Locale.ENGLISH),

                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        };
        for (DateTimeFormatter f : formatters) {
            if (isStrictMatch(cleanInput, f)) {
                return LocalDateTime.parse(cleanInput.trim(), f);
            }
        }

        String formatHint = "MMM dd yyyy HHmm";
        System.out.println("Please use correct date-time format: " + formatHint + "\n"
                + "Failed to add the task.");
        return null;
    }

    /**
     * Checks if the input matches any of the strict date-time patterns supported by the system.
     * * @param input The raw input string to validate.
     * @return {@code true} if the input conforms to any predefined strict date-time formats;
     * {@code false} otherwise.
     */
    private static boolean isStrictMatch(String input, DateTimeFormatter f) {
        try {
            f.parse(input.trim());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
