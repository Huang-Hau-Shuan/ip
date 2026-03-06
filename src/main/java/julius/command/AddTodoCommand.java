package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Adds a {@link julius.task.Todo} task to the task list.
 * Command syntax: {@code todo <description>}
 */
public class AddTodoCommand extends Command {

    private final String description;

    /**
     * Creates an AddTodoCommand for a task with the given description.
     *
     * @param description the description of the todo task
     */
    public AddTodoCommand(String description) {
        this.description = description;
    }

    /**
     * Adds the todo to the task list, confirms the addition via the UI,
     * and persists the updated list to storage.
     *
     * @param tasks   the current task list
     * @param ui      the UI for user-facing output
     * @param storage the storage layer for persistence
     * @throws JuliusException if the description is blank
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        Task added = tasks.addTodo(description);
        ui.showTaskAdded(added, tasks.size());
        storage.save(tasks.getAll());
    }
}
