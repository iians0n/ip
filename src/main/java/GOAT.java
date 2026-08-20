import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for the GOAT chatbot.
 * <p>
 * GOAT reads commands from standard input one line at a time. {@code todo},
 * {@code deadline} and {@code event} add tasks, {@code mark} and {@code unmark}
 * change their status, {@code delete} removes one, {@code list} prints them and
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

    /** Command that marks a task as not done, used as {@code unmark N}. */
    private static final String UNMARK_COMMAND = "unmark";

    /** Command that adds a plain task, used as {@code todo DESCRIPTION}. */
    private static final String TODO_COMMAND = "todo";

    /** Command that adds a due task, used as {@code deadline DESCRIPTION /by WHEN}. */
    private static final String DEADLINE_COMMAND = "deadline";

    /** Command that adds a timed task, used as {@code event DESC /from START /to END}. */
    private static final String EVENT_COMMAND = "event";

    /** Command that removes a task, used as {@code delete N}. */
    private static final String DELETE_COMMAND = "delete";

    /** Commands listed back to the user when input is not understood. */
    private static final String KNOWN_COMMANDS =
            "todo, deadline, event, list, mark, unmark, delete, bye";

    /**
     * Stored tasks, in the order the user added them.
     * <p>
     * An {@link ArrayList} grows on demand, so it replaces both the fixed array and the
     * separate counter that tracked how much of that array was in use.
     */
    private static final ArrayList<Task> tasks = new ArrayList<>();

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

            try {
                if (command.equals(EXIT_COMMAND)) {
                    break;
                } else if (command.equals(LIST_COMMAND)) {
                    listTasks();
                } else if (command.equals(MARK_COMMAND)) {
                    markTask(parseTaskNumber(arguments, MARK_COMMAND));
                } else if (command.equals(UNMARK_COMMAND)) {
                    unmarkTask(parseTaskNumber(arguments, UNMARK_COMMAND));
                } else if (command.equals(DELETE_COMMAND)) {
                    deleteTask(parseTaskNumber(arguments, DELETE_COMMAND));
                } else if (command.equals(TODO_COMMAND)) {
                    addTask(parseTodo(arguments));
                } else if (command.equals(DEADLINE_COMMAND)) {
                    addTask(parseDeadline(arguments));
                } else if (command.equals(EVENT_COMMAND)) {
                    addTask(parseEvent(arguments));
                } else if (command.isEmpty()) {
                    throw new GOATException("I did not catch a command there. I know: "
                            + KNOWN_COMMANDS + ".");
                } else {
                    throw new GOATException("I do not know the command \"" + command
                            + "\". I know: " + KNOWN_COMMANDS + ".");
                }
            } catch (GOATException e) {
                // One catch for the whole loop: a rejected command reports itself and
                // GOAT carries on with the next line instead of terminating.
                respond(e.getMessage());
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
        tasks.add(task);
        respond("Got it. I've added this task:", "  " + task, taskCountSummary());
    }

    /**
     * Removes a task from the list and reports what was removed.
     *
     * @param taskNumber the position shown by {@code list}, counting from 1
     */
    private static void deleteTask(int taskNumber) {
        // remove() returns the removed element, so the confirmation can show the task
        // even though it is no longer in the list. Later tasks shift down by one, which
        // is why list renumbers them automatically.
        Task removed = tasks.remove(taskNumber - 1);
        respond("Noted. I've removed this task:", "  " + removed, taskCountSummary());
    }

    /**
     * Returns the sentence reporting how many tasks are now stored.
     *
     * @return text such as {@code Now you have 3 tasks in the list.}
     */
    private static String taskCountSummary() {
        int count = tasks.size();
        return "Now you have " + count + (count == 1 ? " task" : " tasks") + " in the list.";
    }

    /**
     * Reads the 1-based task number given to a command such as {@code mark}.
     *
     * @param arguments text following the command
     * @param commandName the command being run, used in the error messages
     * @return a task number that is known to be within range
     * @throws GOATException if the number is missing, not a number, or out of range
     */
    private static int parseTaskNumber(String arguments, String commandName)
            throws GOATException {
        if (arguments.isEmpty()) {
            throw new GOATException(commandName + " needs a task number, as in \""
                    + commandName + " 2\".");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new GOATException("\"" + arguments + "\" is not a number. Give me a task"
                    + " number instead, as in \"" + commandName + " 2\".");
        }

        if (tasks.isEmpty()) {
            throw new GOATException("Your list is empty, so there is nothing to "
                    + commandName + " yet.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new GOATException("There is no task " + taskNumber + ". Pick a number"
                    + " from 1 to " + tasks.size() + ".");
        }
        return taskNumber;
    }

    /**
     * Builds a to-do from the text following the {@code todo} command.
     *
     * @param arguments the description
     * @return the parsed to-do
     * @throws GOATException if the description is missing
     */
    private static Todo parseTodo(String arguments) throws GOATException {
        if (arguments.isEmpty()) {
            throw new GOATException("A todo needs a description, as in"
                    + " \"todo borrow book\".");
        }
        return new Todo(arguments);
    }

    /**
     * Builds a deadline from the text following the {@code deadline} command.
     *
     * @param arguments text of the form {@code DESCRIPTION /by WHEN}
     * @return the parsed deadline
     * @throws GOATException if the description, the {@code /by}, or the time is missing
     */
    private static Deadline parseDeadline(String arguments) throws GOATException {
        String example = "\"deadline return book /by Sunday\"";
        int byIndex = arguments.indexOf("/by");
        if (byIndex < 0) {
            throw new GOATException("A deadline needs a /by to say when it is due, as in "
                    + example + ".");
        }

        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new GOATException("A deadline needs a description before the /by, as in "
                    + example + ".");
        }
        if (by.isEmpty()) {
            throw new GOATException("The /by is empty. Tell me when it is due, as in "
                    + example + ".");
        }
        return new Deadline(description, by);
    }

    /**
     * Builds an event from the text following the {@code event} command.
     *
     * @param arguments text of the form {@code DESCRIPTION /from START /to END}
     * @return the parsed event
     * @throws GOATException if the description, {@code /from} or {@code /to} is missing
     */
    private static Event parseEvent(String arguments) throws GOATException {
        String example = "\"event project meeting /from Mon 2pm /to 4pm\"";
        int fromIndex = arguments.indexOf("/from");
        if (fromIndex < 0) {
            throw new GOATException("An event needs a /from to say when it starts, as in "
                    + example + ".");
        }

        // Search after /from so that a /to written before it is not mistaken for the end.
        int toIndex = arguments.indexOf("/to", fromIndex + "/from".length());
        if (toIndex < 0) {
            throw new GOATException("An event needs a /to after the /from, as in "
                    + example + ".");
        }

        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = arguments.substring(toIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new GOATException("An event needs a description before the /from, as in "
                    + example + ".");
        }
        if (from.isEmpty()) {
            throw new GOATException("The /from is empty. Tell me when it starts, as in "
                    + example + ".");
        }
        if (to.isEmpty()) {
            throw new GOATException("The /to is empty. Tell me when it ends, as in "
                    + example + ".");
        }
        return new Event(description, from, to);
    }

    /**
     * Marks a task as done and echoes it back.
     *
     * @param taskNumber the position shown by {@code list}, counting from 1
     */
    private static void markTask(int taskNumber) {
        int index = taskNumber - 1;
        Task task = tasks.get(index);
        task.markAsDone();
        respond("Nice! I've marked this task as done:", "  " + task);
    }

    /**
     * Marks a task as not done and echoes it back.
     *
     * @param taskNumber the position shown by {@code list}, counting from 1
     */
    private static void unmarkTask(int taskNumber) {
        int index = taskNumber - 1;
        Task task = tasks.get(index);
        task.markAsNotDone();
        respond("OK, I've marked this task as not done yet:", "  " + task);
    }

    /** Prints every stored task, numbered from 1, with its completion status. */
    private static void listTasks() {
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasks.get(i);
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
