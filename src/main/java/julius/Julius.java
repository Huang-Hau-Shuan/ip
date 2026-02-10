package julius;

import julius.exception.JuliusException;
import julius.task.Deadline;
import julius.task.Event;
import julius.task.Task;
import julius.task.Todo;

import java.util.Scanner;

public class Julius {
    private static final String DIVIDER = "____________________________________________________________";
    private static final int MAX_TASKS = 100;
    private static final String BOT_NAME = "Julius";
    private static final int TODO_PREFIX_LENGTH = 5;
    private static final int DEADLINE_PREFIX_LENGTH = 9;
    private static final int EVENT_PREFIX_LENGTH = 6;
    private static final int MARK_PREFIX_LENGTH = 5;
    private static final int UNMARK_PREFIX_LENGTH = 7;

    private static Task[] tasks = new Task[MAX_TASKS];
    private static int taskCount = 0;

    public static void main(String[] args) {
        showWelcomeMessage();
        Scanner scanner = new Scanner(System.in);
        runCommandLoop(scanner);
        showGoodbyeMessage();
    }

    private static void showWelcomeMessage() {
        String logo = "      _ _    _ _      _____ _    _  _____\n"
                + "     | | |  | | |    |_   _| |  | |/ ____|\n"
                + "     | | |  | | |      | | | |  | | (___\n"
                + " _   | | |  | | |      | | | |  | |\\___ \\\n"
                + "| |__| | |__| | |____ _| |_| |__| |____) |\n"
                + " \\____/ \\____/|______|_____|\\____/|_____/\n";

        System.out.println("Hello from\n" + logo);
        printDivider();
        System.out.println(" Hello! I'm " + BOT_NAME);
        System.out.println(" What can I do for you?");
        printDivider();
    }

    private static void runCommandLoop(Scanner scanner) {
        while (true) {
            String userInput = scanner.nextLine().trim();

            if (isExitCommand(userInput)) {
                break;
            }

            printDivider();
            executeCommand(userInput);
            printDivider();
        }
    }

    private static boolean isExitCommand(String input) {
        String processedInput = input.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
        return processedInput.contains("bye");
    }

    private static void executeCommand(String userInput) {
        try {
            if (userInput.equalsIgnoreCase("list")) {
                listTasks();
            } else if (userInput.equalsIgnoreCase("todo")) {
                throw new JuliusException("The description of a todo cannot be empty.");
            } else if (userInput.startsWith("todo ")) {
                addTodoTask(userInput);
            } else if (userInput.equalsIgnoreCase("deadline")) {
                throw new JuliusException("The description of a deadline cannot be empty.");
            } else if (userInput.startsWith("deadline ")) {
                addDeadlineTask(userInput);
            } else if (userInput.equalsIgnoreCase("event")) {
                throw new JuliusException("The description of an event cannot be empty.");
            } else if (userInput.startsWith("event ")) {
                addEventTask(userInput);
            } else if (userInput.equalsIgnoreCase("mark")) {
                throw new JuliusException("Please provide a task number to mark.");
            } else if (userInput.startsWith("mark ")) {
                markTaskAsDone(userInput);
            } else if (userInput.equalsIgnoreCase("unmark")) {
                throw new JuliusException("Please provide a task number to unmark.");
            } else if (userInput.startsWith("unmark ")) {
                markTaskAsNotDone(userInput);
            } else {
                throw new JuliusException("Mea Culpa! I don't know what that means!");
            }
        } catch (JuliusException e) {
            System.out.println("    " + e.getMessage());
        } catch (Exception e) {
            System.out.println("    An error occurred: " + e.getMessage());
        }
    }

    private static void listTasks() {
        if (taskCount == 0) {
            System.out.println("    No tasks in your list.");
            return;
        }

        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + "." + tasks[i].toString());
        }
    }

    private static void addTodoTask(String userInput) throws JuliusException {
        String description = userInput.substring(TODO_PREFIX_LENGTH).trim();

        if (description.isEmpty()) {
            throw new JuliusException("Please provide a task description.");
        }

        tasks[taskCount] = new Todo(description);
        taskCount++;
        printTaskAddedMessage();
    }

    private static void addDeadlineTask(String userInput) throws JuliusException{
        String remainder = userInput.substring(DEADLINE_PREFIX_LENGTH).trim();
        int byIndex = remainder.indexOf("/by ");

        if (byIndex == -1) {
            throw new JuliusException("Please use format: deadline <description> /by <date>");
        }

        String description = remainder.substring(0, byIndex).trim();
        String by = remainder.substring(byIndex + 4).trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new JuliusException("Please provide both description and deadline.");
        }

        tasks[taskCount] = new Deadline(description, by);
        taskCount++;
        printTaskAddedMessage();
    }

    private static void addEventTask(String userInput) throws JuliusException {
        String remainder = userInput.substring(EVENT_PREFIX_LENGTH).trim();
        int fromIndex = remainder.indexOf("/from ");
        int toIndex = remainder.indexOf("/to ");

        if (fromIndex == -1 || toIndex == -1) {
            throw new JuliusException("Please use format: event <description> /from <start> /to <end>");
        }
        if (toIndex <= fromIndex) {
            throw new JuliusException("The /to must come after /from in the command.");
        }

        String description = remainder.substring(0, fromIndex).trim();
        String from = remainder.substring(fromIndex + 6, toIndex).trim();
        String to = remainder.substring(toIndex + 4).trim();

        if (description.isEmpty()) {
            throw new JuliusException("The description of an event cannot be empty.");
        }
        if (from.isEmpty()){
            throw new JuliusException("Please provide a start time for the event.");
        }
        if (to.isEmpty()){
            throw new JuliusException("Please provide an end time for the event.");
        }
        // I choose to be more specific here and split up the checks for clarity.


        tasks[taskCount] = new Event(description, from, to);
        taskCount++;
        printTaskAddedMessage();
    }

    private static void markTaskAsDone(String userInput) throws JuliusException {
        try {
            int index = parseTaskIndex(userInput, MARK_PREFIX_LENGTH);
            validateTaskIndex(index);

            tasks[index].markAsDone();
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + tasks[index].toString());
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to mark.");
        } catch (IndexOutOfBoundsException e) {
            throw new JuliusException("Task number out of range. You ONLY have " + taskCount + " tasks.");
        }
    }

    private static void markTaskAsNotDone(String userInput) throws JuliusException {
        try {
            int index = parseTaskIndex(userInput, UNMARK_PREFIX_LENGTH);
            validateTaskIndex(index);

            tasks[index].markAsNotDone();
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + tasks[index].toString());
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to unmark.");
        } catch (IndexOutOfBoundsException e) {
            throw new JuliusException("Task number out of range. You ONLY have " + taskCount + " tasks.");
        }
    }

    private static int parseTaskIndex(String input, int prefixLength) {
        return Integer.parseInt(input.substring(prefixLength).trim()) - 1;
    }

    private static void validateTaskIndex(int index) {
        if (index < 0 || index >= taskCount) {
            throw new IndexOutOfBoundsException("Invalid task index.");
        }
    }

    private static void printTaskAddedMessage() {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks[taskCount - 1].toString());
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    private static void printDivider() {
        System.out.println(DIVIDER);
    }

    private static void showGoodbyeMessage() {
        printDivider();
        System.out.println(" Bye. Hope to see you again soon!");
        printDivider();
    }
}