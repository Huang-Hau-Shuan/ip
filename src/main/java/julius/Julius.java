package julius;

import julius.exception.JuliusException;
import julius.parser.Parser;
import julius.storage.Storage;
import julius.task.TaskList;
import julius.ui.Ui;

import java.util.Scanner;

public class Julius {
    private static final Storage storage = new Storage();
    private static final TaskList tasks = new TaskList(storage.load());
    private static final Ui ui = new Ui();
    private static final Parser parser = new Parser();

    public static void main(String[] args) {
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        runCommandLoop(scanner);
        ui.showGoodbye();
    }

    private static void runCommandLoop(Scanner scanner) {
        while (true) {
            String userInput = scanner.nextLine().trim();

            if (parser.isExit(userInput)) {
                break;
            }

            ui.showDivider();
            try {
                parser.parse(userInput, tasks, ui);
                storage.save(tasks.getAll());
            } catch (JuliusException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("An error occurred: " + e.getMessage());
            }
            ui.showDivider();
        }
    }
}
