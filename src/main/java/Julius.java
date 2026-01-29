import java.util.Scanner;

public class Julius {
    public static void main(String[] args) {
        String divider = "____________________________________________________________";

        // Level-2: Initialise storage array and a counter
        Task[] tasks = new Task[100];
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
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("    " + (i + 1) + ". " + tasks[i].toString());
                    }
                }
            } else if (processedInput.startsWith("add ")) {
                // Level-2: Add a new task (normalize to start with "[ ] ")
                String task = input.substring(4).trim();
                if (!task.isEmpty()) {
                    // Remove any existing leading "[ ]" or "[X]" (case-insensitive) and leading spaces
                    String cleaned = task.replaceFirst("^\\s*\\[[ xX]\\]\\s*", "");
                    tasks[taskCount] = new Task(cleaned);
                     taskCount++;
                     System.out.println("    Added task: " + cleaned);
                 } else {
                     System.out.println("    Please provide a task to add.");
                 }
             } else if (processedInput.startsWith("mark ")) {
                 // Level-3: Mark as done by mark [index] command of the list
                 try {
                     int index = Integer.parseInt(processedInput.substring(5).trim()) - 1;
                     if (index >= 0 && index < taskCount) {
                        tasks[index].markAsDone();
                        System.out.println("    Marked task " + (index + 1) + " as done.");
                     } else {
                         System.out.println("    Invalid task index.");
                     }
                 } catch (NumberFormatException e) {
                     System.out.println("    Please provide a valid task index to mark.");
                 }
             } else if (processedInput.startsWith("unmark ")) {
                 // Level-3: Unmark as not done by unmark [index] command
                 try {
                     int index = Integer.parseInt(processedInput.substring(7).trim()) - 1;
                     if (index >= 0 && index < taskCount) {
                        tasks[index].markAsNotDone();
                        System.out.println("    Unmarked task " + (index + 1) + " as not done.");
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
