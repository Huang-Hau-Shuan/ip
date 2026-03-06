package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Marks a task as done.
 * Command syntax: {@code mark <task number>}
 */
public class MarkCommand extends Command {
    private final int index;

    /**
     * Creates a MarkCommand targeting the given position in the task list.
     *
     * @param index 0-based task index (converted from the 1-based user input by {@link julius.parser.Parser})
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    /**
     * Marks the task at the stored index as done, confirms the change via the UI,
     * and persists the updated list to storage.
     *
     * @param tasks   the current task list
     * @param ui      the UI for user-facing output
     * @param storage the storage layer for persistence
     * @throws JuliusException if the index is out of range
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        Task marked = tasks.markDone(index);
        ui.showTaskMarkedDone(marked);
        storage.save(tasks.getAll());
    }
}
