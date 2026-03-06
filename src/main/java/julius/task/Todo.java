package julius.task;

/**
 * Represents a task with no specific deadline or time window.
 * Displayed with the {@code [T]} prefix.
 */
public class Todo extends Task {

    /**
     * Creates a new Todo task with the given description.
     *
     * @param description human-readable description of the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a formatted string representation of this Todo task.
     *
     * @return task string prefixed with {@code [T]}
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
