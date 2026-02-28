package julius.parser;

import julius.command.AddDeadlineCommand;
import julius.command.AddEventCommand;
import julius.command.AddTodoCommand;
import julius.command.Command;
import julius.command.DeleteCommand;
import julius.command.ExitCommand;
import julius.command.InvalidCommand;
import julius.command.ListCommand;
import julius.command.MarkCommand;
import julius.command.UnmarkCommand;
import julius.exception.JuliusException;

/**
 * Makes sense of raw user input and returns the matching Command object.
 * Owns all string-parsing logic (prefix lengths, flag tokens, if-else dispatch).
 */
public class Parser {

    private static final int TODO_PREFIX_LENGTH = 5;       // "todo "
    private static final int DEADLINE_PREFIX_LENGTH = 9;   // "deadline "
    private static final int EVENT_PREFIX_LENGTH = 6;      // "event "
    private static final int MARK_PREFIX_LENGTH = 5;       // "mark "
    private static final int UNMARK_PREFIX_LENGTH = 7;     // "unmark "
    private static final int DELETE_PREFIX_LENGTH = 7;     // "delete "

    /**
     * Parses {@code userInput} and returns the corresponding {@link Command}.
     * Never returns null — unrecognised input returns an {@link InvalidCommand}.
     *
     * @param userInput the raw, trimmed line entered by the user
     * @return a ready-to-execute Command
     * @throws JuliusException if the input is structurally invalid (e.g. missing /by)
     */
    public static Command parse(String userInput) throws JuliusException {
        if (userInput.equalsIgnoreCase("bye")) {
            return new ExitCommand();

        } else if (userInput.equalsIgnoreCase("list")) {
            return new ListCommand();

        } else if (userInput.equalsIgnoreCase("todo")) {
            return new InvalidCommand("The description of a todo cannot be empty.");

        } else if (userInput.startsWith("todo ")) {
            return parseTodo(userInput);

        } else if (userInput.equalsIgnoreCase("deadline")) {
            return new InvalidCommand("The description of a deadline cannot be empty.");

        } else if (userInput.startsWith("deadline ")) {
            return parseDeadline(userInput);

        } else if (userInput.equalsIgnoreCase("event")) {
            return new InvalidCommand("The description of an event cannot be empty.");

        } else if (userInput.startsWith("event ")) {
            return parseEvent(userInput);

        } else if (userInput.equalsIgnoreCase("mark")) {
            return new InvalidCommand("Please provide a task number to mark.");

        } else if (userInput.startsWith("mark ")) {
            return parseMark(userInput);

        } else if (userInput.equalsIgnoreCase("unmark")) {
            return new InvalidCommand("Please provide a task number to unmark.");

        } else if (userInput.startsWith("unmark ")) {
            return parseUnmark(userInput);

        } else if (userInput.equalsIgnoreCase("delete")) {
            return new InvalidCommand("Please provide a task number to delete.");

        } else if (userInput.startsWith("delete ")) {
            return parseDelete(userInput);

        } else {
            return new InvalidCommand("Mea Culpa! I don't know what that means! Here are the commands I understand:\n"
                    + " - list\n"
                    + " - todo <description>\n"
                    + " - deadline <description> /by <date>\n"
                    + " - event <description> /from <start> /to <end>\n"
                    + " - mark <task number>\n"
                    + " - unmark <task number>\n"
                    + " - delete <task number>\n"
                    + " - bye to exit");
        }
    }

    // ----------------------------------------------------------------
    // Private parse helpers — each returns a fully constructed Command
    // ----------------------------------------------------------------

    private static Command parseTodo(String userInput) {
        String description = userInput.substring(TODO_PREFIX_LENGTH).trim();
        return new AddTodoCommand(description);
    }

    private static Command parseDeadline(String userInput) throws JuliusException {
        String remainder = userInput.substring(DEADLINE_PREFIX_LENGTH).trim();
        int byIndex = remainder.indexOf("/by ");
        if (byIndex == -1) {
            throw new JuliusException("Please use format: deadline <description> /by <date>");
        }
        String description = remainder.substring(0, byIndex).trim();
        String by = remainder.substring(byIndex + 4).trim();
        return new AddDeadlineCommand(description, by);
    }

    private static Command parseEvent(String userInput) throws JuliusException {
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
        return new AddEventCommand(description, from, to);
    }

    private static Command parseMark(String userInput) throws JuliusException {
        try {
            int index = parseIndex(userInput, MARK_PREFIX_LENGTH);
            return new MarkCommand(index);
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to mark.");
        }
    }

    private static Command parseUnmark(String userInput) throws JuliusException {
        try {
            int index = parseIndex(userInput, UNMARK_PREFIX_LENGTH);
            return new UnmarkCommand(index);
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to unmark.");
        }
    }

    private static Command parseDelete(String userInput) throws JuliusException {
        try {
            int index = parseIndex(userInput, DELETE_PREFIX_LENGTH);
            return new DeleteCommand(index);
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to delete.");
        }
    }

    /** Converts the numeric suffix of a command string to a 0-based task index. */
    private static int parseIndex(String input, int prefixLength) {
        return Integer.parseInt(input.substring(prefixLength).trim()) - 1;
    }
}
