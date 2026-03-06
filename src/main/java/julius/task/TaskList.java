package julius.task;

import julius.exception.JuliusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the task list and exposes operations to add, delete,
 * mark, and retrieve tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty TaskList. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** Creates a TaskList pre-populated from a loaded list. */
    public TaskList(List<Task> loadedTasks) {
        this.tasks = new ArrayList<>(loadedTasks);
    }

    // Accessors

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns a copy of the internal list (for saving to disk). */
    public ArrayList<Task> getAll() {
        return new ArrayList<>(tasks);
    }

    // Mutation operations

    /**
     * Adds a Todo task.
     *
     * @return the newly created Todo
     * @throws JuliusException if description is blank
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
     * Adds a Deadline task.
     *
     * @return the newly created Deadline
     * @throws JuliusException if description or by-date is blank / format wrong
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
     * Adds an Event task.
     *
     * @return the newly created Event
     * @throws JuliusException if any field is blank
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
     * @throws JuliusException if the index is out of range
     */
    public Task delete(int index) throws JuliusException {
        validateIndex(index);
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given 0-based index as done.
     *
     * @throws JuliusException if the index is out of range
     */
    public Task markDone(int index) throws JuliusException {
        validateIndex(index);
        tasks.get(index).markAsDone();
        return tasks.get(index);
    }

    /**
     * Marks the task at the given 0-based index as not done.
     *
     * @throws JuliusException if the index is out of range
     */
    public Task markNotDone(int index) throws JuliusException {
        validateIndex(index);
        tasks.get(index).markAsNotDone();
        return tasks.get(index);
    }

    /**
     * Returns all Deadline tasks whose due date matches {@code date}.
     * (Events remain free-text for now; only typed Deadlines are filtered.)
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

    // Private helpers

    private void validateIndex(int index) throws JuliusException {
        if (index < 0 || index >= tasks.size()) {
            throw new JuliusException("Task number out of range. You ONLY have " + tasks.size() + " tasks.");
        }
    }
}
