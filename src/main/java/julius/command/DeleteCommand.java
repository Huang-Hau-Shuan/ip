package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Deletes a task from the task list by its 1-based user-facing number.
 * Command syntax: {@code delete <task number>}
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a DeleteCommand targeting the given position in the task list.
     *
     * @param index 0-based task index (converted from the 1-based user input by {@link julius.parser.Parser})
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Removes the task at the stored index, confirms the deletion via the UI,
     * and persists the updated list to storage.
     *
     * @param tasks   the current task list
     * @param ui      the UI for user-facing output
     * @param storage the storage layer for persistence
     * @throws JuliusException if the index is out of range
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        Task deleted = tasks.delete(index);
        ui.showTaskDeleted(deleted, tasks.size());
        storage.save(tasks.getAll());
    }
}
