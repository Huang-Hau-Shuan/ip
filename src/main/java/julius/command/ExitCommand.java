package julius.command;

import julius.storage.Storage;
import julius.task.TaskList;
import julius.ui.Ui;

/** Signals the application to exit. */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
