package seemlmot;

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

    private static final String GREETING = """
                ____________________________________________________________
                Hello! I'm Seemlmot
                What can I do for you?
                ____________________________________________________________
                """;

    private static final String EXIT = """
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """;


    public static void main(String[] args) {
        System.out.println(GREETING);

        cmdProcessing();

        System.out.println(EXIT);
    }

    public static void cmdProcessing() throws  SeemlmotException{
        Scanner in = new Scanner(System.in);
        String currentDescription = in.nextLine();

        while(!currentDescription.trim().equals("bye") && countCmd < 100) {
            String[] currentCmd = currentDescription.split(" ");

            System.out.println(HORIZONTAL_LINE);
            try{
                switch(currentCmd[0]) {
                    case "list":
                        list();
                        break;
                    case "mark":
                        mark(Integer.parseInt(currentCmd[1]) - 1, true);
                        break;
                    case "unmark":
                        mark(Integer.parseInt(currentCmd[1]) - 1, false);
                        break;
                    case "todo":
                        addTask(currentDescription, "T");
                        break;
                    case "deadline":
                        addTask(currentDescription, "D");
                        break;
                    case "event":
                        addTask(currentDescription, "E");
                        break;
                    default:
                        throw new SeemlmotException("I'm sorry, but I don't know what that means :-(");
                }
            }catch (SeemlmotException e){
                System.out.println(e.getMessage());
            }catch(IndexOutOfBoundsException e){
                System.out.println("Task number required.");
            }finally {
                System.out.println(HORIZONTAL_LINE);
                currentDescription = in.nextLine();
            }
        }
    }

    public static void addTask(String currentDescription, String type){
        String description;

        switch (type){
            case "T": {
                description = currentDescription.substring(
                        PREFIX_TODO.length()
                ).trim();

                cmdList[countCmd] = new ToDo(description);
                break;
            }

            case "D": {
                int byPos = currentDescription.indexOf(PARAM_BY);

                if(byPos == -1)
                    throw new SeemlmotException("Deadline not found. Please add '/by'.");

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

                if(fromPos == -1)
                    throw new SeemlmotException("End time not found. Please add '/to'.");

                if(toPos == -1)
                    throw new SeemlmotException("Start time not found. Please add '/from'.");

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

    public static void mark(int index, boolean markAsDone) {
        if(index >= countCmd)
            throw new SeemlmotException("Task does not exist.");

        if (markAsDone) {
            cmdList[index].markAsDone();
            System.out.println(" Nice! I've marked this task as done:\n"
                    + "   " + cmdList[index]);
        } else {
            cmdList[index].markAsUndone();
            System.out.println(" OK, I've marked this task as not done yet:\n"
                    + "   " + cmdList[index]);
        }
    }
}
