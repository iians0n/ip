import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Converts dates between the three forms GOAT uses.
 * <p>
 * The user types and the save file stores the ISO form {@code yyyy-mm-dd}, while listings
 * show the friendlier {@code MMM dd yyyy}. Keeping the stored form and the displayed form
 * separate means the file stays machine-readable no matter how the display changes.
 */
public class DateFormats {

    /** How a date is shown to the user, for example {@code Oct 15 2019}. */
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    /** The date format GOAT accepts, quoted in error messages. */
    public static final String INPUT_FORMAT = "yyyy-mm-dd";

    /** Utility class: never instantiated. */
    private DateFormats() {
    }

    /**
     * Reads a date written in ISO form.
     * <p>
     * Used for both user input and save-file fields, so a date always round-trips
     * through exactly the same format it was written in.
     *
     * @param text the date as typed or as stored, such as {@code 2019-10-15}
     * @return the parsed date
     * @throws GOATException if the text is not a valid ISO date
     */
    public static LocalDate parse(String text) throws GOATException {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new GOATException("I could not read \"" + text + "\" as a date."
                    + " Please use " + INPUT_FORMAT + ", as in 2019-10-15.");
        }
    }

    /**
     * Renders a date for display.
     *
     * @param date the date to show
     * @return the date as {@code MMM dd yyyy}, such as {@code Oct 15 2019}
     */
    public static String format(LocalDate date) {
        return date.format(DISPLAY_FORMAT);
    }
}
