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

    /**
     * Reads the saved task list from disk.
     * <p>
     * A missing file is not an error: it simply means this is the first run, so an empty
     * list is returned and the file appears the first time a task is added.
     *
     * @return the saved tasks in file order, or an empty list if there is no save file
     * @throws GOATException if the file exists but cannot be read
     */
    public ArrayList<Task> load() throws GOATException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        try {
            for (String line : Files.readAllLines(filePath)) {
                tasks.add(parseLine(line));
            }
        } catch (IOException e) {
            throw new GOATException("I could not read your saved tasks from " + filePath
                    + ". Starting with an empty list.");
        }
        return tasks;
    }

    /**
     * Rebuilds one task from its encoded line.
     *
     * @param line a line produced by {@link Task#toFileString()}
     * @return the decoded task
     */
    private static Task parseLine(String line) {
        // The separator is escaped because split() takes a regular expression, in which
        // a bare | means alternation rather than a literal bar.
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task = switch (type) {
            case "T" -> new Todo(description);
            case "D" -> new Deadline(description, parts[3]);
            default -> new Event(description, parts[3], parts[4]);
        };

        if (isDone) {
            task.markAsDone();
        }
        return task;
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
