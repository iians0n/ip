# GOAT

```
  ____   ___      _     _____
 / ___| / _ \    / \   |_   _|
| |  _ | | | |  / _ \    | |
| |_| || |_| | / ___ \   | |
 \____| \___/ /_/   \_\  |_|
```

GOAT is a chatbot that keeps track of your tasks. It runs as a JavaFX window, and the
same commands also work in a terminal. It is the individual project (iP) for CS2103/T,
grown one increment at a time from the course's project template.

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
| `on DATE` | Shows the dated tasks falling on `DATE` | `on 2019-10-15` |
| `find KEYWORD` | Shows the tasks whose description contains `KEYWORD` | `find book` |
| `bye` | Ends the conversation | `bye` |

Tasks are shown with a type marker and a status marker, so `[D][X] return book (by: Dec
02 2019)` is a completed deadline. Dates are written as `yyyy-mm-dd`, as in `/by
2019-12-02`, and are shown back in the friendlier `MMM dd yyyy` form.

Unrecognised input is reported and the conversation continues; GOAT does not exit on bad
commands.

## Prerequisites

JDK 25. JavaFX comes from the Gradle build rather than from the JDK, so a plain JDK 25
works as well as a build that bundles JavaFX. Check yours with:

```bash
java -version
```

## Running with Gradle

The Gradle wrapper is included, so Gradle itself does not need to be installed. The first
run downloads it.

```bash
./gradlew run
```

This opens the GOAT window. To compile, run the tests and assemble everything:

```bash
./gradlew build
```

## Building a runnable JAR

```bash
./gradlew shadowJar
```

This produces `build/libs/goat.jar`, which bundles its dependencies and runs on its own:

```bash
java -jar goat.jar
```

GOAT saves to `data/goat.txt` relative to the folder it is run from, so running the JAR
in a new folder starts a fresh list there.

## Running the text interface

The chatbot behind the window also runs in a terminal, which is how `text-ui-test`
exercises it. Compile through Gradle so that the JavaFX dependencies are on the
classpath, then start `Goat` rather than `Launcher`:

```bash
./gradlew compileJava
java -cp build/classes/java/main seedu.goat.Goat
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
level** to `SDK default`, then run `src/main/java/seedu/goat/Launcher.java`.

Classes are organised under `src/main/java/seedu/goat`, split into `command`,
`parser`, `storage`, `task` and `ui` subpackages. The window's layout, stylesheets and
images live alongside them in `src/main/resources`.

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
