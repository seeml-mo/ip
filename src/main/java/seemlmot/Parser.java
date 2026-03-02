package seemlmot;

import java.util.Scanner;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

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
                    TaskList.mark(Integer.parseInt(currentCmd[1]) - 1, true);
                    break;
                case "unmark":
                    TaskList.mark(Integer.parseInt(currentCmd[1]) - 1, false);
                    break;
                case "delete":
                    TaskList.deleteTask(Integer.parseInt(currentCmd[1]) - 1);
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
     * Attempts to parse a flexible date/time string into a LocalDateTime object.
     * <p>
     * The method tries to identify dates through keywords (e.g., "today", "tomorrow", "monday"),
     * and interprets time strings (e.g., "2pm", "1400"). If a specific date is not found
     * but a startTime is provided, it inherits the date from the startTime.
     * </p>
     *
     * @param input The raw date/time string provided by the user.
     * @param startTime A reference time used for date inheritance or overnight checks.
     * @return A LocalDateTime object representing the interpreted date and time.
     * @throws DateTimeException If the date or time cannot be recognized from the input.
     */
    public static LocalDateTime guessFlexible(String input, LocalDateTime startTime) throws DateTimeException {
        String cleanInput = input.trim().toLowerCase();

        // 1. Try strict parsing first
        try {
            // Note: MMM dd yyyy is more standard for input than MMM dd yyyy
            return LocalDateTime.parse(cleanInput, DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"));
        } catch (Exception e) {
            // Continue to guessing logic
        }

        // 2. Identify Date
        LocalDate date = null;
        if (cleanInput.contains("today")) {
            date = LocalDate.now();
        } else if (cleanInput.contains("tomorrow")) {
            date = LocalDate.now().plusDays(1);
        } else {
            for (DayOfWeek day : DayOfWeek.values()) {
                String name = day.name().toLowerCase();
                if (cleanInput.contains(name) || cleanInput.contains(name.substring(0, 3))) {
                    date = LocalDate.now().with(TemporalAdjusters.nextOrSame(day));
                    break;
                }
            }
        }

        // THE CORE LOGIC: If no date keyword in THIS input, use startTime's date
        if (date == null && startTime != null) {
            date = startTime.toLocalDate();
        }

        // If still null, it means no date was provided and no startTime exists to inherit from
        if (date == null) {
            throw new DateTimeException("Failed to recognize date. Please provide a date or weekday.");
        }

        // 3. Identify Time (Mandatory)
        LocalTime time;
        String digits = cleanInput.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            throw new DateTimeException("Missing specific time: Please add a time (e.g., 2pm or 1400).");
        }

        try {
            int val = Integer.parseInt(digits);
            int hour = (val >= 100) ? val / 100 : val;

            if (cleanInput.contains("pm") && hour < 12) hour += 12;
            if (cleanInput.contains("am") && hour == 12) hour = 0;

            time = LocalTime.of(hour, 0);
        } catch (Exception e) {
            throw new DateTimeException("Sorry, I couldn't understand the time format in: " + input);
        }

        // 4. Combine and check for "Overnight" (e.g., 11pm to 1am)
        LocalDateTime result = LocalDateTime.of(date, time);
        if (startTime != null && result.isBefore(startTime)) {
            result = result.plusDays(1);
        }

        DateTimeFormatter out = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
        System.out.println("Date & Time processed as follows: " + result.format(out) + " Please write in the format: yyyy-MM-dd HHmm, if guess is not satisfying.");

        return result;
    }

}
