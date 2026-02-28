package seemlmot;

import java.util.Scanner;

public class Parser {
    public static void processCmd() throws SeemlmotException {
        Scanner in = new Scanner(System.in);
        String currentDescription = in.nextLine();
        int cmdListSize = TaskList.getCmdList().size();

        while (!currentDescription.trim().equals("bye") && cmdListSize < TaskList.MAX_TASKS) {
            String[] currentCmd = currentDescription.split(" ");

            Ui.showLine();
            try {
                switch (currentCmd[0]) {
                case "list":
                    TaskList.list(currentDescription);
                    break;
                case "mark":
                    TaskList.mark(Integer.parseInt(currentCmd[1]) - 1, true);
                    break;
                case "unmark":
                    TaskList.mark(Integer.parseInt(currentCmd[1]) - 1, false);
                    break;
                case "delete":
                    TaskList.deleteTask(Integer.parseInt(currentCmd[1]) - 1);
                    break;
                case "todo":
                    TaskList.addTask(currentDescription, "T", true);
                    break;
                case "deadline":
                    TaskList.addTask(currentDescription, "D", true);
                    break;
                case "event":
                    TaskList.addTask(currentDescription, "E", true);
                    break;
                case "save":
                    Storage.saveState(currentDescription, TaskList.getCmdList());
                    break;
                default:
                    throw new SeemlmotException(" I'm sorry, but I don't know what that means :-(");
                }
            } catch (SeemlmotException e) {
                System.out.println(e.getMessage());
            } finally {
                Ui.showLine();
                currentDescription = in.nextLine();
            }
        }
    }


}
