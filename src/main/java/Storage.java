import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes the task list to a text file on disk.
 * <p>
 * The path is supplied as a relative path string and turned into a {@link Path}, so the
 * separator is whatever the host operating system uses and no separator is hardcoded.
 */
public class Storage {

    /** Where the task list is kept between runs. */
    private final Path filePath;

    /**
     * Creates a store backed by the given file.
     *
     * @param filePath relative path to the save file, such as {@code data/goat.txt}
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /** Number of unreadable lines skipped by the most recent {@link #load()}. */
    private int skippedLineCount = 0;

    /**
     * Reads the saved task list from disk.
     * <p>
     * A missing file is not an error: it simply means this is the first run, so an empty
     * list is returned and the file appears the first time a task is added. Individual
     * lines that cannot be decoded are skipped rather than aborting the whole load, so
     * one damaged line cannot cost the user the rest of their list.
     *
     * @return the saved tasks in file order, or an empty list if there is no save file
     * @throws GOATException if the file exists but cannot be read at all
     */
    public ArrayList<Task> load() throws GOATException {
        ArrayList<Task> tasks = new ArrayList<>();
        skippedLineCount = 0;
        if (!Files.exists(filePath)) {
            return tasks;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new GOATException("I could not read your saved tasks from " + filePath
                    + ", so I am starting with an empty list.");
        }

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(parseLine(line));
            } catch (GOATException e) {
                skippedLineCount++;
            }
        }
        return tasks;
    }

    /**
     * Returns how many unreadable lines the most recent load skipped.
     *
     * @return the number of skipped lines, or zero if the file was wholly readable
     */
    public int getSkippedLineCount() {
        return skippedLineCount;
    }

    /**
     * Rebuilds one task from its encoded line.
     *
     * @param line a line produced by {@link Task#toFileString()}
     * @return the decoded task
     * @throws GOATException if the line is malformed in any way
     */
    private static Task parseLine(String line) throws GOATException {
        // The separator is escaped because split() takes a regular expression, in which
        // a bare | means alternation rather than a literal bar.
        String[] parts = line.split(" \\| ");
        requireFieldCount(parts, 3);

        String type = parts[0];
        String status = parts[1];
        String description = parts[2];
        if (!status.equals("0") && !status.equals("1")) {
            throw new GOATException("status must be 0 or 1");
        }
        if (description.isBlank()) {
            throw new GOATException("description is empty");
        }

        Task task = switch (type) {
            case "T" -> new Todo(description);
            case "D" -> {
                requireFieldCount(parts, 4);
                yield new Deadline(description, parts[3]);
            }
            case "E" -> {
                requireFieldCount(parts, 5);
                yield new Event(description, parts[3], parts[4]);
            }
            // Without this the decoder would guess, and an unrecognised letter would be
            // silently turned into some other kind of task.
            default -> throw new GOATException("unknown task type " + type);
        };

        if (status.equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Checks that an encoded line carried enough fields to decode.
     *
     * @param parts the fields split out of the line
     * @param required how many fields this kind of task needs
     * @throws GOATException if there are too few fields, or any required one is blank
     */
    private static void requireFieldCount(String[] parts, int required)
            throws GOATException {
        if (parts.length < required) {
            throw new GOATException("expected " + required + " fields, found "
                    + parts.length);
        }
        for (int i = 0; i < required; i++) {
            if (parts[i].isBlank()) {
                throw new GOATException("field " + (i + 1) + " is empty");
            }
        }
    }

    /**
     * Writes the whole task list to disk, replacing whatever was there before.
     * <p>
     * Rewriting the entire file on every change is far simpler than editing a line in
     * place, and the file is small enough that the cost does not matter.
     *
     * @param tasks the tasks to save, in list order
     * @throws GOATException if the file cannot be written
     */
    public void save(List<Task> tasks) throws GOATException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                // Creates every missing directory in the path, and does nothing if they
                // all already exist, so first run and later runs take the same route.
                Files.createDirectories(parent);
            }

            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toFileString());
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new GOATException("I could not save your tasks to " + filePath
                    + ". Your list is still correct in this session.");
        }
    }
}
