import java.util.Scanner;

public class Seemlmot {
    public static int countCmd = 0;
    public static Task[] cmdList = new Task[100];

    public static void add(String currentDescription){
        cmdList[countCmd++] = new Task(currentDescription);
        System.out.println("added: " + currentDescription);
    }
    public static void list(){
        for(int i = 0; i < countCmd; i++){
            System.out.println("    " + i+1 + ".[" + cmdList[i].getStatusIcon() + "]" + cmdList[i].getDescription() );
        }
    }

    public static void mark(int index, boolean markAsDone){
        if(markAsDone) {
            cmdList[index].markAsDone();
            System.out.println("Nice! I've marked this task as done:\n"
                                + "    [" + cmdList[index].getStatusIcon() +"] " + cmdList[index].getDescription());
        }
        else{
            cmdList[index].markAsUndone();
            System.out.println("OK, I've marked this task as not done yet:\n"
                    + "    [" + cmdList[index].getStatusIcon() +"] " + cmdList[index].getDescription());
        }
    }
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

        String horizontalLine = "____________________________________________________________";

        Scanner in = new Scanner(System.in);
        String currentDescription = in.nextLine();

        while(!currentDescription.equals("bye") && countCmd < 100) {
            String[] currentCmd = currentDescription.split(" ");

            System.out.println(horizontalLine);
            switch(currentCmd[0]) {
                case "list" -> list();
                case "mark" -> mark(Integer.parseInt(currentCmd[1])-1, true);
                case "unmark" -> mark(Integer.parseInt(currentCmd[1])-1, false);
                default -> add(currentDescription);
            }

            System.out.println(horizontalLine);
            currentDescription = in.nextLine();
        }
        System.out.println(Exit);
    }
}
