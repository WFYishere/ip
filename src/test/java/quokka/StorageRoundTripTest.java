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
        assertTrue(in.get(0) instanceof Todo);
        assertEquals("read book", in.get(0).getDescription());
        assertFalse(in.get(0).isDone());
        assertTrue(in.get(1) instanceof Deadline);
        assertEquals("return book", in.get(1).getDescription());
        assertTrue(in.get(1).isDone());
        assertTrue(in.get(2) instanceof Event);
        assertEquals("project", in.get(2).getDescription());
        assertFalse(in.get(2).isDone());
    }
}
