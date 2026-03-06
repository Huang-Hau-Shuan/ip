package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

import java.time.LocalDate;
import java.util.List;

/**
 * Lists all {@link julius.task.Deadline} tasks whose due date matches a specified date.
 * Command syntax: {@code deadline on <yyyy-MM-dd>}
 */
public class DeadlineOnCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a DeadlineOnCommand that filters tasks by the given date.
     *
     * @param date the date to filter deadlines by
     */
    public DeadlineOnCommand(LocalDate date) {
        this.date = date;
    }

    /**
     * Retrieves all deadlines due on the stored date and displays them via the UI.
     * No changes are made to the task list or storage.
     *
     * @param tasks   the current task list
     * @param ui      the UI for user-facing output
     * @param storage the storage layer (unused)
     * @throws JuliusException not thrown by this command
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        List<Task> matched = tasks.getTasksOnDate(date);
        ui.showTasksOnDate(matched, date);
    }
}
