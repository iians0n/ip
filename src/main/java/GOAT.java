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

    /** Where the task list is saved when no other path is given. */
    private static final String DEFAULT_SAVE_PATH = "data/goat.txt";

    /** Handles all reading from and writing to the user. */
    private final Ui ui;

    /** Handles reading and writing the save file. */
    private final Storage storage;

    /** The tasks the user is tracking. */
    private TaskList tasks;

    /**
     * Message describing why loading failed, or null if it did not.
     * <p>
     * Held rather than printed immediately so that the greeting still comes first: being
     * shown an error before GOAT has said hello reads badly.
     */
    private String loadingError;

    /**
     * Creates a chatbot backed by the given save file.
     * <p>
     * A save file that cannot be read leaves GOAT running with an empty list rather than
     * refusing to start, because an unusable chatbot is a worse outcome than a lost list.
     *
     * @param filePath relative path to the save file
     */
    public GOAT(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            this.tasks = new TaskList(storage.load());
        } catch (GOATException e) {
            this.loadingError = e.getMessage();
            this.tasks = new TaskList();
        }
    }

    /** Greets the user, then handles commands until the conversation ends. */
    public void run() {
        ui.showWelcome();
        reportLoadOutcome();

        boolean isRunning = true;
        while (isRunning && ui.hasNextCommand()) {
            String input = ui.readCommand();

            try {
                Parser.ParsedCommand parsed = Parser.parse(input);
                Command command = parsed.command();
                String arguments = parsed.arguments();

                switch (command) {
                    case BYE -> isRunning = false;
                    case LIST -> listTasks();
                    case MARK -> markTask(Parser.parseTaskNumber(arguments, command));
                    case UNMARK -> unmarkTask(Parser.parseTaskNumber(arguments, command));
                    case DELETE -> deleteTask(Parser.parseTaskNumber(arguments, command));
                    case ON -> listTasksOn(Parser.parseDate(arguments));
                    case TODO -> addTask(Parser.parseTodo(arguments));
                    case DEADLINE -> addTask(Parser.parseDeadline(arguments));
                    case EVENT -> addTask(Parser.parseEvent(arguments));
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
     * Starts GOAT with the default save file.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new GOAT(DEFAULT_SAVE_PATH).run();
    }

    /** Tells the user what happened when the save file was read, if anything notable. */
    private void reportLoadOutcome() {
        if (loadingError != null) {
            ui.showError(loadingError);
            return;
        }

        int skipped = storage.getSkippedLineCount();
        if (skipped > 0) {
            ui.show("Some of your save file was unreadable, so I skipped " + skipped
                            + (skipped == 1 ? " line." : " lines."),
                    "The " + tasks.size() + " tasks I could read are in your list.");
        } else if (!tasks.isEmpty()) {
            ui.show("Welcome back. I restored " + tasks.size()
                    + (tasks.size() == 1 ? " task" : " tasks") + " from your last session.");
        }
    }

    /**
     * Stores a new task and confirms it, along with the new task count.
     *
     * @param task the task to store
     * @throws GOATException if the updated list cannot be saved
     */
    private void addTask(Task task) throws GOATException {
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
    private void deleteTask(int taskNumber) throws GOATException {
        // remove() returns the removed element, so the confirmation can show the task
        // even though it is no longer in the list. Later tasks shift down by one, which
        // is why list renumbers them automatically.
        Task removed = tasks.delete(toIndex(taskNumber, Command.DELETE));
        storage.save(tasks.asList());
        ui.show("Noted. I've removed this task:", "  " + removed, taskCountSummary());
    }

    /**
     * Returns the sentence reporting how many tasks are now stored.
     *
     * @return text such as {@code Now you have 3 tasks in the list.}
     */
    private String taskCountSummary() {
        int count = tasks.size();
        return "Now you have " + count + (count == 1 ? " task" : " tasks") + " in the list.";
    }

    /**
     * Converts a task number the user typed into a position in the list.
     * <p>
     * The number itself was already checked by {@link Parser}; what is checked here is
     * whether it refers to a task that actually exists, which only the list can say.
     *
     * @param taskNumber the position the user typed, counting from 1
     * @param command the command being run, named in the error messages
     * @return the matching zero-based index
     * @throws GOATException if the list is empty or the number is out of range
     */
    private int toIndex(int taskNumber, Command command) throws GOATException {
        if (tasks.isEmpty()) {
            throw new GOATException("Your list is empty, so there is nothing to "
                    + command.keyword() + " yet.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new GOATException("There is no task " + taskNumber + ". Pick a number"
                    + " from 1 to " + tasks.size() + ".");
        }
        return taskNumber - 1;
    }

    /**
     * Marks a task as done and echoes it back.
     *
     * @param taskNumber the position shown by {@code list}, counting from 1
     * @throws GOATException if the updated list cannot be saved
     */
    private void markTask(int taskNumber) throws GOATException {
        int index = toIndex(taskNumber, Command.MARK);
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
    private void unmarkTask(int taskNumber) throws GOATException {
        int index = toIndex(taskNumber, Command.UNMARK);
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
     * @param date the date to query
     */
    private void listTasksOn(LocalDate date) {
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
    private void listTasks() {
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasks.get(i);
        }
        ui.show(lines);
    }
}
