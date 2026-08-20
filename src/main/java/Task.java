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

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}
