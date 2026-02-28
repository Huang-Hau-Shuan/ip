package julius.ui;

/**
 * Deals with interactions with the user.
 * Handles all output printed to the console.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BOT_NAME = "Julius";

    /** Prints the opening divider, greeting, and ASCII logo. */
    public void showWelcome() {
        String logo = "      _ _    _ _      _____ _    _  _____\n"
                + "     | | |  | | |    |_   _| |  | |/ ____|\n"
                + "     | | |  | | |      | | | |  | | (___\n"
                + " _   | | |  | | |      | | | |  | |\\___ \\\n"
                + "| |__| | |__| | |____ _| |_| |__| |____) |\n"
                + " \\____/ \\____/|______|_____|\\____/|_____/\n";

        System.out.println("Hello from\n" + logo);
        showDivider();
        System.out.println(" Hello! I'm " + BOT_NAME);
        System.out.println(" What can I do for you?");
        showDivider();
    }

    /** Prints the goodbye message. */
    public void showGoodbye() {
        showDivider();
        System.out.println(" Bye. Hope to see you again soon!");
        showDivider();
    }

    /** Prints the horizontal divider line. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }
}
