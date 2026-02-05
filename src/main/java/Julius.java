import java.util.Scanner;

public class Julius {
    private static final String DIVIDER = "____________________________________________________________";
    private static final int MAX_TASKS = 100;
    private static final String BOT_NAME = "Julius";
    public static void main(String[] args) {

        // Level-2: Initialise storage array and a counter
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        String logo = "      _ _    _ _      _____ _    _  _____\n"
                + "     | | |  | | |    |_   _| |  | |/ ____|\n"
                + "     | | |  | | |      | | | |  | | (___\n"
                + " _   | | |  | | |      | | | |  | |\\___ \\\n"
                + "| |__| | |__| | |____ _| |_| |__| |____) |\n"
                + " \\____/ \\____/|______|_____|\\____/|_____/\n";

        System.out.println("Hello from\n" + logo);

        // Greet the user
        System.out.println(DIVIDER);
        System.out.println(" Hello! I'm " + BOT_NAME);
        System.out.println(" What can I do for you?");
        System.out.println(DIVIDER);

        Scanner in = new Scanner(System.in);

        while (true) {
            String userInput = in.nextLine().trim();
            String processedInput = userInput.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");

            if (processedInput.contains("bye")) {
                break; // Exit the loop if the user types "bye"
            }

            System.out.println(DIVIDER);

            if (userInput.equalsIgnoreCase("list")) {
                // List all tasks
                if (taskCount == 0) {
                    System.out.println("    No tasks in your list.");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i].toString());
                    }
                }
            } else if (userInput.startsWith("todo ")) {
                // Changed to Level 4: Add a Todo task
                String description = userInput.substring(5).trim();
                if (!description.isEmpty()) {
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1].toString());
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else {
                    System.out.println("    Please provide a task description.");
                }
            } else if (userInput.startsWith("deadline ")) {
                // Level 4: Add a Deadline task
                String remainder = userInput.substring(9).trim();
                int byIndex = remainder.indexOf("/by ");

                if (byIndex != -1) {
                    String description = remainder.substring(0, byIndex).trim();
                    String by = remainder.substring(byIndex + 4).trim();

                    if (!description.isEmpty() && !by.isEmpty()) {
                        tasks[taskCount] = new Deadline(description, by);
                        taskCount++;
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks[taskCount - 1].toString());
                        System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    } else {
                        System.out.println("    Please provide both description and deadline.");
                    }
                } else {
                    System.out.println("    Please use format: deadline <description> /by <date>");
                }
            } else if (userInput.startsWith("event ")) {
                // Add an Event task
                String remainder = userInput.substring(6).trim();
                int fromIndex = remainder.indexOf("/from ");
                int toIndex = remainder.indexOf("/to ");

                if (fromIndex != -1 && toIndex != -1 && toIndex > fromIndex) {
                    String description = remainder.substring(0, fromIndex).trim();
                    String from = remainder.substring(fromIndex + 6, toIndex).trim();
                    String to = remainder.substring(toIndex + 4).trim();

                    if (!description.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
                        tasks[taskCount] = new Event(description, from, to);
                        taskCount++;
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks[taskCount - 1].toString());
                        System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    } else {
                        System.out.println("    Please provide description, from and to times.");
                    }
                } else {
                    System.out.println("    Please use format: event <description> /from <start> /to <end>");
                }
            } else if (userInput.startsWith("mark ")) {
                // Mark as done by mark [index] command of the list
                try {
                    int index = Integer.parseInt(userInput.substring(5).trim()) - 1;
                    if (index >= 0 && index < taskCount) {
                        tasks[index].markAsDone();
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks[index].toString());
                    } else {
                        System.out.println("    Invalid task index.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("    Please provide a valid task index to mark.");
                }
            } else if (userInput.startsWith("unmark ")) {
                // Level 3: Unmark as not done by unmark [index] command
                try {
                    int index = Integer.parseInt(userInput.substring(7).trim()) - 1;
                    if (index >= 0 && index < taskCount) {
                        tasks[index].markAsNotDone();
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks[index].toString());
                    } else {
                        System.out.println("    Invalid task index.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("    Please provide a valid task index to unmark.");
                }
            } else {
                System.out.println("    " + userInput); // Echo the input
            }

            System.out.println(DIVIDER);
        }

        // Exit message
        System.out.println(DIVIDER);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}