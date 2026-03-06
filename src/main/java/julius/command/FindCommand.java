package julius.command;

import julius.exception.JuliusException;
import julius.storage.Storage;
import julius.task.Task;
import julius.task.TaskList;
import julius.ui.Ui;

import java.util.List;

/**
 * Searches the task list for tasks whose description contains a given keyword
 * and displays the results.
 * Command syntax: {@code find <keyword>}
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a FindCommand that searches for tasks matching the given keyword.
     *
     * @param keyword the search term; matched case-insensitively against task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Searches the task list for matching tasks and displays the results via the UI.
     * No changes are made to the task list or storage.
     *
     * @param tasks   the current task list
     * @param ui      the UI for user-facing output
     * @param storage the storage layer (unused)
     * @throws JuliusException not thrown by this command
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JuliusException {
        List<Task> matched = tasks.findByKeyword(keyword);
        ui.showMatchingTasks(matched);
    }
}
