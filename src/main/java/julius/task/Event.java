package julius.task;

/**
 * Represents a task that takes place over a specific time window.
 * Displayed with the {@code [E]} prefix.
 * <p>
 * The start and end times are stored as free-text strings
 * (e.g. {@code Mon 2pm} and {@code 4pm}).
 * </p>
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates a new Event task.
     *
     * @param description human-readable description of the event
     * @param from        free-text start time or date (e.g. {@code Mon 2pm})
     * @param to          free-text end time or date (e.g. {@code 4pm})
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start time of this event.
     *
     * @return start time string
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the end time of this event.
     *
     * @return end time string
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns a formatted string representation of this Event task,
     * including the start and end times.
     *
     * @return task string prefixed with {@code [E]}
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
