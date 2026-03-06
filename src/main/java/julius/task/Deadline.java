package julius.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that must be completed by a specific date and time.
 * Displayed with the {@code [D]} prefix.
 * <p>
 * The due date/time is accepted and stored internally in {@code yyyy-MM-dd HHmm} format
 * (e.g. {@code 2019-12-02 1800}) and displayed to the user in {@code MMM dd yyyy, h:mma}
 * format (e.g. {@code Dec 02 2019, 6:00PM}).
 * </p>
 */
public class Deadline extends Task {
    /** Input / storage format: yyyy-MM-dd HHmm (e.g. 2019-12-02 1800) */
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    /** Human-readable display format: MMM dd yyyy, h:mma (e.g. Dec 02 2019, 6:00PM) */
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    protected LocalDateTime by;

    /**
     * Creates a Deadline task whose due date/time is parsed from {@code byString}.
     *
     * @param description human-readable description of the task
     * @param byString    due date/time string in {@code yyyy-MM-dd HHmm} format
     * @throws IllegalArgumentException if {@code byString} does not match the expected format
     */
    public Deadline(String description, String byString) {
        super(description);
        try {
            this.by = LocalDateTime.parse(byString.trim(), INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date/time format. Please use: yyyy-MM-dd HHmm (e.g. 2019-12-02 1800)");
        }
    }

    /**
     * Returns the due date/time formatted for file storage ({@code yyyy-MM-dd HHmm}).
     * This value is stable and suitable for round-tripping through the data file.
     *
     * @return due date/time string in storage format
     */
    public String getBy() {
        return by.format(INPUT_FORMAT);
    }

    /**
     * Returns only the date portion of the due date/time.
     * Used for date-based filtering (e.g. the {@code deadline on} command).
     *
     * @return the {@link LocalDate} on which this task is due
     */
    public LocalDate getDate() {
        return by.toLocalDate();
    }

    /**
     * Returns a formatted string representation of this Deadline task,
     * including the due date/time in human-readable form.
     *
     * @return task string prefixed with {@code [D]}
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
