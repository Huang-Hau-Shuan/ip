package julius.parser;

import julius.command.AddDeadlineCommand;
import julius.command.AddEventCommand;
import julius.command.AddTodoCommand;
import julius.command.Command;
import julius.command.DeadlineOnCommand;
import julius.command.DeleteCommand;
import julius.command.ExitCommand;
import julius.command.FindCommand;
import julius.command.InvalidCommand;
import julius.command.ListCommand;
import julius.command.MarkCommand;
import julius.command.UnmarkCommand;
import julius.exception.JuliusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Translates raw user input strings into executable {@link Command} objects.
 * <p>
 * All string-parsing logic — prefix lengths, flag tokens ({@code /by}, {@code /from},
 * {@code /to}), and the if-else dispatch table — is contained here, keeping it out of
 * the main application loop and the command classes themselves.
 * </p>
 * <p>
 * {@link #parse(String)} never returns {@code null}; unrecognised input produces an
 * {@link InvalidCommand} that surfaces a helpful error message to the user.
 * </p>
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
     * <p>
     * The dispatch order matters: more specific prefixes (e.g. {@code deadline on})
     * are checked before their general counterparts (e.g. {@code deadline}).
     * </p>
     *
     * @param userInput the raw, trimmed line entered by the user
     * @return a ready-to-execute {@link Command}; never {@code null}
     * @throws JuliusException if the input is structurally invalid (e.g. missing {@code /by})
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

        } else if (userInput.startsWith("deadline on ")) {
            return parseDeadlineOn(userInput);

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

        } else if (userInput.equalsIgnoreCase("find")) {
            return new InvalidCommand("Please provide a keyword to search for.");

        } else if (userInput.startsWith("find ")) {
            return new FindCommand(userInput.substring(5).trim());

        } else {
            return new InvalidCommand("Mea Culpa! I don't know what that means! Here are the commands I understand:\n"
                    + " - list\n"
                    + " - todo <description>\n"
                    + " - deadline <description> /by <date>  (date: yyyy-MM-dd HHmm)\n"
                    + " - deadline on <date>                 (date: yyyy-MM-dd)\n"
                    + " - event <description> /from <start> /to <end>\n"
                    + " - find <keyword>\n"
                    + " - mark <task number>\n"
                    + " - unmark <task number>\n"
                    + " - delete <task number>\n"
                    + " - bye to exit");
        }
    }

    // ----------------------------------------------------------------
    // Private parse helpers — each returns a fully constructed Command
    // ----------------------------------------------------------------

    /**
     * Parses a {@code todo <description>} command.
     *
     * @param userInput the full input string
     * @return an {@link AddTodoCommand} with the extracted description
     */
    private static Command parseTodo(String userInput) {
        String description = userInput.substring(TODO_PREFIX_LENGTH).trim();
        return new AddTodoCommand(description);
    }

    /**
     * Parses a {@code deadline on <yyyy-MM-dd>} command.
     *
     * @param userInput the full input string
     * @return a {@link DeadlineOnCommand} for the parsed date
     * @throws JuliusException if the date string does not match {@code yyyy-MM-dd}
     */
    private static Command parseDeadlineOn(String userInput) throws JuliusException {
        // "deadline on " is 12 characters
        String dateStr = userInput.substring(12).trim();
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return new DeadlineOnCommand(date);
        } catch (DateTimeParseException e) {
            throw new JuliusException("Invalid date format. Please use: deadline on yyyy-MM-dd (e.g. 2019-12-02)");
        }
    }

    /**
     * Parses a {@code deadline <description> /by <yyyy-MM-dd HHmm>} command.
     *
     * @param userInput the full input string
     * @return an {@link AddDeadlineCommand} with the extracted description and due date
     * @throws JuliusException if the {@code /by} flag is missing
     */
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

    /**
     * Parses an {@code event <description> /from <start> /to <end>} command.
     *
     * @param userInput the full input string
     * @return an {@link AddEventCommand} with the extracted description and time window
     * @throws JuliusException if either the {@code /from} or {@code /to} flag is missing,
     *                         or if {@code /to} appears before {@code /from}
     */
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

    /**
     * Parses a {@code mark <task number>} command.
     *
     * @param userInput the full input string
     * @return a {@link MarkCommand} with the 0-based task index
     * @throws JuliusException if the task number is not a valid integer
     */
    private static Command parseMark(String userInput) throws JuliusException {
        try {
            int index = parseIndex(userInput, MARK_PREFIX_LENGTH);
            return new MarkCommand(index);
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to mark.");
        }
    }

    /**
     * Parses an {@code unmark <task number>} command.
     *
     * @param userInput the full input string
     * @return an {@link UnmarkCommand} with the 0-based task index
     * @throws JuliusException if the task number is not a valid integer
     */
    private static Command parseUnmark(String userInput) throws JuliusException {
        try {
            int index = parseIndex(userInput, UNMARK_PREFIX_LENGTH);
            return new UnmarkCommand(index);
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to unmark.");
        }
    }

    /**
     * Parses a {@code delete <task number>} command.
     *
     * @param userInput the full input string
     * @return a {@link DeleteCommand} with the 0-based task index
     * @throws JuliusException if the task number is not a valid integer
     */
    private static Command parseDelete(String userInput) throws JuliusException {
        try {
            int index = parseIndex(userInput, DELETE_PREFIX_LENGTH);
            return new DeleteCommand(index);
        } catch (NumberFormatException e) {
            throw new JuliusException("Please provide a valid task number to delete.");
        }
    }

    /**
     * Extracts the numeric suffix of a command string and converts it to a 0-based index.
     * For example, {@code "mark 3"} with prefix length 5 yields {@code 2}.
     *
     * @param input        the full command string
     * @param prefixLength the number of characters occupied by the command verb and trailing space
     * @return the 0-based task index
     * @throws NumberFormatException if the suffix is not a valid integer
     */
    private static int parseIndex(String input, int prefixLength) {
        return Integer.parseInt(input.substring(prefixLength).trim()) - 1;
    }
}
