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

    /** Command that marks a task as done, used as {@code mark N}. */
    private static final String MARK_COMMAND = "mark";

    /** Upper bound on stored tasks; the fixed array is replaced by a list in Level-6. */
    private static final int MAX_TASKS = 100;

    /** Task descriptions, filled from index 0 upwards. */
    private static final String[] tasks = new String[MAX_TASKS];

    /** Completion flag for each task, parallel to {@link #tasks}. */
    private static final boolean[] isDone = new boolean[MAX_TASKS];

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
            } else if (input.startsWith(MARK_COMMAND + " ")) {
                String argument = input.substring(MARK_COMMAND.length() + 1).trim();
                markTask(Integer.parseInt(argument));
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

    /**
     * Marks a task as done and echoes it back.
     *
     * @param taskNumber the position shown by {@code list}, counting from 1
     */
    private static void markTask(int taskNumber) {
        int index = taskNumber - 1;
        isDone[index] = true;
        respond("Nice! I've marked this task as done:",
                "  " + statusIcon(index) + " " + tasks[index]);
    }

    /**
     * Returns the completion marker for a task.
     *
     * @param index zero-based position in {@link #tasks}
     * @return {@code [X]} if done, {@code [ ]} otherwise
     */
    private static String statusIcon(int index) {
        return isDone[index] ? "[X]" : "[ ]";
    }

    /** Prints every stored task, numbered from 1, with its completion status. */
    private static void listTasks() {
        String[] lines = new String[taskCount + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < taskCount; i++) {
            lines[i + 1] = (i + 1) + "." + statusIcon(i) + " " + tasks[i];
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
