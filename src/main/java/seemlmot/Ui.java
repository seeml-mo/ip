package seemlmot;

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

    public static void initialize() {
        showLine();

        TaskList.initialize( Storage.loadData() );

        System.out.println(GREETING);
    }

    public static void showLine(){
        System.out.println(HORIZONTAL_LINE);
    }

    public static void bye(){
        System.out.println(EXIT);
    }
}
