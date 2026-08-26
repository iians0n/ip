package seedu.goat.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the tasks the user is tracking, in the order they were added.
 * <p>
 * This class deliberately does no printing and no saving. It answers questions about the
 * tasks and changes them, leaving the caller to decide what to tell the user and when to
 * write to disk, which is what makes it straightforward to test on its own.
 */
public class TaskList {

    /** The tasks, in list order. Positions shown to the user are these indices plus one. */
    private final ArrayList<Task> tasks;

    /** Creates an empty list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a list holding the given tasks.
     * <p>
     * The tasks are copied, so later changes to this list do not disturb the caller's.
     *
     * @param tasks the tasks to start with, in order
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the task at a position and returns it.
     *
     * @param index zero-based position in the list
     * @return the task that was removed
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at a position without removing it.
     *
     * @param index zero-based position in the list
     * @return the task at that position
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns how many tasks are held.
     *
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether there are no tasks.
     *
     * @return true if the list is empty
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the tasks falling on a given date, in list order.
     * <p>
     * Each task decides for itself whether it matches, so this method needs no knowledge
     * of the different kinds of task.
     *
     * @param date the date being asked about
     * @return the matching tasks, empty if none match
     */
    public List<Task> findOn(LocalDate date) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the tasks whose description contains a keyword, in list order.
     * <p>
     * Each task decides for itself whether it matches, so this method needs no access to
     * the description and no knowledge of the different kinds of task.
     *
     * @param keyword the text to look for
     * @return the matching tasks, empty if none match
     */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.hasKeyword(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns a read-only view of the tasks, for callers that only need to walk them.
     *
     * @return the tasks in list order
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
