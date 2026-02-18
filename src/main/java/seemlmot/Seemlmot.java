package seemlmot;

import java.util.Scanner;
import java.util.ArrayList;

public class Seemlmot {
    public static final int MAX_TASKS = 100;
    public static ArrayList<Task> cmdList = new ArrayList<>();
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

        while(!currentDescription.trim().equals("bye") && cmdList.size() < MAX_TASKS) {
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
                    case "delete":
                        deleteTask(Integer.parseInt(currentCmd[1]) - 1);
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
                if (currentDescription.trim().length() <= PREFIX_TODO.length()) {
                    throw new SeemlmotException("OOPS!!! The description of a todo cannot be empty.");
                }

                description = currentDescription.substring(
                        PREFIX_TODO.length()
                ).trim();

                cmdList.add(new ToDo(description));
                break;
            }

            case "D": {
                int byPos = currentDescription.indexOf(PARAM_BY);

                if(byPos == -1)
                    throw new SeemlmotException("Deadline not found. Please add '/by'.");

                description = currentDescription.substring(
                        PREFIX_DEADLINE.length(), byPos
                ).trim();

                if (description.isEmpty()) {
                    throw new SeemlmotException("OOPS!!! The description of a deadline cannot be empty.");
                }

                String by = currentDescription.substring(byPos + PARAM_BY.length()).trim();

                cmdList.add(new Deadline(description, by));
                break;
            }

            case "E": {
                int fromPos = currentDescription.indexOf(PARAM_FROM);
                int toPos = currentDescription.indexOf(PARAM_TO);

                if(fromPos == -1)
                    throw new SeemlmotException("Start time not found. Please add '/to'.");

                if(toPos == -1)
                    throw new SeemlmotException("End time not found. Please add '/from'.");

                description = currentDescription.substring(
                        PREFIX_EVENT.length(), fromPos
                ).trim();

                if (description.isEmpty()) {
                    throw new SeemlmotException("OOPS!!! The description of an event cannot be empty.");
                }

                String start = currentDescription.substring(
                        fromPos + PARAM_FROM.length(), toPos
                ).trim();

                String end = currentDescription.substring(
                        toPos + PARAM_TO.length()
                ).trim();

                cmdList.add(new Event(description, start, end));
                break;
            }

            default: {
                 cmdList.add(new Task(currentDescription));
            }
        }

        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + cmdList.get(cmdList.size() - 1));
        String taskExpression = (cmdList.size() == 1)? "task":"tasks";
        System.out.println(" Now you have " + cmdList.size() + " " + taskExpression + " in the list.");
    }
    public static void list(){
        System.out.println(" Here are the tasks in your list:");
        for(int i = 0; i < ( cmdList.size() ); i++){
            System.out.println(" " + (i+1) + "." + cmdList.get(i) );
        }
    }

    public static void mark(int index, boolean markAsDone) {
        if(index >= cmdList.size())
            throw new SeemlmotException("Task does not exist.");

        if (markAsDone) {
            cmdList.get(index).markAsDone();
            System.out.println(" Nice! I've marked this task as done:\n"
                    + "   " + cmdList.get(index));
        } else {
            cmdList.get(index).markAsUndone();
            System.out.println(" OK, I've marked this task as not done yet:\n"
                    + "   " + cmdList.get(index));
        }
    }

    public static void deleteTask(int index){
        if(index >= cmdList.size())
            throw new SeemlmotException("Task does not exist.");

        System.out.println("Noted. I've removed this task:\n" +
                "  " + cmdList.get(index) );

        cmdList.remove(index);

        System.out.println("Now you have " + cmdList.size() + " tasks in the list.");
    }
}
