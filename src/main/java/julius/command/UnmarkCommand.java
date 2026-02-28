package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/** Marks a task as not done. */
public class UnmarkCommand extends Command {
    private final int index;

    /** @param index 0-based task index */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        Task marked = tasks.markNotDone(index);
        ui.showTaskMarkedNotDone(marked);
        storage.save(tasks.getAll());
    }
}
