package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Adds a {@link julius.task.Event} task to the task list.
 * Command syntax: {@code event <description> /from <start> /to <end>}
 */
public class AddEventCommand extends Command {
    private final String description;
    private final String from;
    private final String to;

    /**
     * Creates an AddEventCommand with the given description and time window.
     *
     * @param description the description of the event task
     * @param from        free-text start time (e.g. {@code Mon 2pm})
     * @param to          free-text end time (e.g. {@code 4pm})
     */
    public AddEventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    /**
     * Adds the event to the task list, confirms the addition via the UI,
     * and persists the updated list to storage.
     *
     * @param tasks   the current task list
     * @param ui      the UI for user-facing output
     * @param storage the storage layer for persistence
     * @throws JuliusException if any field is blank
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        Task added = tasks.addEvent(description, from, to);
        ui.showTaskAdded(added, tasks.size());
        storage.save(tasks.getAll());
    }
}
