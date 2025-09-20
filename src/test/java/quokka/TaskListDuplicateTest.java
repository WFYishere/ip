package quokka;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskListDuplicateTest {

    @Test
    void containsDuplicate_todo_caseInsensitiveOnDesc() {
        TaskList tl = new TaskList(new ArrayList<>());
        Todo a = new Todo("Read Book");
        Todo b = new Todo("read book");

        assertFalse(tl.containsDuplicate(a));
        tl.add(a);
        assertTrue(tl.containsDuplicate(b));
    }

    @Test
    void containsDuplicate_deadline_usesDateField() {
        TaskList tl = new TaskList(new ArrayList<>());
        Deadline a = new Deadline("submit report", "2025-09-10");
        Deadline b = new Deadline("Submit Report", "2025-09-10");
        Deadline c = new Deadline("Submit Report", "2025-09-11");

        tl.add(a);
        assertTrue(tl.containsDuplicate(b));
        assertFalse(tl.containsDuplicate(c));
    }

    @Test
    void containsDuplicate_event_usesBothDates() {
        TaskList tl = new TaskList(new ArrayList<>());
        Event a = new Event("trip", "2025-09-10", "2025-09-12");
        Event b = new Event("TRIP", "2025-09-10", "2025-09-12");
        Event c = new Event("trip", "2025-09-11", "2025-09-12");

        tl.add(a);
        assertTrue(tl.containsDuplicate(b));
        assertFalse(tl.containsDuplicate(c));
    }
}
