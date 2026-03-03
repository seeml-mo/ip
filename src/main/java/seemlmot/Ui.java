package seemlmot;

/**
 * Handles the user interface of the Seemlmot chatbot.
 * Responsible for displaying greetings, exit messages, and structural dividers
 * to the console.
 */
public class Ui {
    public static final String HORIZONTAL_LINE = "____________________________________________________________";
    private static final String GREETING = """
                 What can I do for you?
                ____________________________________________________________
                """;
    private static final String LOGO =
            """
                     ____                       _                  _  \s
                    / ___|  ___  ___ _ __ ___  | | _ __ ___   ___ | |_\s
                    \\___ \\ / _ \\/ _ \\ '_ ` _ \\ | || '_ ` _ \\ / _ \\| __|
                     ___) |  __/  __/ | | | | || || | | | | | (_) | |_\s
                    |____/ \\___|\\___|_| |_| |_||_||_| |_| |_|\\___/ \\__|
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

        System.out.println("Hello! I'm Seemlmot.");
        System.out.println(LOGO + GREETING);
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
