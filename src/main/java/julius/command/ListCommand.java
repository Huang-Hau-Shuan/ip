package julius.command;

import julius.storage.Storage;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Displays all tasks currently in the task list.
 * Command syntax: {@code list}
 */
public class ListCommand extends Command {

    /**
     * Prints the full task list to the user via the UI.
     * No changes are made to the task list or storage.
     *
     * @param tasks   the current task list
     * @param ui      the UI for user-facing output
     * @param storage the storage layer (unused)
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
