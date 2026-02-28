package seemlmot;

public class Seemlmot {
    public static void main(String[] args) {
        Ui.initialize();

        Parser.processCmd();

        Ui.bye();
    }
}
