import java.util.Scanner;

public class Seemlmot {
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
        String[] cmdList = new String[100];
        String currentCmd = in.nextLine();

        int countCmd = 0;
        while(!currentCmd.equals("bye") && countCmd < 100) {
                System.out.println(horizontalLine);
                if( currentCmd.equals("list") ){
                    for(int i = 0; i < countCmd; i++)
                        System.out.println( i+1 + ". " + cmdList[i]);
                }
                else {
                    cmdList[countCmd++] = currentCmd;
                    System.out.println("added: " + currentCmd);
                }

                System.out.println(horizontalLine);
                currentCmd = in.nextLine();
        }
        System.out.println(Exit);
    }
}
