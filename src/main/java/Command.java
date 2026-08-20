/**
 * The instructions GOAT understands, one constant per keyword the user can type.
 * <p>
 * Commands are a fixed, known set, which is exactly what an enum is for: the compiler now
 * knows every possible command, so a typo like {@code MRAK} fails to compile rather than
 * silently never matching the way a misspelled string literal would.
 */
public enum Command {
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    BYE("bye");

    /** The word the user types to invoke this command. */
    private final String keyword;

    Command(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns the word that invokes this command, for use in error messages.
     *
     * @return the keyword, such as {@code mark}
     */
    public String keyword() {
        return keyword;
    }

    /**
     * Finds the command matching a word typed by the user.
     *
     * @param keyword the first word of the user's input
     * @return the matching command
     * @throws GOATException if the word is empty or matches no command
     */
    public static Command fromKeyword(String keyword) throws GOATException {
        for (Command command : values()) {
            if (command.keyword.equals(keyword)) {
                return command;
            }
        }

        if (keyword.isEmpty()) {
            throw new GOATException("I did not catch a command there. I know: "
                    + keywordList() + ".");
        }
        throw new GOATException("I do not know the command \"" + keyword + "\". I know: "
                + keywordList() + ".");
    }

    /**
     * Lists every keyword in declaration order.
     * <p>
     * Building this from {@link #values()} means adding a constant above automatically
     * updates the help text, so the two can never fall out of step.
     *
     * @return the keywords separated by commas
     */
    private static String keywordList() {
        StringBuilder joined = new StringBuilder();
        for (Command command : values()) {
            if (!joined.isEmpty()) {
                joined.append(", ");
            }
            joined.append(command.keyword);
        }
        return joined.toString();
    }
}
