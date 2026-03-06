package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Represents a command that could not be recognised or was structurally malformed.
 * <p>
 * Always throws a {@link JuliusException} when executed, causing the main loop
 * to display the stored error message to the user.
 * </p>
 */
public class InvalidCommand extends Command {
    private final String errorMessage;

    /**
     * Creates an InvalidCommand that will surface the given message as an error.
     *
     * @param errorMessage the user-facing explanation of what went wrong
     */
    public InvalidCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Always throws a {@link JuliusException} containing the error message.
     *
     * @param tasks   the current task list (unused)
     * @param ui      the UI for user-facing output (unused; error is thrown, not printed here)
     * @param storage the storage layer (unused)
     * @throws JuliusException always, with the message supplied at construction time
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        throw new JuliusException(errorMessage);
    }
}
