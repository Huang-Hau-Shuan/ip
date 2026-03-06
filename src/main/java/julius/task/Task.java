package julius.task;

/**
 * Represents a generic task with a description and a completion status.
 * <p>
 * This is the base class for all task types ({@link Todo}, {@link Deadline}, {@link Event}).
 * </p>
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new incomplete task with the given description.
     *
     * @param description human-readable description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns {@code "X"} if the task is done, or a space character if not.
     * Used when rendering the task as a string.
     *
     * @return single-character status icon
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns whether this task has been marked as done.
     *
     * @return {@code true} if done, {@code false} otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the description of this task.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns a formatted string representation of this task,
     * including its completion status and description.
     *
     * @return formatted task string
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
