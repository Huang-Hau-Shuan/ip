package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/** Adds a Todo task to the list. */
public class AddTodoCommand extends Command {

    private final String description;

    public AddTodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        Task added = tasks.addTodo(description);
        ui.showTaskAdded(added, tasks.size());
        storage.save(tasks.getAll());
    }
}
