/**
 * A task that spans a period of time, such as
 * {@code event project meeting /from Mon 2pm /to 4pm}.
 */
public class Event extends Task {

    /** When the event starts, kept as free text for the same reason as in Deadline. */
    private final String from;

    /** When the event ends, kept as free text. */
    private final String to;

    /**
     * Creates an event that starts out not done.
     *
     * @param description what the event is
     * @param from when it starts, exactly as the user typed it
     * @param to when it ends, exactly as the user typed it
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
