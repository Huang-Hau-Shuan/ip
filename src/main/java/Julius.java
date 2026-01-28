import java.util.Scanner;

public class Julius {
    public static void main(String[] args) {
        String divider = "____________________________________________________________";
        String logo = "      _ _    _ _      _____ _    _  _____\n"
                + "     | | |  | | |    |_   _| |  | |/ ____|\n"
                + "     | | |  | | |      | | | |  | | (___  \n"
                + " _   | | |  | | |      | | | |  | |\\___ \\ \n"
                + "| |__| | |__| | |____ _| |_| |__| |____) |\n"
                + " \\____/ \\____/|______|_____|\\____/|_____/ \n";

        System.out.println("Hello from\n" + logo);
        String name = "Julius";

        // Greet the user
        System.out.println(divider);
        System.out.println(" Hello! I'm " + name);
        System.out.println(" What can I do for you?");
        System.out.println(divider);

        Scanner in = new Scanner(System.in);

        while (true) {
            String input = in.nextLine().trim();
            String processedInput = input.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");

            if (processedInput.contains("bye")) {
                break; // Exit the loop if the user types "bye"
            }

            System.out.println(divider);
            System.out.println("    " + input); // Echo the input
            System.out.println(divider);
        }

        // Exit message
        System.out.println(divider);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(divider);
    }
}
