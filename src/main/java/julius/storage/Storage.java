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
 * Handles loading and saving of tasks to a file on disk.
 * File format per line:
 *   T | 0 | description
 *   D | 1 | description | by
 *   E | 0 | description | from | to
 */
public class Storage {
    private static final String DEFAULT_FILE_PATH = "./data/julius.txt";

    private final File file;

    /** Creates a Storage using the default data file path. */
    public Storage() {
        this(DEFAULT_FILE_PATH);
    }

    /** Creates a Storage using a custom file path. */
    public Storage(String filePath) {
        this.file = new File(filePath);
        ensureDirectoryExists();
    }

    /** Creates the parent directory of the data file if it does not yet exist. */
    private void ensureDirectoryExists() {
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    /**
     * Loads tasks from the data file.
     * Skips corrupted lines with a warning.
     *
     * @return List of tasks loaded from disk.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();

        if (!file.exists()) {
            // Fresh start — no file yet, return empty list
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
     * Saves all tasks to the data file, overwriting previous content.
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
     * Converts a task to its file storage string format.
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
     * Parses a single line from the data file into a Task.
     * Throws IllegalArgumentException if the line format is invalid.
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