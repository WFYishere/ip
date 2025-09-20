package quokka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventValidationTest {

    @Test
    void constructor_rejectsSameOrReversedRange() {
        IllegalArgumentException same = assertThrows(IllegalArgumentException.class,
            () -> new Event("demo", "2025-09-10", "2025-09-10"));
        assertTrue(same.getMessage().toLowerCase().contains("start"));

        IllegalArgumentException reversed = assertThrows(IllegalArgumentException.class,
            () -> new Event("demo", "2025-09-11", "2025-09-10"));
        assertTrue(reversed.getMessage().toLowerCase().contains("start"));
    }

    @Test
    void constructor_acceptsProperRange() {
        Event e = new Event("demo", "2025-09-10", "2025-09-12");
        assertNotNull(e);
    }

    @Test
    void constructor_rejectsImpossibleDate() {
        assertThrows(IllegalArgumentException.class,
            () -> new Event("demo", "2025-02-30", "2025-03-01"));
    }
}
