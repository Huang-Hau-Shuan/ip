package julius.storage;

import julius.task.Deadline;
import julius.task.Event;
import julius.task.Task;
import julius.task.Todo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles loading and saving of tasks to a plain-text file on disk.
 * <p>
 * Each line in the file encodes one task in pipe-delimited format:
 * </p>
 * <pre>
 *   T | 0 | description
 *   D | 1 | description | yyyy-MM-dd HHmm
 *   E | 0 | description | from | to
 * </pre>
 * <p>
 * Corrupted lines are skipped with a console warning rather than aborting the load.
 * </p>
 */
public class Storage {
    private static final String DEFAULT_FILE_PATH = "./data/julius.txt";

    private final File file;

    /**
     * Creates a Storage instance using the default data file path
     * ({@code ./data/julius.txt}).
     */
    public Storage() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Creates a Storage instance that reads from and writes to the given file path.
     * The parent directory is created automatically if it does not exist.
     *
     * @param filePath path to the data file
     */
    public Storage(String filePath) {
        this.file = new File(filePath);
        ensureDirectoryExists();
    }

    /**
     * Loads tasks from the data file.
     * <p>
     * Returns an empty list if the file does not yet exist.
     * Lines that cannot be parsed are skipped with a warning printed to standard output.
     * </p>
     *
     * @return list of tasks loaded from disk; never {@code null}
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();

        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    Task task = parseTask(line);
                    tasks.add(task);
                } catch (Exception e) {
                    System.out.println("    [Warning] Skipping corrupted line " + lineNumber + ": " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("    [Warning] Could not read data file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves all tasks in the given list to the data file, overwriting any previous content.
     * A console warning is printed if the file cannot be written.
     *
     * @param tasks the current list of tasks to persist
     */
    public void save(ArrayList<Task> tasks) {
        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks) {
                writer.write(encodeTask(task) + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("    [Warning] Could not save tasks to file: " + e.getMessage());
        }
    }

    /**
     * Creates the parent directory of the data file if it does not already exist.
     */
    private void ensureDirectoryExists() {
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    /**
     * Converts a {@link Task} to its pipe-delimited file storage string.
     *
     * @param task the task to encode; must be a {@link Todo}, {@link Deadline}, or {@link Event}
     * @return the encoded line ready to be written to the data file
     * @throws IllegalArgumentException if the task type is not recognised
     */
    private String encodeTask(Task task) {
        if (task instanceof Todo) {
            return "T | " + (task.isDone() ? "1" : "0") + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return "D | " + (d.isDone() ? "1" : "0") + " | " + d.getDescription() + " | " + d.getBy();
        } else if (task instanceof Event) {
            Event e = (Event) task;
            return "E | " + (e.isDone() ? "1" : "0") + " | " + e.getDescription()
                    + " | " + e.getFrom() + " | " + e.getTo();
        }
        throw new IllegalArgumentException("Unknown task type: " + task.getClass().getName());
    }

    /**
     * Parses a single pipe-delimited line from the data file into a {@link Task}.
     *
     * @param line the raw line read from disk
     * @return the reconstructed task
     * @throws IllegalArgumentException if the line format is invalid or the task type is unknown
     */
    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            throw new IllegalArgumentException("Too few fields.");
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        Task task;
        switch (type) {
        case "T":
            if (parts.length != 3) {
                throw new IllegalArgumentException("Todo should have exactly 3 fields.");
            }
            task = new Todo(description);
            break;
        case "D":
            if (parts.length != 4) {
                throw new IllegalArgumentException("Deadline should have exactly 4 fields.");
            }
            String by = parts[3].trim();
            task = new Deadline(description, by);
            break;
        case "E":
            if (parts.length != 5) {
                throw new IllegalArgumentException("Event should have exactly 5 fields.");
            }
            String from = parts[3].trim();
            String to = parts[4].trim();
            task = new Event(description, from, to);
            break;
        default:
            throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}