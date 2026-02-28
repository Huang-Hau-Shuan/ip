package julius;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

import java.util.Scanner;

public class Julius {
    private static final int TODO_PREFIX_LENGTH = 5;
    private static final int DEADLINE_PREFIX_LENGTH = 9;
    private static final int EVENT_PREFIX_LENGTH = 6;
    private static final int MARK_PREFIX_LENGTH = 5;
    private static final int UNMARK_PREFIX_LENGTH = 7;
    private static final int DELETE_PREFIX_LENGTH = 7;

    private static final Storage storage = new Storage();
    private static final TaskList tasks = new TaskList(storage.load());
    private static final Ui ui = new Ui();

    public static void main(String[] args) {
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        runCommandLoop(scanner);
        ui.showGoodbye();
    }


    private static void saveTasksToDisk() {
        storage.save(tasks.getAll());
    }

    private static void runCommandLoop(Scanner scanner) {
        while (true) {
            String userInput = scanner.nextLine().trim();

            if (isExitCommand(userInput)) {
                break;
            }

            ui.showDivider();
            executeCommand(userInput);
            ui.showDivider();
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
        Task added = tasks.addTodo(description);
        printTaskAddedMessage(added);
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
        Task added = tasks.addDeadline(description, by);
        printTaskAddedMessage(added);
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
        Task added = tasks.addEvent(description, from, to);
        printTaskAddedMessage(added);
        saveTasksToDisk();
    }

    private static void markTaskAsDone(String userInput) throws JuliusException {
        try {
            int index = parseTaskIndex(userInput, MARK_PREFIX_LENGTH);
            Task marked = tasks.markDone(index);
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + marked.toString());
            saveTasksToDisk();
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to mark.");
        }
    }

    private static void markTaskAsNotDone(String userInput) throws JuliusException {
        try {
            int index = parseTaskIndex(userInput, UNMARK_PREFIX_LENGTH);
            Task marked = tasks.markNotDone(index);
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + marked.toString());
            saveTasksToDisk();
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to unmark.");
        }
    }

    private static void deleteTask(String userInput) throws JuliusException {
        try {
            int index = parseTaskIndex(userInput, DELETE_PREFIX_LENGTH);
            Task deleted = tasks.delete(index);
            System.out.println(" Noted. I've removed this task:");
            System.out.println("   " + deleted.toString());
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
            saveTasksToDisk();
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to delete.");
        }
    }

    private static int parseTaskIndex(String input, int prefixLength) {
        return Integer.parseInt(input.substring(prefixLength).trim()) - 1;
    }

    private static void printTaskAddedMessage(Task task) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task.toString());
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }
}
