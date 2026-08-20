import java.util.Scanner;

/**
 * Entry point for the GOAT chatbot.
 * <p>
 * GOAT reads commands from standard input one line at a time. Any line that is not a
 * recognised command is stored as a task; {@code list} prints the stored tasks and
 * {@code bye} ends the conversation.
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

    /** Command that prints every stored task. */
    private static final String LIST_COMMAND = "list";

    /** Upper bound on stored tasks; the fixed array is replaced by a list in Level-6. */
    private static final int MAX_TASKS = 100;

    /** Task descriptions, filled from index 0 upwards. */
    private static final String[] tasks = new String[MAX_TASKS];

    /** Number of slots of {@link #tasks} currently in use. */
    private static int taskCount = 0;

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
            } else if (input.equals(LIST_COMMAND)) {
                listTasks();
            } else {
                addTask(input);
            }
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

    /**
     * Stores a new task and confirms it to the user.
     *
     * @param description the task text exactly as the user typed it
     */
    private static void addTask(String description) {
        tasks[taskCount] = description;
        taskCount++;
        respond("added: " + description);
    }

    /** Prints every stored task, numbered from 1. */
    private static void listTasks() {
        String[] lines = new String[taskCount];
        for (int i = 0; i < taskCount; i++) {
            lines[i] = (i + 1) + ". " + tasks[i];
        }
        respond(lines);
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
