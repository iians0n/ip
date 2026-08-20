/**
 * Entry point for the GOAT chatbot.
 * <p>
 * At this increment GOAT simply greets the user and exits.
 */
public class GOAT {

    /** Horizontal rule printed around each block of output. */
    private static final String LINE =
            "____________________________________________________________";

    /** The bot's name, kept in one place so every message stays consistent. */
    private static final String NAME = "GOAT";

    /** ASCII-art banner shown once at startup. */
    private static final String BANNER = """
  ____   ___      _     _____
 / ___| / _ \\    / \\   |_   _|
| |  _ | | | |  / _ \\    | |
| |_| || |_| | / ___ \\   | |
 \\____| \\___/ /_/   \\_\\  |_|
""";

    public static void main(String[] args) {
        System.out.println(BANNER);
        greet();
        farewell();
    }

    /** Prints the horizontal rule that brackets every response. */
    private static void showLine() {
        System.out.println(LINE);
    }

    /** Prints the opening message shown when the program starts. */
    private static void greet() {
        showLine();
        System.out.println(" Hello! I'm " + NAME);
        System.out.println(" What can I do for you?");
        showLine();
    }

    /** Prints the closing message shown just before the program ends. */
    private static void farewell() {
        System.out.println(" Bye. Hope to see you again soon!");
        showLine();
    }
}
