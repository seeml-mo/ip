package seemlmot;

/**
 * The main entry point for the Seemlmot chatbot application.
 * Coordinates the initialization of the UI, processing of user commands,
 * and the termination of the program.
 */
public class Seemlmot {
    /**
     * Starts the chatbot application.
     * Initializes the user interface, enters the command processing loop,
     * and performs cleanup before exiting.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        Ui.initialize();

        Parser.processCmd();

        Ui.bye();
    }
}
