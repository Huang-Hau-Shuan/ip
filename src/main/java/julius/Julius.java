package julius;

import julius.command.Command;
import julius.exception.JuliusException;
import julius.parser.Parser;
import julius.storage.Storage;
import julius.task.TaskList;
import julius.ui.Ui;

/**
 * Entry point and main controller for the Julius task-management chatbot.
 * <p>
 * Wires together {@link Ui}, {@link Storage}, {@link TaskList}, and {@link Parser}
 * into the application's read-evaluate-print loop.
 * </p>
 */
public class Julius {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a Julius instance using the default data file path.
     */
    public Julius() {
        ui = new Ui();
        storage = new Storage();
        tasks = new TaskList(storage.load());
    }

    /**
     * Creates a Julius instance that persists tasks to the given file path.
     *
     * @param filePath path to the data file (created automatically if absent)
     */
    public Julius(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Starts the main command loop.
     * Reads commands from the user, parses them, and executes them until
     * an {@link julius.command.ExitCommand} is received.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showDivider();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (JuliusException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showDivider();
            }
        }
    }

    /**
     * Application entry point.
     * Accepts an optional command-line argument specifying a custom data file path.
     *
     * @param args optional; {@code args[0]} is used as the data file path when provided
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            new Julius(args[0]).run();
        } else {
            new Julius().run();
        }
    }
}
