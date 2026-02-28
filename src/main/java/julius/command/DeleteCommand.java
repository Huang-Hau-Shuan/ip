package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/** Deletes a task from the list. */
public class DeleteCommand extends Command {
    private final int index;

    /** @param index 0-based task index */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        Task deleted = tasks.delete(index);
        ui.showTaskDeleted(deleted, tasks.size());
        storage.save(tasks.getAll());
    }
}
