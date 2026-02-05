import java.util.Scanner;

public class Julius {
    public static void main(String[] args) {
        String divider = "____________________________________________________________";

        // Level-2: Initialise storage array and a counter
        Task[] tasks = new Task[100];
        int taskCount = 0;

        String logo = "      _ _    _ _      _____ _    _  _____\n"
                + "     | | |  | | |    |_   _| |  | |/ ____|\n"
                + "     | | |  | | |      | | | |  | | (___\n"
                + " _   | | |  | | |      | | | |  | |\\___ \\\n"
                + "| |__| | |__| | |____ _| |_| |__| |____) |\n"
                + " \\____/ \\____/|______|_____|\\____/|_____/\n";

        System.out.println("Hello from\n" + logo);
        String name = "Julius";

        // Greet the user
        System.out.println(divider);
        System.out.println(" Hello! I'm " + name);
        System.out.println(" What can I do for you?");
        System.out.println(divider);

        Scanner in = new Scanner(System.in);

        while (true) {
            String input = in.nextLine().trim();
            String processedInput = input.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");

            if (processedInput.contains("bye")) {
                break; // Exit the loop if the user types "bye"
            }

            System.out.println(divider);

            if (input.equalsIgnoreCase("list")) {
                // List all tasks
                if (taskCount == 0) {
                    System.out.println("    No tasks in your list.");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i].toString());
                    }
                }
            } else if (input.startsWith("todo ")) {
                // Changed to Level 4: Add a Todo task
                String description = input.substring(5).trim();
                if (!description.isEmpty()) {
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1].toString());
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else {
                    System.out.println("    Please provide a task description.");
                }
            } else if (input.startsWith("deadline ")) {
                // Level 4: Add a Deadline task
                String remainder = input.substring(9).trim();
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
            } else if (input.startsWith("event ")) {
                // Add an Event task
                String remainder = input.substring(6).trim();
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
            } else if (input.startsWith("mark ")) {
                // Mark as done by mark [index] command of the list
                try {
                    int index = Integer.parseInt(input.substring(5).trim()) - 1;
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
            } else if (input.startsWith("unmark ")) {
                // Level 3: Unmark as not done by unmark [index] command
                try {
                    int index = Integer.parseInt(input.substring(7).trim()) - 1;
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
                System.out.println("    " + input); // Echo the input
            }

            System.out.println(divider);
        }

        // Exit message
        System.out.println(divider);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(divider);
    }
}