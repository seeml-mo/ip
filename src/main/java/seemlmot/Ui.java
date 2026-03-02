package seemlmot;

/**
 * Handles the user interface of the Seemlmot chatbot.
 * Responsible for displaying greetings, exit messages, and structural dividers
 * to the console.
 */
public class Ui {
    public static final String HORIZONTAL_LINE = "____________________________________________________________";
    private static final String GREETING = """
                 Hello! I'm Seemlmot
                 What can I do for you?
                ____________________________________________________________
                """;
    private static final String EXIT = """
            ____________________________________________________________
            Bye. Hope to see you again soon!
            ____________________________________________________________
            """;

    /**
     * Prepares the application for use.
     * Prints a divider, loads existing task data from storage,
     * and displays the greeting message.
     */
    public static void initialize() {
        showLine();

        TaskList.initialize( Storage.loadData() );

        System.out.println(GREETING);
    }

    /**
     * Prints a horizontal divider line to the console for better readability.
     */
    public static void showLine(){
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays the exit message to the user before the application closes.
     */
    public static void bye(){
        System.out.println(EXIT);
    }
}
