package seemlmot;

/**
 * Represents exceptions specific to the Seemlmot chatbot.
 * Used to handle logical errors such as empty descriptions or invalid command formats.
 */
public class SeemlmotException extends RuntimeException {
    private final String message;

    /**
     * Creates an exception with a specific message.
     *
     * @param message The details of the error.
     */
    public SeemlmotException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Retrieves the error message stored in this exception.
     *
     * @return The error message string.
     */
    @Override
    public String getMessage() {
        return message;
    }
}
