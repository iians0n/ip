# Week 6 submission audit

Audit date: 14 September 2026. Product: **GOAT**. Fork:
[iians0n/ip](https://github.com/iians0n/ip) (GitHub account `iians0n`).
The working tree was clean at the start, at `69df496`. No existing branches,
public tags, or historical commits were rewritten or deleted.

## Requirements and evidence

| Requirement | Existing evidence | Missing work at initial audit | Verification / outcome |
| --- | --- | --- | --- |
| Week 2: Level-0 through Level-6, A-Enums | All exact tags exist; greeting/echo/task hierarchy/errors/deletion/enum in their commits | No required implementation gap found | Inspected tag history; final command and model tests pass |
| Week 3: Level-7 and Level-8 as retained branches | Both branch refs exist locally and remotely; tags point to two-parent merge commits | Strengthen storage safety | Merge graph inspected; storage/date tests pass |
| Week 3: A-MoreOOP, A-Packages, A-Gradle, A-JUnit, A-Jar | Exact tags and corresponding classes/build/tests present | Expand tests for Week 6 | Final build/check and packaged launch pass |
| Week 3: parallel A-JavaDoc, A-CodingStandard, Level-9 | Retained branches, separate common-base work, merge tags in history | Indentation was not enforced by Checkstyle | Added indentation checks; all style checks pass |
| Upstream iP PR and Week 4 GFMD | [PR #311](https://github.com/NUS-CS2103-AY2627-S1/ip/pull/311), title `[iians0n] iP`, required Markdown elements present | PR text still describes the old command-line version and leaves Level-10 unchecked | Historical GFMD requirement is evidenced; PR messaging was not changed |
| Week 4 peer reviews | COMMENTED reviews on [#368](https://github.com/NUS-CS2103-AY2627-S1/ip/pull/368) and [#251](https://github.com/NUS-CS2103-AY2627-S1/ip/pull/251) | Reviews were submitted after the scheduled deadline | GitHub timestamps: September 4 at 21:55–21:58 Singapore time; course deadline was 16:00 |
| Level-10 GUI, A-Varargs | Retained merged branches and tags; GUI/FXML and varargs methods present | Final GUI smoke test and screenshot | Final window tested, enlarged/restored, saved screenshot in `docs/Ui.png` |
| Week 5 A-Assertions, A-CodeQuality, A-Streams | Fork PRs #1–#3 merged September 7; exact tags; parallel branches and master sync merges retained | No required history change | Assertions enabled, command handlers inspected, stream implementations and tests pass |
| A-FullCommitMessage | Existing tag at `44fd1db`; at least three Week 5 commits have explanatory bodies | No historical change | Inspected `44fd1db`, `0cbf82b`, `1e6a928` |
| BCD-Extension | Duplicate detection in PR #4, tag `69df496` | Confirm team members chose different extensions | Duplicate tests pass; team coordination is not inferable from Git |
| A-CI | No workflow/tag | Optional increment not selected | Week 5 calls this optional; no completion claimed |
| A-MoreErrorHandling | Existing basic input errors, strict ISO parsing, duplicate rejection | Repeated parameters, whitespace, unsafe overwrites, failed-save consistency | Implemented and tested; tag `9959a43` |
| A-MoreTesting | 77 JUnit tests in four suites at baseline | Application sequences, command enum, model state, text UI, storage failures, locale/boundaries | 99 tests in eight suites, all pass; text test now isolated; tag `8682967` |
| A-BetterGui | Existing bubbles, resizing, avatars | Unverified avatar licenses and text readability | Replaced photos with text labels, improved contrast/default width, verified final GUI; tag `623d5f2` |
| Java style and OOP | Classes split into command/parser/storage/task/ui; existing Checkstyle | Switch/continuation indentation, comment spelling/punctuation | Stronger Checkstyle passes; explicit imports, exception handling, encapsulation inspected |
| User Guide and screenshot | Placeholder guide, no screenshot | Complete guide, capture real window, publish Pages | `docs/README.md` and `docs/Ui.png`; public content and image bytes checked |
| AI and reuse acknowledgement | Root README already credits Claude Code | Credit extensive Week 6 Codex use and tooling | Credits preserved and extended; undocumented avatar media removed; no new runtime libraries |
| Java 25 and fat JAR | Java 25 build and course JavaFX dependencies already configured | Final clean build, manifest/resource verification and isolated launch | See release verification below |
| Git history and tags | All earlier refs already pushed | New genuine commits and exact Week 6 tags | No force push, tag moves, backdating, or synthetic history; no `Git Standard` tag |
| Final submission | No GitHub releases and Pages disabled initially | Publish one public JAR release and Pages | Pages enabled at `master:/docs`; release publication is the final external step after this audit snapshot |
| Historical progress / attendance | Commits dated August 20, August 26, September 5, September 7 | Attendance, Git-Mastery work, team coordination and personal dashboard mapping cannot be inferred | No historical completion or all-green dashboard claim |

## Choices and compatibility

The two selected Week 6 increments address the largest practical gaps:
protecting saved tasks and exercising non-GUI behavior end to end. GUI
readability was also improved to remove media without recorded reuse rights.

GOAT uses inclusive **calendar dates**, not instants. Same-day events were
already explicitly supported by tests and remain valid. Reversed ranges and
impossible dates are rejected. The Week 6 equal-endpoint example is treated
as something to consider in the product's date semantics, not a command to
remove existing single-day events.

The saved text format remains compatible. Legacy duplicates and single-day
events are preserved. Malformed records, including obsolete free-text dates,
remain in the original file; GOAT displays recoverable tasks read-only and
explains recovery. Failed saves roll back changes. Atomic replacement avoids
truncating the old file, and a byte comparison detects intervening edits.
Use one instance per data folder: the comparison is not a multi-process lock.
Atomic moves and filesystem permissions are platform/filesystem-dependent;
failure is reported rather than falling back to an unsafe overwrite.

## Verification

- macOS 26.5, Apple Silicon (`aarch64`), Zulu JavaFX JDK 25.0.4+7-LTS.
- Gradle 9.6.1 launcher and daemon both reported Java 25.0.4. Source and target
  compatibility are 25; no separate Gradle toolchain was configured.
- `./gradlew clean shadowJar`: successful. `./gradlew check`: successful.
- `./text-ui-test/runtest.sh`: PASSED with the original expected transcript.
  Its old `rm -rf data` behavior was replaced with a temporary test directory.
- 99 JUnit tests: parser 32, task list 30, storage 14, dates 9,
  application 7, model 3, command enum 2, text UI 2; no failures/skips.
- Tests use JUnit temporary directories, never the user's real task file.
- The JAR manifest points to `seedu.goat.Launcher`. Both FXML layouts and CSS
  files are present. JavaFX `.dll`, `.dylib`, and `.so` libraries are present,
  using the exact platform dependency pattern from the course tutorial.
- Final JAR copied into a fresh empty directory and run with `java -jar goat.jar`.
  A temporary macOS app wrapper exposes that same command to UI automation;
  the wrapper is not part of the release and is not required by users.
- GUI: first-run missing storage; all three task types; list; mark/unmark;
  delete; find/on; impossible date; invalid index; duplicate refusal;
  error recovery; bye; restart with three tasks and completed status restored.
- Default and enlarged/restored windows inspected; long conversation and
  wrapped replies remain usable. Minimum-size drag could not be exercised
  by the automation service, so no minimum-size test is claimed.
- `docs/Ui.png` is an unaltered capture of one complete running GUI window,
  showing the persisted sample list and GOAT title. No generated mockup.
- Windows/Linux execution remains untested. Packaged native files alone are
  not evidence of successful execution on those systems.

The macOS course installation page names the older Zulu FX patch 25.0.3.
The installed 25.0.4 distribution passed all checks. The official Azul
25.0.3+9 FX archive was then downloaded to `/tmp`, without changing the
installed JDK or system defaults. Gradle launcher and daemon both reported
25.0.3; a second `clean shadowJar`, all 99 tests, both style checks, and the
isolated transcript test passed. The resulting JAR is byte-identical to the
one tested through the GUI on 25.0.4. The same JAR also launched under
25.0.3 and restored the three saved tasks. Listing, invalid-date recovery,
adding/deleting a temporary task, and submitting a search through **Send**
were verified in that runtime.

Final JAR: `goat.jar` (10,751,985 bytes).
SHA-256: `4f1d8ad9b06986a968674f836094cea6e2df604ac42d42cccbc8e6b1ebcd4d31`.

Pages API reports a successful build at `bab1a90`, using `master:/docs`.
Anonymous HTTPS requests to the [guide](https://iians0n.github.io/ip/) and
[screenshot](https://iians0n.github.io/ip/Ui.png) succeeded. The generated HTML
contains a real command table, the product heading, all guide sections and
correct link targets. Downloaded screenshot bytes match `docs/Ui.png`.
Browser visual verification was blocked by Chrome reporting that tabs could
not be edited; no browser screenshot of the website is claimed.

The public progress dashboard was last updated September 13 at 23:45 when
checked. It uses masked student identifiers; no reliable mapping to `iians0n`
was available, so personal status (including Git Standard) remains unverified.
The Showcase page was accessible, but a GOAT entry was not identifiable in
the extracted content. Recheck after the next course dashboard refresh.

The last five commits after this audit commit have imperative, capitalized
subjects under 72 characters and properly separated/wrapped bodies. Earlier
commit `9959a43` has a body containing literal newline escapes, an error
preserved rather than rewriting history; it falls outside the final five.

## Another-OS smoke script

On Windows or Linux with Java 25, download the release's **single** `goat.jar`
into a new writable folder. Record OS, architecture, and `java -version`.
Run `java -jar goat.jar`, then enter:

```text
todo read CS2103 notes
deadline submit iP /by 2026-09-18
event team retreat /from 2026-09-19 /to 2026-09-20
mark 1
list
find retreat
on 2026-09-18
deadline impossible /by 2026-02-30
mark 99
unmark 1
todo temporary smoke task
delete 4
bye
```

Expect three tasks, the appropriate search results, actionable errors for the
invalid date/index, and normal continued use. Restart with the same command;
`list` must restore three tasks, with task 1 incomplete. Resize the window,
scroll earlier replies, test Send as well as Enter, and close with `bye`.
Report any startup errors and the JAR SHA-256. No teammate/forum messages
were sent on the owner's behalf.

## Authoritative sources

- [Week 6 project](https://nus-cs2103-ay2627-s1.github.io/website/schedule/week6/project.html)
- [Course standards](https://nus-cs2103-ay2627-s1.github.io/website/admin/standardsAndConventions.html)
- [Java basic + intermediate](https://se-education.org/guides/conventions/java/intermediate.html)
- [Git conventions](https://se-education.org/guides/conventions/git.html)
- [Week 2](https://nus-cs2103-ay2627-s1.github.io/website/admin/ip-w2.html)
- [Week 3](https://nus-cs2103-ay2627-s1.github.io/website/admin/ip-w3.html)
- [Week 4](https://nus-cs2103-ay2627-s1.github.io/website/admin/ip-w4.html)
- [Week 5](https://nus-cs2103-ay2627-s1.github.io/website/admin/ip-w5.html)
- [JavaFX packaging and launcher](https://se-education.org/guides/tutorials/javaFxPart1.html)
- [macOS Java installation](https://se-education.org/guides/tutorials/javaInstallationMac.html)
- [Reuse policy](https://nus-cs2103-ay2627-s1.github.io/website/admin/appendixB-policies.html#policy-on-reuse)

The Week 6 deadline is September 18, 2026 at 23:59 Singapore time. The source,
guide and latest release JAR form the submission; there is no separate Canvas
submission. This audit does not predict marks.

## Earlier tag inventory (preserved)

The remote tag IDs matched the local IDs before editing. Tagged source and
the merge graph were inspected; this records historical evidence, not a claim
that every old revision has been rebuilt on today's environment.

| Tag | Commit | Commit date | Subject |
| --- | --- | --- | --- |
| `A-Assertions` | `bf1db1e` | 2026-09-07 | Merge pull request #1 from iians0n/branch-A-Assertions |
| `A-CodeQuality` | `dc8b8ee` | 2026-09-07 | Merge pull request #2 from iians0n/branch-A-CodeQuality |
| `A-CodingStandard` | `ee323b7` | 2026-08-26 | Merge branch 'branch-A-CodingStandard' |
| `A-Enums` | `76986ba` | 2026-08-20 | Use enums for command types |
| `A-FullCommitMessage` | `44fd1db` | 2026-09-07 | Add assertions to document key assumptions |
| `A-Gradle` | `b84cdfe` | 2026-08-26 | Point the Gradle build at the Goat main class |
| `A-JUnit` | `68242da` | 2026-08-26 | Merge branch 'branch-A-JUnit' |
| `A-Jar` | `b84cdfe` | 2026-08-26 | Point the Gradle build at the Goat main class |
| `A-JavaDoc` | `1cd2721` | 2026-08-26 | Merge branch 'branch-A-JavaDoc' |
| `A-MoreOOP` | `e86a7db` | 2026-08-26 | Merge branch 'branch-A-MoreOOP' |
| `A-Packages` | `12e54eb` | 2026-08-26 | Merge branch 'branch-A-Packages' |
| `A-Streams` | `b90a94e` | 2026-09-07 | Merge pull request #3 from iians0n/branch-A-Streams |
| `A-TextUiTesting` | `dc0fcfb` | 2026-08-20 | Add text UI test harness |
| `A-Varargs` | `d4f9eca` | 2026-09-05 | Merge branch 'branch-A-Varargs' |
| `BCD-Extension` | `69df496` | 2026-09-07 | Merge pull request #4 from iians0n/branch-C-DetectDuplicates |
| `Level-0` | `e9621d9` | 2026-08-20 | Rename main class to GOAT and add greeting |
| `Level-1` | `4e6828b` | 2026-08-20 | Add support for echo |
| `Level-10` | `93f9a83` | 2026-09-05 | Merge branch 'branch-Level-10' |
| `Level-2` | `e4a8530` | 2026-08-20 | Add support for add |
| `Level-3` | `8596070` | 2026-08-20 | Extract Task class |
| `Level-4` | `066f909` | 2026-08-20 | Add Task subclasses and todo/deadline/event commands |
| `Level-5` | `d7cdeb0` | 2026-08-20 | Add custom exception and handle input errors |
| `Level-6` | `40bdfcd` | 2026-08-20 | Add delete command |
| `Level-7` | `5ab659c` | 2026-08-26 | Merge branch 'branch-Level-7' |
| `Level-8` | `8051c4f` | 2026-08-26 | Merge branch 'branch-Level-8' |
| `Level-9` | `1077dc3` | 2026-08-26 | Merge branch 'branch-Level-9' |
| `v0.1` | `ee323b7` | 2026-08-26 | Merge branch 'branch-A-CodingStandard' |