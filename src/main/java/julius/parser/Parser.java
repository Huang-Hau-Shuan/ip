package julius.parser;

import julius.exception.JuliusException;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Makes sense of raw user input and executes the corresponding action.
 * Owns all string-parsing logic (prefix lengths, flag tokens, if-else dispatch).
 */
public class Parser {

    // ----------------------------------------------------------------
    // Prefix lengths for trimming the command keyword from user input
    // ----------------------------------------------------------------
    private static final int TODO_PREFIX_LENGTH = 5;       // "todo "
    private static final int DEADLINE_PREFIX_LENGTH = 9;   // "deadline "
    private static final int EVENT_PREFIX_LENGTH = 6;      // "event "
    private static final int MARK_PREFIX_LENGTH = 5;       // "mark "
    private static final int UNMARK_PREFIX_LENGTH = 7;     // "unmark "
    private static final int DELETE_PREFIX_LENGTH = 7;     // "delete "

    /**
     * Parses {@code userInput} and immediately executes the matching action
     * against the provided {@code TaskList} and {@code Ui}.
     *
     * @param userInput the raw, trimmed line entered by the user
     * @param tasks     the live task list to mutate
     * @param ui        used to print responses
     * @throws JuliusException for known user errors (bad format, empty description, etc.)
     */
    public void parse(String userInput, TaskList tasks, Ui ui) throws JuliusException {
        if (userInput.equalsIgnoreCase("list")) {
            handleList(tasks, ui);

        } else if (userInput.equalsIgnoreCase("todo")) {
            throw new JuliusException("The description of a todo cannot be empty.");

        } else if (userInput.startsWith("todo ")) {
            handleTodo(userInput, tasks, ui);

        } else if (userInput.equalsIgnoreCase("deadline")) {
            throw new JuliusException("The description of a deadline cannot be empty.");

        } else if (userInput.startsWith("deadline ")) {
            handleDeadline(userInput, tasks, ui);

        } else if (userInput.equalsIgnoreCase("event")) {
            throw new JuliusException("The description of an event cannot be empty.");

        } else if (userInput.startsWith("event ")) {
            handleEvent(userInput, tasks, ui);

        } else if (userInput.equalsIgnoreCase("mark")) {
            throw new JuliusException("Please provide a task number to mark.");

        } else if (userInput.startsWith("mark ")) {
            handleMark(userInput, tasks, ui);

        } else if (userInput.equalsIgnoreCase("unmark")) {
            throw new JuliusException("Please provide a task number to unmark.");

        } else if (userInput.startsWith("unmark ")) {
            handleUnmark(userInput, tasks, ui);

        } else if (userInput.equalsIgnoreCase("delete")) {
            throw new JuliusException("Please provide a task number to delete.");

        } else if (userInput.startsWith("delete ")) {
            handleDelete(userInput, tasks, ui);

        } else {
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
    }

    /**
     * Returns true when the input is a recognised exit command.
     * Kept in Parser so Julius.java has no string-matching logic at all.
     */
    public boolean isExit(String userInput) {
        String processed = userInput.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
        return processed.contains("bye");
    }

    // ----------------------------------------------------------------
    // Private command handlers
    // ----------------------------------------------------------------

    private void handleList(TaskList tasks, Ui ui) {
        ui.showTaskList(tasks);
    }

    private void handleTodo(String userInput, TaskList tasks, Ui ui) throws JuliusException {
        String description = userInput.substring(TODO_PREFIX_LENGTH).trim();
        Task added = tasks.addTodo(description);
        ui.showTaskAdded(added, tasks.size());
    }

    private void handleDeadline(String userInput, TaskList tasks, Ui ui) throws JuliusException {
        String remainder = userInput.substring(DEADLINE_PREFIX_LENGTH).trim();
        int byIndex = remainder.indexOf("/by ");
        if (byIndex == -1) {
            throw new JuliusException("Please use format: deadline <description> /by <date>");
        }
        String description = remainder.substring(0, byIndex).trim();
        String by = remainder.substring(byIndex + 4).trim();
        Task added = tasks.addDeadline(description, by);
        ui.showTaskAdded(added, tasks.size());
    }

    private void handleEvent(String userInput, TaskList tasks, Ui ui) throws JuliusException {
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
        ui.showTaskAdded(added, tasks.size());
    }

    private void handleMark(String userInput, TaskList tasks, Ui ui) throws JuliusException {
        try {
            int index = parseIndex(userInput, MARK_PREFIX_LENGTH);
            Task marked = tasks.markDone(index);
            ui.showTaskMarkedDone(marked);
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to mark.");
        }
    }

    private void handleUnmark(String userInput, TaskList tasks, Ui ui) throws JuliusException {
        try {
            int index = parseIndex(userInput, UNMARK_PREFIX_LENGTH);
            Task marked = tasks.markNotDone(index);
            ui.showTaskMarkedNotDone(marked);
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to unmark.");
        }
    }

    private void handleDelete(String userInput, TaskList tasks, Ui ui) throws JuliusException {
        try {
            int index = parseIndex(userInput, DELETE_PREFIX_LENGTH);
            Task deleted = tasks.delete(index);
            ui.showTaskDeleted(deleted, tasks.size());
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to delete.");
        }
    }

    // ----------------------------------------------------------------
    // Private parse helpers
    // ----------------------------------------------------------------

    /** Converts the numeric suffix of a command string to a 0-based task index. */
    private int parseIndex(String input, int prefixLength) {
        return Integer.parseInt(input.substring(prefixLength).trim()) - 1;
    }
}
