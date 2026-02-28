package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/** Marks a task as done. */
public class MarkCommand extends Command {
    private final int index;

    /** @param index 0-based task index */
    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        Task marked = tasks.markDone(index);
        ui.showTaskMarkedDone(marked);
        storage.save(tasks.getAll());
    }
}
