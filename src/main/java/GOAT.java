import java.util.Scanner;

/**
 * Entry point for the GOAT chatbot.
 * <p>
 * GOAT reads commands from standard input one line at a time. {@code todo},
 * {@code deadline} and {@code event} add tasks, {@code mark} and {@code unmark}
 * change their status, {@code list} prints them and {@code bye} ends the conversation.
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

    /** Command that marks a task as not done, used as {@code unmark N}. */
    private static final String UNMARK_COMMAND = "unmark";

    /** Command that adds a plain task, used as {@code todo DESCRIPTION}. */
    private static final String TODO_COMMAND = "todo";

    /** Command that adds a due task, used as {@code deadline DESCRIPTION /by WHEN}. */
    private static final String DEADLINE_COMMAND = "deadline";

    /** Command that adds a timed task, used as {@code event DESC /from START /to END}. */
    private static final String EVENT_COMMAND = "event";

    /** Upper bound on stored tasks; the fixed array is replaced by a list in Level-6. */
    private static final int MAX_TASKS = 100;

    /** Stored tasks, filled from index 0 upwards. */
    private static final Task[] tasks = new Task[MAX_TASKS];

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
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String arguments = parts.length > 1 ? parts[1].trim() : "";

            if (command.equals(EXIT_COMMAND)) {
                break;
            } else if (command.equals(LIST_COMMAND)) {
                listTasks();
            } else if (command.equals(MARK_COMMAND)) {
                markTask(Integer.parseInt(arguments));
            } else if (command.equals(UNMARK_COMMAND)) {
                unmarkTask(Integer.parseInt(arguments));
            } else if (command.equals(TODO_COMMAND)) {
                addTask(new Todo(arguments));
            } else if (command.equals(DEADLINE_COMMAND)) {
                addTask(parseDeadline(arguments));
            } else if (command.equals(EVENT_COMMAND)) {
                addTask(parseEvent(arguments));
            } else {
                respond("Sorry, I don't recognise the command \"" + command + "\".");
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
     * Stores a new task and confirms it, along with the new task count.
     *
     * @param task the task to store
     */
    private static void addTask(Task task) {
        tasks[taskCount] = task;
        taskCount++;
        respond("Got it. I've added this task:",
                "  " + task,
                "Now you have " + taskCount + (taskCount == 1 ? " task" : " tasks")
                        + " in the list.");
    }

    /**
     * Builds a deadline from the text following the {@code deadline} command.
     *
     * @param arguments text of the form {@code DESCRIPTION /by WHEN}
     * @return the parsed deadline
     */
    private static Deadline parseDeadline(String arguments) {
        int byIndex = arguments.indexOf("/by");
        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + "/by".length()).trim();
        return new Deadline(description, by);
    }

    /**
     * Builds an event from the text following the {@code event} command.
     *
     * @param arguments text of the form {@code DESCRIPTION /from START /to END}
     * @return the parsed event
     */
    private static Event parseEvent(String arguments) {
        int fromIndex = arguments.indexOf("/from");
        int toIndex = arguments.indexOf("/to", fromIndex + 1);
        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = arguments.substring(toIndex + "/to".length()).trim();
        return new Event(description, from, to);
    }

    /**
     * Marks a task as done and echoes it back.
     *
     * @param taskNumber the position shown by {@code list}, counting from 1
     */
    private static void markTask(int taskNumber) {
        int index = taskNumber - 1;
        tasks[index].markAsDone();
        respond("Nice! I've marked this task as done:", "  " + tasks[index]);
    }

    /**
     * Marks a task as not done and echoes it back.
     *
     * @param taskNumber the position shown by {@code list}, counting from 1
     */
    private static void unmarkTask(int taskNumber) {
        int index = taskNumber - 1;
        tasks[index].markAsNotDone();
        respond("OK, I've marked this task as not done yet:", "  " + tasks[index]);
    }

    /** Prints every stored task, numbered from 1, with its completion status. */
    private static void listTasks() {
        String[] lines = new String[taskCount + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < taskCount; i++) {
            lines[i + 1] = (i + 1) + "." + tasks[i];
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
