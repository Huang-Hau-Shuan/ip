package julius.task;

import julius.exception.JuliusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the in-memory list of tasks and exposes operations to add, delete,
 * mark, search, and retrieve tasks.
 * <p>
 * Acts as the single source of truth for the current task state during a session.
 * </p>
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList pre-populated with tasks loaded from persistent storage.
     *
     * @param loadedTasks list of tasks previously saved to disk
     */
    public TaskList(List<Task> loadedTasks) {
        this.tasks = new ArrayList<>(loadedTasks);
    }

    // ----------------------------------------------------------------
    // Accessors
    // ----------------------------------------------------------------

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns {@code true} if the list contains no tasks.
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the task at the specified 0-based index without removing it.
     *
     * @param index 0-based position in the list
     * @return task at the given index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns a shallow copy of the internal task list.
     * Intended for use by {@link julius.storage.Storage} when persisting tasks to disk.
     *
     * @return copy of the task list
     */
    public ArrayList<Task> getAll() {
        return new ArrayList<>(tasks);
    }

    // ----------------------------------------------------------------
    // Mutation operations
    // ----------------------------------------------------------------

    /**
     * Creates and adds a new {@link Todo} task to the list.
     *
     * @param description task description; must not be blank
     * @return the newly created {@link Todo}
     * @throws JuliusException if {@code description} is blank
     */
    public Todo addTodo(String description) throws JuliusException {
        if (description.isEmpty()) {
            throw new JuliusException("Please provide a task description.");
        }
        Todo todo = new Todo(description);
        tasks.add(todo);
        return todo;
    }

    /**
     * Creates and adds a new {@link Deadline} task to the list.
     *
     * @param description task description; must not be blank
     * @param by          due date/time string in {@code yyyy-MM-dd HHmm} format; must not be blank
     * @return the newly created {@link Deadline}
     * @throws JuliusException if {@code description} or {@code by} is blank,
     *                         or if {@code by} cannot be parsed
     */
    public Deadline addDeadline(String description, String by) throws JuliusException {
        if (description.isEmpty() || by.isEmpty()) {
            throw new JuliusException("Please provide both description and deadline.");
        }
        try {
            Deadline deadline = new Deadline(description, by);
            tasks.add(deadline);
            return deadline;
        } catch (IllegalArgumentException e) {
            throw new JuliusException(e.getMessage());
        }
    }

    /**
     * Creates and adds a new {@link Event} task to the list.
     *
     * @param description task description; must not be blank
     * @param from        free-text start time; must not be blank
     * @param to          free-text end time; must not be blank
     * @return the newly created {@link Event}
     * @throws JuliusException if any of the three fields is blank
     */
    public Event addEvent(String description, String from, String to) throws JuliusException {
        if (description.isEmpty()) {
            throw new JuliusException("The description of an event cannot be empty.");
        }
        if (from.isEmpty()) {
            throw new JuliusException("Please provide a start time for the event.");
        }
        if (to.isEmpty()) {
            throw new JuliusException("Please provide an end time for the event.");
        }
        Event event = new Event(description, from, to);
        tasks.add(event);
        return event;
    }

    /**
     * Removes and returns the task at the given 0-based index.
     *
     * @param index 0-based position of the task to remove
     * @return the removed task
     * @throws JuliusException if {@code index} is out of range
     */
    public Task delete(int index) throws JuliusException {
        validateIndex(index);
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given 0-based index as done and returns it.
     *
     * @param index 0-based position of the task to mark
     * @return the updated task
     * @throws JuliusException if {@code index} is out of range
     */
    public Task markDone(int index) throws JuliusException {
        validateIndex(index);
        tasks.get(index).markAsDone();
        return tasks.get(index);
    }

    /**
     * Marks the task at the given 0-based index as not done and returns it.
     *
     * @param index 0-based position of the task to unmark
     * @return the updated task
     * @throws JuliusException if {@code index} is out of range
     */
    public Task markNotDone(int index) throws JuliusException {
        validateIndex(index);
        tasks.get(index).markAsNotDone();
        return tasks.get(index);
    }

    // ----------------------------------------------------------------
    // Search and filter operations
    // ----------------------------------------------------------------

    /**
     * Returns all tasks whose description contains {@code keyword},
     * compared case-insensitively.
     *
     * @param keyword search term; must not be {@code null}
     * @return list of matching tasks, possibly empty
     */
    public List<Task> findByKeyword(String keyword) {
        List<Task> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * Returns all {@link Deadline} tasks whose due date matches {@code date} exactly.
     * {@link Event} tasks are not included because their times remain free-text.
     *
     * @param date the date to filter by
     * @return list of matching deadlines, possibly empty
     */
    public List<Task> getTasksOnDate(LocalDate date) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof Deadline) {
                Deadline d = (Deadline) task;
                if (d.getDate().equals(date)) {
                    result.add(d);
                }
            }
        }
        return result;
    }

    // ----------------------------------------------------------------
    // Private helpers
    // ----------------------------------------------------------------

    /**
     * Asserts that {@code index} is a valid 0-based position in the task list.
     *
     * @param index the index to validate
     * @throws JuliusException if the index is negative or greater than or equal to the list size
     */
    private void validateIndex(int index) throws JuliusException {
        if (index < 0 || index >= tasks.size()) {
            throw new JuliusException("Task number out of range. You ONLY have " + tasks.size() + " tasks.");
        }
    }
}
