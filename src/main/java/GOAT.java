import java.util.Scanner;

/**
 * Entry point for the GOAT chatbot.
 * <p>
 * GOAT reads commands from standard input one line at a time and echoes them back,
 * stopping when the user types {@code bye}.
 */
public class GOAT {

    /** Horizontal rule printed around each block of output. */
    private static final String LINE =
            "____________________________________________________________";

    /** The bot's name, kept in one place so every message stays consistent. */
    private static final String NAME = "GOAT";

    /** ASCII-art banner shown once at startup. */
    private static final String BANNER = """
  ____   ___      _     _____
 / ___| / _ \\    / \\   |_   _|
| |  _ | | | |  / _ \\    | |
| |_| || |_| | / ___ \\   | |
 \\____| \\___/ /_/   \\_\\  |_|
""";

    /** Command that ends the conversation. */
    private static final String EXIT_COMMAND = "bye";

    public static void main(String[] args) {
        System.out.println(BANNER);
        greet();

        Scanner scanner = new Scanner(System.in);
        // hasNextLine() is false at end of input, so piped input and Ctrl-D exit cleanly
        // instead of looping forever.
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.equals(EXIT_COMMAND)) {
                break;
            }
            respond(input);
        }

        farewell();
    }

    /**
     * Prints one reply, wrapped in horizontal rules and indented.
     *
     * @param messages lines of the reply, printed in order
     */
    private static void respond(String... messages) {
        System.out.println(LINE);
        for (String message : messages) {
            System.out.println(" " + message);
        }
        System.out.println(LINE);
    }

    /** Prints the opening message shown when the program starts. */
    private static void greet() {
        respond("Hello! I'm " + NAME, "What can I do for you?");
    }

    /** Prints the closing message shown just before the program ends. */
    private static void farewell() {
        respond("Bye. Hope to see you again soon!");
    }
}
