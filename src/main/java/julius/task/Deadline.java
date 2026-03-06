package julius.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    /** Input / storage format: yyyy-MM-dd HHmm (e.g. 2019-12-02 1800) */
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    /** Human-readable display format: MMM dd yyyy, h:mma (e.g. Dec 02 2019, 6:00PM) */
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    protected LocalDateTime by;

    /**
     * Creates a Deadline whose due date is parsed from {@code byString}.
     *
     * @param description task description
     * @param byString    date/time string in {@code yyyy-MM-dd HHmm} format
     * @throws IllegalArgumentException if {@code byString} cannot be parsed
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
     * Returns the due date/time in the stable storage format ({@code yyyy-MM-dd HHmm}).
     * Used by Storage when writing to disk.
     */
    public String getBy() {
        return by.format(INPUT_FORMAT);
    }

    /** Returns just the date portion, for date-based filtering. */
    public LocalDate getDate() {
        return by.toLocalDate();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
