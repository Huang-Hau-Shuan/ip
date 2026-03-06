package julius.command;

import julius.storage.Storage;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Terminates the application's main command loop.
 * Command syntax: {@code bye}
 */
public class ExitCommand extends Command {

    /**
     * Prints the goodbye message to the user.
     * No changes are made to the task list or storage.
     *
     * @param tasks   the current task list (unused)
     * @param ui      the UI for user-facing output
     * @param storage the storage layer (unused)
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Returns {@code true} to signal the main loop to stop.
     *
     * @return {@code true} always
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
