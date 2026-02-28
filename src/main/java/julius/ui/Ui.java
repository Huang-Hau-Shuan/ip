package julius.ui;

import julius.task.Task;
import julius.task.TaskList;

import java.util.Scanner;

/**
 * Deals with interactions with the user.
 * Handles all input reading and output printing.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BOT_NAME = "Julius";

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Reads and returns the next line of user input, trimmed of whitespace. */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Prints the opening divider, greeting, and ASCII logo. */
    public void showWelcome() {
        String logo = "      _ _    _ _      _____ _    _  _____\n"
                + "     | | |  | | |    |_   _| |  | |/ ____|\n"
                + "     | | |  | | |      | | | |  | | (___\n"
                + " _   | | |  | | |      | | | |  | |\\___ \\\n"
                + "| |__| | |__| | |____ _| |_| |__| |____) |\n"
                + " \\____/ \\____/|______|_____|\\____/|_____/\n";

        System.out.println("Hello from\n" + logo);
        showDivider();
        System.out.println(" Hello! I'm " + BOT_NAME);
        System.out.println(" What can I do for you?");
        showDivider();
    }

    /** Prints the goodbye message. */
    public void showGoodbye() {
        showDivider();
        System.out.println(" Bye. Hope to see you again soon!");
        showDivider();
    }

    /** Prints the horizontal divider line. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /** Prints an error message. */
    public void showError(String message) {
        System.out.println("    " + message);
    }

    /** Prints all tasks in the list. */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println("    No tasks in your list.");
            return;
        }
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i).toString());
        }
    }

    /** Prints the confirmation that a task was added. */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task.toString());
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
    }

    /** Prints the confirmation that a task was deleted. */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task.toString());
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
    }

    /** Prints the confirmation that a task was marked done. */
    public void showTaskMarkedDone(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task.toString());
    }

    /** Prints the confirmation that a task was marked not done. */
    public void showTaskMarkedNotDone(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task.toString());
    }
}
