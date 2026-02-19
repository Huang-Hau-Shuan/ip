package julius;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Deadline;
import julius.task.Event;
import julius.task.Task;
import julius.task.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Julius {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BOT_NAME = "Julius";
    private static final int TODO_PREFIX_LENGTH = 5;
    private static final int DEADLINE_PREFIX_LENGTH = 9;
    private static final int EVENT_PREFIX_LENGTH = 6;
    private static final int MARK_PREFIX_LENGTH = 5;
    private static final int UNMARK_PREFIX_LENGTH = 7;
    private static final int DELETE_PREFIX_LENGTH = 7;
    private static final String DATA_FILE_PATH = "./data/julius.txt";

    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Storage storage = new Storage(DATA_FILE_PATH);

    public static void main(String[] args) {
        loadTasksFromDisk();
        showWelcomeMessage();
        Scanner scanner = new Scanner(System.in);
        runCommandLoop(scanner);
        showGoodbyeMessage();
    }

    private static void loadTasksFromDisk() {
        List<Task> loaded = storage.load();
        tasks.addAll(loaded);
    }

    private static void saveTasksToDisk() {
        storage.save(tasks);
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
            } else if (userInput.equalsIgnoreCase("delete")) {
                throw new JuliusException("Please provide a task number to delete.");
            } else if (userInput.startsWith("delete ")) {
                deleteTask(userInput);
            } else {
                // add a list of valid commands in the error message for better user guidance

                throw new JuliusException("Mea Culpa! I don't know what that means! Here are the commands I understand:\n"
                        + " - list\n"
                        + " - todo <description>\n"
                        + " - deadline <description> /by <date>\n"
                        + " - event <description> /from <start> /to <end>\n"
                        + " - mark <task number>\n"
                        + " - unmark <task number>\n"
                        + " - delete <task number>\n"
                        + " - anything containing 'bye' to exit");
            }
        } catch (JuliusException e) {
            System.out.println("    " + e.getMessage());
        } catch (Exception e) {
            System.out.println("    An error occurred: " + e.getMessage());
        }
    }

    private static void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("    No tasks in your list.");
            return;
        }

        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i).toString());
        }
    }

    private static void addTodoTask(String userInput) throws JuliusException {
        String description = userInput.substring(TODO_PREFIX_LENGTH).trim();

        if (description.isEmpty()) {
            throw new JuliusException("Please provide a task description.");
        }

        tasks.add(new Todo(description));
        printTaskAddedMessage();
        saveTasksToDisk();
    }

    private static void addDeadlineTask(String userInput) throws JuliusException {
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

        tasks.add(new Deadline(description, by));
        printTaskAddedMessage();
        saveTasksToDisk();
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
        if (from.isEmpty()) {
            throw new JuliusException("Please provide a start time for the event.");
        }
        if (to.isEmpty()) {
            throw new JuliusException("Please provide an end time for the event.");
        }
        // I choose to be more specific here and split up the checks for clarity.

        tasks.add(new Event(description, from, to));
        printTaskAddedMessage();
        saveTasksToDisk();
    }

    private static void markTaskAsDone(String userInput) throws JuliusException {
        try {
            int index = parseTaskIndex(userInput, MARK_PREFIX_LENGTH);
            validateTaskIndex(index);

            tasks.get(index).markAsDone();
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + tasks.get(index).toString());
            saveTasksToDisk();
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to mark.");
        } catch (IndexOutOfBoundsException e) {
            throw new JuliusException("Task number out of range. You ONLY have " + tasks.size() + " tasks.");
        }
    }

    private static void markTaskAsNotDone(String userInput) throws JuliusException {
        try {
            int index = parseTaskIndex(userInput, UNMARK_PREFIX_LENGTH);
            validateTaskIndex(index);

            tasks.get(index).markAsNotDone();
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + tasks.get(index).toString());
            saveTasksToDisk();
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to unmark.");
        } catch (IndexOutOfBoundsException e) {
            throw new JuliusException("Task number out of range. You ONLY have " + tasks.size() + " tasks.");
        }
    }

    private static void deleteTask(String userInput) throws JuliusException {
        try {
            int index = parseTaskIndex(userInput, DELETE_PREFIX_LENGTH);
            validateTaskIndex(index);

            Task deletedTask = tasks.remove(index);
            System.out.println(" Noted. I've removed this task:");
            System.out.println("   " + deletedTask.toString());
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
            saveTasksToDisk();
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to delete.");
        } catch (IndexOutOfBoundsException e) {
            throw new JuliusException("Task number out of range. You ONLY have " + tasks.size() + " tasks.");
        }
    }

    private static int parseTaskIndex(String input, int prefixLength) {
        return Integer.parseInt(input.substring(prefixLength).trim()) - 1;
    }

    private static void validateTaskIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Invalid task index.");
        }
    }

    private static void printTaskAddedMessage() {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks.get(tasks.size() - 1).toString());
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
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
