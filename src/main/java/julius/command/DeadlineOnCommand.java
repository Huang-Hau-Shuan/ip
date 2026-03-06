package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

import java.time.LocalDate;
import java.util.List;

/**
 * Lists all deadline tasks whose due date matches a specified date.
 * Command syntax: {@code deadline on yyyy-MM-dd}
 */
public class DeadlineOnCommand extends Command {
    private final LocalDate date;

    public DeadlineOnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        List<Task> matched = tasks.getTasksOnDate(date);
        ui.showTasksOnDate(matched, date);
    }
}
