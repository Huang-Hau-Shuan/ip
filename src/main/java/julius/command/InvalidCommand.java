package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Represents an unrecognised or malformed command.
 * Always throws a JuliusException when executed.
 */
public class InvalidCommand extends Command {
    private final String errorMessage;

    public InvalidCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        throw new JuliusException(errorMessage);
    }
}
