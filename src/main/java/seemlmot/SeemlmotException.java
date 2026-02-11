package seemlmot;

public class SeemlmotException extends RuntimeException {
    private String message;

    public SeemlmotException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
