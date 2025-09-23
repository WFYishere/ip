package quokka;

import org.junit.jupiter.api.Test;
import quokka.util.Dates;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DatesTest {

    @Test
    void parseStrictDate_acceptsCommonPatterns() {
        assertEquals(LocalDate.of(2025, 9, 10), Dates.parseStrictDate("2025-09-10"));
        assertEquals(LocalDate.of(2025, 9, 10), Dates.parseStrictDate("10/9/2025"));
        assertEquals(LocalDate.of(2025, 9, 10), Dates.parseStrictDate("10 Sep 2025"));
        assertEquals(LocalDate.of(2025, 9, 1),  Dates.parseStrictDate("1-9-2025"));
    }

    @Test
    void parseStrictDate_rejectsImpossibleDates() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Dates.parseStrictDate("2025-02-30"));
        assertTrue(ex.getMessage().toLowerCase().contains("date") || ex.getMessage().toLowerCase().contains("parse"));
    }

    @Test
    void validateHHmm_happyPath() {
        assertEquals(0, Dates.validateHHmm("0000"));
        assertEquals(9 * 60 + 30, Dates.validateHHmm("0930"));
        assertEquals(23 * 60 + 59, Dates.validateHHmm("2359"));
    }

    @Test
    void validateHHmm_rejectsBadTimes() {
        assertThrows(IllegalArgumentException.class, () -> Dates.validateHHmm("24:00"));
        assertThrows(IllegalArgumentException.class, () -> Dates.validateHHmm("2460"));
        assertThrows(IllegalArgumentException.class, () -> Dates.validateHHmm("abcd"));
        assertThrows(IllegalArgumentException.class, () -> Dates.validateHHmm("9999"));
    }

    @Test
    void fmt_formatsShortMonthStyle() {
        assertEquals("Sep 10 2025",
            Dates.fmt(java.time.LocalDate.of(2025, 9, 10)));
    }

}
