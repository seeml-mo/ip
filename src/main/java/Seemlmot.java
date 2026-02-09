import java.util.Scanner;

public class Seemlmot {
    public static int countCmd = 0;
    public static final int MAX_TASKS = 100;
    public static Task[] cmdList = new Task[MAX_TASKS];
    public static final String HORIZONTAL_LINE = "____________________________________________________________";

    private static final String PREFIX_TODO = "todo ";
    private static final String PREFIX_DEADLINE = "deadline ";
    private static final String PREFIX_EVENT = "event ";
    private static final String PARAM_BY = "/by ";
    private static final String PARAM_FROM = "/from ";
    private static final String PARAM_TO = "/to ";

    public static void main(String[] args) {
        /*String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);*/

        String Greeting = """
                ____________________________________________________________
                Hello! I'm Seemlmot
                What can I do for you?
                ____________________________________________________________
                """;
        System.out.println(Greeting);

        String Exit = """
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """;

        cmdProcessing();

        System.out.println(Exit);
    }

    public static void cmdProcessing(){
        Scanner in = new Scanner(System.in);
        String currentDescription = in.nextLine();

        while(!currentDescription.trim().equals("bye") && countCmd < 100) {
            String[] currentCmd = currentDescription.split(" ");

            System.out.println(HORIZONTAL_LINE);
            switch(currentCmd[0]) {
                case "list" -> list();
                case "mark" -> mark(Integer.parseInt(currentCmd[1])-1, true);
                case "unmark" -> mark(Integer.parseInt(currentCmd[1])-1, false);
                case "todo" -> addTask(currentDescription, "T");
                case "deadline" -> addTask(currentDescription, "D");
                case  "event" -> addTask(currentDescription, "E");
                default -> addTask(currentDescription, "T0");
            }

            System.out.println(HORIZONTAL_LINE);
            currentDescription = in.nextLine();
        }
    }

    public static void addTask(String currentDescription, String type){
        String description;

        switch (type){
            case "T": {
                description = currentDescription.substring(PREFIX_TODO.length()).trim();
                cmdList[countCmd] = new ToDo(description);
                break;
            }

            case "D": {
                int byPos = currentDescription.indexOf(PARAM_BY);

                description = currentDescription.substring(
                        PREFIX_DEADLINE.length(), byPos
                ).trim();

                String by = currentDescription.substring(byPos + PARAM_BY.length()).trim();

                cmdList[countCmd] = new Deadline(description, by);
                break;
            }

            case "E": {
                int fromPos = currentDescription.indexOf(PARAM_FROM);
                int toPos = currentDescription.indexOf(PARAM_TO);

                description = currentDescription.substring(
                        PREFIX_EVENT.length(), fromPos
                ).trim();

                String start = currentDescription.substring(
                        fromPos + PARAM_FROM.length(), toPos
                ).trim();

                String end = currentDescription.substring(
                        toPos + PARAM_TO.length()
                ).trim();

                cmdList[countCmd] = new Event(description, start, end);
                break;
            }

            default: {
                 cmdList[countCmd] = new Task(currentDescription);
            }
        }

        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + cmdList[countCmd]);
        String taskExpression = countCmd==0? "task":"tasks";
        System.out.println(" Now you have " + (++countCmd) + " " + taskExpression + " in the list.");
    }
    public static void list(){
        System.out.println(" Here are the tasks in your list:");
        for(int i = 0; i < countCmd; i++){
            System.out.println(" " + (i+1) + "." + cmdList[i] );
        }
    }

    public static void mark(int index, boolean markAsDone){
        if(markAsDone) {
            cmdList[index].markAsDone();
            System.out.println(" Nice! I've marked this task as done:\n"
                    + "   " + cmdList[index]);
        }
        else{
            cmdList[index].markAsUndone();
            System.out.println(" OK, I've marked this task as not done yet:\n"
                    + "   " + cmdList[index]);
        }
    }
}
