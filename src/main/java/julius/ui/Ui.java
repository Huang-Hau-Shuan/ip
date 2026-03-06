package julius.ui;

import julius.task.Task;
import julius.task.TaskList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Handles all interaction with the user — reading input from standard input
 * and printing output to standard output.
 * <p>
 * All user-visible strings (dividers, messages, task listings) are produced here,
 * keeping presentation logic out of the rest of the application.
 * </p>
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BOT_NAME = "Julius";

    private final Scanner scanner;

    /**
     * Creates a Ui instance backed by standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads and returns the next line of user input, trimmed of leading and trailing whitespace.
     *
     * @return the trimmed input line
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints the ASCII logo, opening divider, greeting, and closing divider.
     */
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

    /**
     * Prints the goodbye message.
     */
    public void showGoodbye() {
        showDivider();
        System.out.println(" Bye. Hope to see you again soon!");
        showDivider();
    }

    /**
     * Prints the horizontal divider line used to separate responses.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Prints a user-facing error message with indentation.
     *
     * @param message the error text to display
     */
    public void showError(String message) {
        System.out.println("    " + message);
    }

    /**
     * Prints all tasks in the given task list, numbered from 1.
     * Prints a placeholder message if the list is empty.
     *
     * @param tasks the task list to display
     */
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

    /**
     * Prints a confirmation that a task has been added to the list.
     *
     * @param task       the newly added task
     * @param totalTasks the total number of tasks now in the list
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task.toString());
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Prints a confirmation that a task has been removed from the list.
     *
     * @param task       the task that was deleted
     * @param totalTasks the total number of tasks remaining in the list
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task.toString());
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Prints a confirmation that a task has been marked as done.
     *
     * @param task the task that was marked done
     */
    public void showTaskMarkedDone(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task.toString());
    }

    /**
     * Prints a confirmation that a task has been marked as not done.
     *
     * @param task the task that was unmarked
     */
    public void showTaskMarkedNotDone(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task.toString());
    }

    /**
     * Prints all tasks from a keyword search result, numbered from 1.
     * Prints a placeholder message if no tasks matched.
     *
     * @param matched the list of tasks matching the search keyword
     */
    public void showMatchingTasks(List<Task> matched) {
        if (matched.isEmpty()) {
            System.out.println(" No matching tasks found.");
            return;
        }
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < matched.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matched.get(i).toString());
        }
    }

    /**
     * Prints all deadline tasks due on the specified date, numbered from 1.
     * Prints a placeholder message if no deadlines fall on that date.
     *
     * @param matched the list of deadlines due on {@code date}
     * @param date    the date that was queried, used in the header message
     */
    public void showTasksOnDate(List<Task> matched, LocalDate date) {
        String formatted = date.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        if (matched.isEmpty()) {
            System.out.println(" No deadlines found on " + formatted + ".");
            return;
        }
        System.out.println(" Here are the deadlines on " + formatted + ":");
        for (int i = 0; i < matched.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matched.get(i).toString());
        }
    }
}
