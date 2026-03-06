package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Adds a {@link julius.task.Deadline} task to the task list.
 * Command syntax: {@code deadline <description> /by <yyyy-MM-dd HHmm>}
 */
public class AddDeadlineCommand extends Command {
    private final String description;
    private final String by;

    /**
     * Creates an AddDeadlineCommand with the given description and due date/time string.
     *
     * @param description the description of the deadline task
     * @param by          the due date/time string in {@code yyyy-MM-dd HHmm} format
     */
    public AddDeadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }

    /**
     * Adds the deadline to the task list, confirms the addition via the UI,
     * and persists the updated list to storage.
     *
     * @param tasks   the current task list
     * @param ui      the UI for user-facing output
     * @param storage the storage layer for persistence
     * @throws JuliusException if the description is blank or the date format is invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        Task added = tasks.addDeadline(description, by);
        ui.showTaskAdded(added, tasks.size());
        storage.save(tasks.getAll());
    }
}
