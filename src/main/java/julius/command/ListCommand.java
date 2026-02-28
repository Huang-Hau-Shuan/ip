package julius.command;

import julius.storage.Storage;
import julius.task.TaskList;
import julius.ui.Ui;

/** Lists all tasks currently in the task list. */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
