package seedu.goat.task;

import java.time.LocalDate;

/**
 * A single item on the user's task list.
 * <p>
 * A task pairs a description with whether it has been completed. Keeping both in one
 * object replaces the parallel arrays used before this increment, so a task's text and
 * its status can no longer drift apart.
 */
public abstract class Task {

    /**
     * What the user wants to do. Protected rather than private so that the task-type
     * subclasses added in Level-4 can reuse it when building their own display text.
     */
    protected final String description;

    /** Whether the user has marked this task as completed. */
    protected boolean isDone;

    /**
     * Creates a task that starts out not done.
     *
     * @param description what the user wants to do
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as not completed. */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the completion marker shown in listings.
     *
     * @return {@code [X]} if this task is done, {@code [ ]} otherwise
     */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Returns whether this task falls on a given date.
     * <p>
     * A plain task carries no date, so the base answer is no. Subclasses that do carry
     * dates override this, which keeps callers from having to test a task's type.
     *
     * @param date the date being asked about
     * @return true if this task falls on that date
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Returns the completion flag as it is written to the save file.
     *
     * @return {@code "1"} if this task is done, {@code "0"} otherwise
     */
    protected String getFileStatus() {
        return isDone ? "1" : "0";
    }

    /**
     * Returns this task encoded as a single line of the save file.
     * <p>
     * Declared here and implemented by each subclass so that Storage never has to test
     * a task's runtime type to work out which fields to write.
     *
     * @return the encoded task, such as {@code D | 0 | return book | Sunday}
     */
    public abstract String toFileString();

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}
