import java.util.Scanner;

public class Julius {
    public static void main(String[] args) {
        String logo = "      _ _    _ _      _____ _    _  _____\n"
                + "     | | |  | | |    |_   _| |  | |/ ____|\n"
                + "     | | |  | | |      | | | |  | | (___  \n"
                + " _   | | |  | | |      | | | |  | |\\___ \\ \n"
                + "| |__| | |__| | |____ _| |_| |__| |____) |\n"
                + " \\____/ \\____/|______|_____|\\____/|_____/ \n";

        System.out.println("Hello from\n" + logo);
        String name = "Julius";

        // Greet the user
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm " + name);
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        Scanner in = new Scanner(System.in);
        String line;

        while (true) {
            line = in.nextLine();
            if (line.equals("bye")) {
                break;
            }
            System.out.println(line); // Echo the input
        }

        // Exit message
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }
}
