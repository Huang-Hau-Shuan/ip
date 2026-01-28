import java.util.Scanner;

public class Julius {
    public static void main(String[] args) {
        String divider = "____________________________________________________________";

        // Level-2: Initialise storage array and a counter
        String[] tasks = new String[100];
        int taskCount = 0;

        String logo = "      _ _    _ _      _____ _    _  _____\n"
                + "     | | |  | | |    |_   _| |  | |/ ____|\n"
                + "     | | |  | | |      | | | |  | | (___  \n"
                + " _   | | |  | | |      | | | |  | |\\___ \\ \n"
                + "| |__| | |__| | |____ _| |_| |__| |____) |\n"
                + " \\____/ \\____/|______|_____|\\____/|_____/ \n";

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

            if (input.equalsIgnoreCase("list")){
                // Level-2: List all tasks
                if (taskCount == 0) {
                    System.out.println("    No tasks in your list.");
                } else {
                    // System.out.println("    Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("    " + (i + 1) + ". " + tasks[i]);
                    }
                }
            } else if (processedInput.startsWith("add ")) {
                // Level-2: Add a new task
                String task = input.substring(4).trim();
                if (!task.isEmpty()) {
                    tasks[taskCount] = task;
                    taskCount++;
                    System.out.println("    Added task: " + task);
                } else {
                    System.out.println("    Please provide a task to add.");
                }
            } else {
                // System.out.println("    I'm sorry, I don't understand that command:");
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
