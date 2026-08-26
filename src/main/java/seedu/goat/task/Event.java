package seedu.goat.task;

import java.time.LocalDate;

import seedu.goat.DateFormats;

/**
 * A task that spans a period of time, such as
 * {@code event project meeting /from 2019-10-15 /to 2019-10-16}.
 */
public class Event extends Task {

    /** The date the event starts. */
    private final LocalDate from;

    /** The date the event ends. */
    private final LocalDate to;

    /**
     * Creates an event that starts out not done.
     *
     * @param description what the event is
     * @param from the date it starts
     * @param to the date it ends
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the date this event starts.
     *
     * @return the start date
     */
    public LocalDate getFrom() {
        return from;
    }

    /**
     * Returns the date this event ends.
     *
     * @return the end date
     */
    public LocalDate getTo() {
        return to;
    }

    /**
     * Returns whether this event is running on a given date.
     *
     * @param date the date to test
     * @return true if the date falls within the event, endpoints included
     */
    @Override
    public boolean isOn(LocalDate date) {
        return !date.isBefore(from) && !date.isAfter(to);
    }

    @Override
    public String toFileString() {
        return "E | " + getFileStatus() + " | " + description + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + DateFormats.format(from)
                + " to: " + DateFormats.format(to) + ")";
    }
}
