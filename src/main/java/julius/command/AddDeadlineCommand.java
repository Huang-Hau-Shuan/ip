package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/** Adds a Deadline task to the list. */
public class AddDeadlineCommand extends Command {
    private final String description;
    private final String by;

    public AddDeadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        Task added = tasks.addDeadline(description, by);
        ui.showTaskAdded(added, tasks.size());
        storage.save(tasks.getAll());
    }
}
