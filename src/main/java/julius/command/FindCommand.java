package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

import java.util.List;

/**
 * Finds and lists all tasks whose description contains the given keyword.
 * Command syntax: {@code find <keyword>}
 */
public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        List<Task> matched = tasks.findByKeyword(keyword);
        ui.showMatchingTasks(matched);
    }
}
