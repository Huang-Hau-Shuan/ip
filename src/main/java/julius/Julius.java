package julius;

import julius.command.Command;
import julius.exception.JuliusException;
import julius.parser.Parser;
import julius.storage.Storage;
import julius.task.TaskList;
import julius.ui.Ui;

public class Julius {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Julius() {
        ui = new Ui();
        storage = new Storage();
        tasks = new TaskList(storage.load());
    }

    public Julius(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

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

    public static void main(String[] args) {
        if (args.length > 0) {
            new Julius(args[0]).run();
        } else {
            new Julius().run();
        }
    }
}
