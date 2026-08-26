package seedu.goat.ui;

import java.util.Scanner;

/**
 * Handles everything the user sees and types.
 * <p>
 * Collecting the input stream and the output formatting here means the rest of GOAT
 * never calls {@code System.out} directly, so the wording, the indentation and the
 * horizontal rules can all change without touching the command logic.
 */
public class Ui {

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

    /** Reads the user's commands from standard input. */
    private final Scanner scanner;

    /** Creates a user interface reading from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Prints the banner and the opening message shown when the program starts. */
    public void showWelcome() {
        System.out.println(BANNER);
        show("Hello! I'm " + NAME, "What can I do for you?");
    }

    /** Prints the closing message shown just before the program ends. */
    public void showGoodbye() {
        show("Bye. Hope to see you again soon!");
    }

    /**
     * Returns whether there is another command waiting to be read.
     * <p>
     * False at end of input, so piped input and Ctrl-D end the conversation cleanly
     * rather than looping forever.
     *
     * @return true if another line can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command, with surrounding whitespace removed.
     *
     * @return the line the user typed, trimmed
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints one reply, wrapped in horizontal rules and indented.
     *
     * @param messages lines of the reply, printed in order
     */
    public void show(String... messages) {
        System.out.println(LINE);
        for (String message : messages) {
            System.out.println(" " + message);
        }
        System.out.println(LINE);
    }

    /**
     * Reports something that went wrong, in the same shape as any other reply.
     *
     * @param message what went wrong, phrased as advice
     */
    public void showError(String message) {
        show(message);
    }
}
