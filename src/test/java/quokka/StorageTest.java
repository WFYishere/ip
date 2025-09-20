package quokka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    @TempDir
    Path tmpDir;

    @Test
    void saveAndLoad_roundTripAndSkipCorrupted() throws Exception {
        Path data = tmpDir.resolve("quokka-data.txt");

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));
        tasks.add(new Deadline("submit report", "2025-09-10"));
        tasks.add(new Event("camp", "2025-09-10", "2025-09-12"));

        Storage.save(data, tasks);

        Files.writeString(data, "X | 0 | nonsense line\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);

        List<Task> loaded = new ArrayList<>();
        Storage.load(data, loaded);

        assertEquals(3, loaded.size());

        assertTrue(loaded.get(0) instanceof Todo);
        assertTrue(loaded.get(1) instanceof Deadline);
        assertTrue(loaded.get(2) instanceof Event);
    }

    @Test
    void save_createsParentDirectories() throws Exception {
        Path nested = tmpDir.resolve("a/b/c/data.txt");
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("hello"));

        Storage.save(nested, tasks);
        assertTrue(Files.exists(nested), "Data file should be created along nested dirs");

        List<Task> loaded = new ArrayList<>();
        Storage.load(nested, loaded);
        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0) instanceof Todo);
    }
}
