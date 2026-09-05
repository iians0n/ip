package seedu.goat.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the task collection and the date query built on it.
 */
public class TaskListTest {

    private static final LocalDate OCT_15 = LocalDate.of(2019, 10, 15);

    @Test
    public void findOn_deadlineDueThatDay_matches() {
        TaskList tasks = new TaskList(new Deadline("return book", OCT_15));
        assertEquals(1, tasks.findOn(OCT_15).size());
    }

    @Test
    public void findOn_deadlineDueAnotherDay_doesNotMatch() {
        TaskList tasks = new TaskList(new Deadline("return book", LocalDate.of(2019, 12, 2)));
        assertTrue(tasks.findOn(OCT_15).isEmpty());
    }

    @Test
    public void findOn_dateInsideEventSpan_matches() {
        TaskList tasks = new TaskList(new Event("conference", LocalDate.of(2019, 10, 14),
                LocalDate.of(2019, 10, 16)));
        assertEquals(1, tasks.findOn(OCT_15).size());
    }

    @Test
    public void findOn_eventStartAndEndDates_bothCount() {
        // The span includes its endpoints, which is the easiest thing to get wrong.
        TaskList tasks = new TaskList(new Event("conference", LocalDate.of(2019, 10, 14),
                LocalDate.of(2019, 10, 16)));
        assertEquals(1, tasks.findOn(LocalDate.of(2019, 10, 14)).size());
        assertEquals(1, tasks.findOn(LocalDate.of(2019, 10, 16)).size());
        assertTrue(tasks.findOn(LocalDate.of(2019, 10, 13)).isEmpty());
        assertTrue(tasks.findOn(LocalDate.of(2019, 10, 17)).isEmpty());
    }

    @Test
    public void findOn_todo_neverMatches() {
        TaskList tasks = new TaskList(new Todo("read book"));
        assertTrue(tasks.findOn(OCT_15).isEmpty());
    }

    @Test
    public void findOn_severalMatches_returnedInListOrder() {
        TaskList tasks = new TaskList(
                new Deadline("first", OCT_15),
                new Todo("ignored"),
                new Event("second", OCT_15, OCT_15));

        List<Task> found = tasks.findOn(OCT_15);
        assertEquals(2, found.size());
        assertTrue(found.get(0).toString().contains("first"));
        assertTrue(found.get(1).toString().contains("second"));
    }

    @Test
    public void find_keywordInDescription_matches() {
        TaskList tasks = new TaskList(new Todo("read book"), new Todo("buy bread"));
        assertEquals(1, tasks.find("book").size());
    }

    @Test
    public void find_differentCase_stillMatches() {
        TaskList tasks = new TaskList(new Todo("Read Book"));
        assertEquals(1, tasks.find("book").size());
        assertEquals(1, tasks.find("BOOK").size());
    }

    @Test
    public void find_keywordInsideAWord_matches() {
        // A substring match is intended: searching "book" should find "bookshop".
        TaskList tasks = new TaskList(new Todo("visit the bookshop"));
        assertEquals(1, tasks.find("book").size());
    }

    @Test
    public void find_noMatch_emptyList() {
        TaskList tasks = new TaskList(new Todo("read book"));
        assertTrue(tasks.find("bicycle").isEmpty());
    }

    @Test
    public void find_matchesAcrossTaskTypes_allReturnedInListOrder() {
        TaskList tasks = new TaskList(
                new Todo("read book"),
                new Deadline("return book", OCT_15),
                new Todo("unrelated"),
                new Event("book fair", OCT_15, OCT_15));

        List<Task> found = tasks.find("book");
        assertEquals(3, found.size());
        assertTrue(found.get(0).toString().contains("read book"));
        assertTrue(found.get(1).toString().contains("return book"));
        assertTrue(found.get(2).toString().contains("book fair"));
    }

    @Test
    public void find_keywordMatchingOnlyADate_doesNotMatch() {
        // Only the description is searched, not the formatted date.
        TaskList tasks = new TaskList(new Deadline("return book", OCT_15));
        assertTrue(tasks.find("Oct").isEmpty());
    }

    @Test
    public void delete_middleTask_removedAndLaterTasksShiftDown() {
        TaskList tasks = new TaskList(new Todo("first"), new Todo("second"),
                new Todo("third"));

        Task removed = tasks.delete(1);

        assertEquals("[T][ ] second", removed.toString());
        assertEquals(2, tasks.size());
        assertEquals("[T][ ] third", tasks.get(1).toString());
    }

    @Test
    public void constructor_fromExistingList_copiesRatherThanShares() {
        // Sharing the caller's list would let outside changes mutate the task list.
        List<Task> source = new ArrayList<>();
        source.add(new Todo("read book"));

        TaskList tasks = new TaskList(source);
        source.add(new Todo("added afterwards"));

        assertEquals(1, tasks.size());
    }

    @Test
    public void asList_returnedView_cannotBeModified() {
        TaskList tasks = new TaskList(new Todo("read book"));
        assertThrows(UnsupportedOperationException.class,
                () -> tasks.asList().add(new Todo("sneaked in")));
    }

    @Test
    public void isEmpty_newList_true() {
        // No arguments is a valid varargs call, so this still builds an empty list.
        assertTrue(new TaskList().isEmpty());
    }

    @Test
    public void constructor_severalTasksNamedInPlace_heldInThatOrder() {
        TaskList tasks = new TaskList(new Todo("first"), new Todo("second"),
                new Todo("third"));

        assertEquals(3, tasks.size());
        assertEquals("[T][ ] first", tasks.get(0).toString());
        assertEquals("[T][ ] third", tasks.get(2).toString());
    }

    @Test
    public void isEmpty_afterAdd_false() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        assertFalse(tasks.isEmpty());
    }
}
