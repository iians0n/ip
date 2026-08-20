# GOAT

```
  ____   ___      _     _____
 / ___| / _ \    / \   |_   _|
| |  _ | | | |  / _ \    | |
| |_| || |_| | / ___ \   | |
 \____| \___/ /_/   \_\  |_|
```

GOAT is a command-line chatbot that keeps track of your tasks. It is the individual
project (iP) for CS2103/T, grown one increment at a time from the course's project
template.

## Features

| Command | What it does | Example |
|---|---|---|
| `todo DESCRIPTION` | Adds a task with no date | `todo borrow book` |
| `deadline DESCRIPTION /by WHEN` | Adds a task that is due at some point | `deadline return book /by Sunday` |
| `event DESCRIPTION /from START /to END` | Adds a task that spans a period | `event project meeting /from Mon 2pm /to 4pm` |
| `list` | Shows every task, numbered, with its status | `list` |
| `mark N` | Marks task `N` as done | `mark 2` |
| `unmark N` | Marks task `N` as not done | `unmark 2` |
| `delete N` | Removes task `N` | `delete 3` |
| `bye` | Ends the conversation | `bye` |

Tasks are shown with a type marker and a status marker, so `[D][X] return book (by:
Sunday)` is a completed deadline. Dates and times are kept as free text, so `/by next
Friday` is just as valid as `/by Sunday`.

Unrecognised input is reported and the conversation continues; GOAT does not exit on bad
commands.

## Prerequisites

JDK 25. On macOS the course requires the Azul Zulu build that bundles JavaFX; check yours
with:

```bash
java -version
```

## Running from the command line

```bash
javac -d out src/main/java/*.java
java -cp out GOAT
```

## Running the tests

`text-ui-test/runtest.sh` feeds `input.txt` to GOAT and compares the output against
`EXPECTED.TXT`, printing a diff of anything that changed:

```bash
./text-ui-test/runtest.sh
```

When an increment changes the output on purpose, review the diff and then update the
baseline with `./text-ui-test/runtest.sh --bless`.

## Setting up in an IDE

**VS Code:** install the Extension Pack for Java, open this folder, and point the Java
extension at your JDK 25 installation.

**IntelliJ:** open the project, set the SDK to **JDK 25** and the **Project language
level** to `SDK default`, then run `src/main/java/GOAT.java`.

**Warning:** keep `src/main/java` as the root folder for Java files. Do not rename those
folders or move Java files outside that path, as tools such as Gradle expect to find them
there.

## Acknowledgements

This project started from the CS2103/T `ip` project template
([NUS-CS2103-AY2627-S1/ip](https://github.com/NUS-CS2103-AY2627-S1/ip)).

I used **Claude Code**, Anthropic's command-line coding agent, while building this
project. It was used to write and refactor the code for the increments in this
repository, working from the course's increment specifications and under my direction.
The course permits AI-assisted work at levels AI-2 to AI-5.
