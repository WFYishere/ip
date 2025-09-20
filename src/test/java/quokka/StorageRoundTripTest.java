package quokka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StorageRoundTripTest {
    @Test
    void saveThenLoad_roundTripPreservesData() throws Exception {
        Path tempDir = Files.createTempDirectory("quokka-test-");
        Path file = tempDir.resolve("duke.txt");

        List<Task> out = new ArrayList<>();
        Todo t1 = new Todo("read book");
        Deadline t2 = new Deadline("return book", "2019-10-15");
        Event t3 = new Event("project", "2019-12-01", "2019-12-02");
        t2.markAsDone();

        out.add(t1); out.add(t2); out.add(t3);
        Storage.save(file, out);

        List<Task> in = new ArrayList<>();
        Storage.load(file, in);

        assertEquals(3, in.size());
        assertInstanceOf(Todo.class, in.get(0));
        assertEquals("read book", in.get(0).getDescription());
        assertFalse(in.get(0).isDone());
        assertInstanceOf(Deadline.class, in.get(1));
        assertEquals("return book", in.get(1).getDescription());
        assertTrue(in.get(1).isDone());
        assertInstanceOf(Event.class, in.get(2));
        assertEquals("project", in.get(2).getDescription());
        assertFalse(in.get(2).isDone());
    }
}
