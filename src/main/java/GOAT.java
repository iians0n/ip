import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for the GOAT chatbot.
 * <p>
 * GOAT reads commands from standard input one line at a time. {@code todo},
 * {@code deadline} and {@code event} add tasks, {@code mark} and {@code unmark}
 * change their status, {@code delete} removes one, {@code list} prints them and
 * {@code bye} ends the conversation.
 */
public class GOAT {

    /** Handles all reading from and writing to the user. */
    private static final Ui ui = new Ui();

    /** Where the task list is saved between runs, relative to the working directory. */
    private static final String SAVE_FILE_PATH = "data/goat.txt";

    /** Handles reading and writing {@link #SAVE_FILE_PATH}. */
    private static final Storage storage = new Storage(SAVE_FILE_PATH);

    /** The tasks the user is tracking. Replaced wholesale when a save file is loaded. */
    private static TaskList tasks = new TaskList();

    public static void main(String[] args) {
        ui.showWelcome();
        loadTasks();

        boolean isRunning = true;
        while (isRunning && ui.hasNextCommand()) {
            String input = ui.readCommand();
            String[] parts = input.split(" ", 2);
            String keyword = parts[0];
            String arguments = parts.length > 1 ? parts[1].trim() : "";

            try {
                Command command = Command.fromKeyword(keyword);
                switch (command) {
                    case BYE -> isRunning = false;
                    case LIST -> listTasks();
                    case MARK -> markTask(parseTaskNumber(arguments, command));
                    case UNMARK -> unmarkTask(parseTaskNumber(arguments, command));
                    case DELETE -> deleteTask(parseTaskNumber(arguments, command));
                    case ON -> listTasksOn(arguments);
                    case TODO -> addTask(parseTodo(arguments));
                    case DEADLINE -> addTask(parseDeadline(arguments));
                    case EVENT -> addTask(parseEvent(arguments));
                }
            } catch (GOATException e) {
                // One catch for the whole loop: a rejected command reports itself and
                // GOAT carries on with the next line instead of terminating.
                ui.showError(e.getMessage());
            }
        }

        ui.showGoodbye();
    }

    /**
     * Restores any previously saved tasks into the in-memory list.
     * <p>
     * A failure here is reported and then ignored, so a bad save file leaves GOAT usable
     * with an empty list rather than preventing it from starting at all.
     */
    private static void loadTasks() {
        try {
            List<Task> saved = storage.load();
            tasks = new TaskList(saved);

            int skipped = storage.getSkippedLineCount();
            if (skipped > 0) {
                ui.show("Some of your save file was unreadable, so I skipped " + skipped
                                + (skipped == 1 ? " line." : " lines."),
                        "The " + saved.size() + " tasks I could read are in your list.");
            } else if (!saved.isEmpty()) {
                ui.show("Welcome back. I restored " + saved.size()
                        + (saved.size() == 1 ? " task" : " tasks") + " from your last session.");
            }
        } catch (GOATException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Stores a new task and confirms it, along with the new task count.
     *
     * @param task the task to store
     * @throws GOATException if the updated list cannot be saved
     */
    private static void addTask(Task task) throws GOATException {
        tasks.add(task);
        storage.save(tasks.asList());
        ui.show("Got it. I've added this task:", "  " + task, taskCountSummary());
    }

    /**
     * Removes a task from the list and reports what was removed.
     *
     * @param taskNumber the position shown by {@code list}, counting from 1
     * @throws GOATException if the updated list cannot be saved
     */
    private static void deleteTask(int taskNumber) throws GOATException {
        // remove() returns the removed element, so the confirmation can show the task
        // even though it is no longer in the list. Later tasks shift down by one, which
        // is why list renumbers them automatically.
        Task removed = tasks.delete(taskNumber - 1);
        storage.save(tasks.asList());
        ui.show("Noted. I've removed this task:", "  " + removed, taskCountSummary());
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
     * @param command the command being run, named in the error messages
     * @return a task number that is known to be within range
     * @throws GOATException if the number is missing, not a number, or out of range
     */
    private static int parseTaskNumber(String arguments, Command command)
            throws GOATException {
        String name = command.keyword();
        if (arguments.isEmpty()) {
            throw new GOATException(name + " needs a task number, as in \""
                    + name + " 2\".");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new GOATException("\"" + arguments + "\" is not a number. Give me a task"
                    + " number instead, as in \"" + name + " 2\".");
        }

        if (tasks.isEmpty()) {
            throw new GOATException("Your list is empty, so there is nothing to "
                    + name + " yet.");
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
        String example = "\"deadline return book /by 2019-12-02\"";
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
        return new Deadline(description, DateFormats.parse(by));
    }

    /**
     * Builds an event from the text following the {@code event} command.
     *
     * @param arguments text of the form {@code DESCRIPTION /from START /to END}
     * @return the parsed event
     * @throws GOATException if the description, {@code /from} or {@code /to} is missing
     */
    private static Event parseEvent(String arguments) throws GOATException {
        String example = "\"event project meeting /from 2019-10-15 /to 2019-10-16\"";
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

        LocalDate start = DateFormats.parse(from);
        LocalDate end = DateFormats.parse(to);
        if (end.isBefore(start)) {
            throw new GOATException("An event cannot end before it starts."
                    + " " + DateFormats.format(end) + " is earlier than "
                    + DateFormats.format(start) + ".");
        }
        return new Event(description, start, end);
    }

    /**
     * Marks a task as done and echoes it back.
     *
     * @param taskNumber the position shown by {@code list}, counting from 1
     * @throws GOATException if the updated list cannot be saved
     */
    private static void markTask(int taskNumber) throws GOATException {
        int index = taskNumber - 1;
        Task task = tasks.get(index);
        task.markAsDone();
        storage.save(tasks.asList());
        ui.show("Nice! I've marked this task as done:", "  " + task);
    }

    /**
     * Marks a task as not done and echoes it back.
     *
     * @param taskNumber the position shown by {@code list}, counting from 1
     * @throws GOATException if the updated list cannot be saved
     */
    private static void unmarkTask(int taskNumber) throws GOATException {
        int index = taskNumber - 1;
        Task task = tasks.get(index);
        task.markAsNotDone();
        storage.save(tasks.asList());
        ui.show("OK, I've marked this task as not done yet:", "  " + task);
    }

    /**
     * Prints the dated tasks that fall on a given date.
     * <p>
     * A deadline matches when it is due that day; an event matches when the date lies
     * anywhere in its span. To-dos carry no date and so never match.
     *
     * @param arguments the date to query, in {@code yyyy-mm-dd} form
     * @throws GOATException if the date is missing or cannot be parsed
     */
    private static void listTasksOn(String arguments) throws GOATException {
        if (arguments.isEmpty()) {
            throw new GOATException("on needs a date, as in \"on 2019-10-15\".");
        }
        LocalDate date = DateFormats.parse(arguments);

        List<Task> matches = tasks.findOn(date);
        List<String> lines = new ArrayList<>();
        for (Task task : matches) {
            lines.add((lines.size() + 1) + "." + task);
        }

        if (lines.isEmpty()) {
            ui.show("Nothing is scheduled on " + DateFormats.format(date) + ".");
            return;
        }
        lines.add(0, "Here is what you have on " + DateFormats.format(date) + ":");
        ui.show(lines.toArray(new String[0]));
    }

    /** Prints every stored task, numbered from 1, with its completion status. */
    private static void listTasks() {
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasks.get(i);
        }
        ui.show(lines);
    }
}
