/**
 * A task that must be finished by a stated time, such as
 * {@code deadline return book /by Sunday}.
 */
public class Deadline extends Task {

    /**
     * When the task is due, kept as free text. Level-4 deliberately does not parse this
     * into a date, so inputs like {@code /by no idea :-p} are still accepted.
     */
    private final String by;

    /**
     * Creates a deadline that starts out not done.
     *
     * @param description what the user wants to do
     * @param by when it is due, exactly as the user typed it
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
