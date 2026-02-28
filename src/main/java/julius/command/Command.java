package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Abstract base for all commands.
 * Each concrete subclass encapsulates one user action.
 */
public abstract class Command {

    /**
     * Executes the command against the given task list, UI, and storage.
     *
     * @throws JuliusException for any known user-facing error
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException;

    /**
     * Returns true if this command should terminate the application loop.
     * Overridden only by {@link julius.command.ExitCommand}.
     */
    public boolean isExit() {
        return false;
    }
}
