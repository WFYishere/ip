package quokka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void commandWord_trimsAndNormalizes() {
        String input = "\u00A0 todo\u00A0read book  ";
        assertEquals("todo", Parser.commandWord(input));
    }

    @Test
    void remainder_trimsAndNormalizes() {
        String input = "\u00A0deadline\u00A0 read book \u00A0 /by 2025-09-10  ";
        assertEquals("read book /by 2025-09-10", Parser.remainder(input));
    }

    @Test
    void countToken_countsNonOverlapping() {
        assertEquals(0, Parser.countToken("", " /by "));
        assertEquals(1, Parser.countToken("a /by b", " /by "));
        assertEquals(2, Parser.countToken("x /from a /to b", " /"));
    }

    @Test
    void splitOnce_splitsOnFirstOccurrence() {
        String[] p = Parser.splitOnce("desc /by 2024-01-01", " /by ");
        assertArrayEquals(new String[]{"desc", "2024-01-01"}, p);

        String[] p2 = Parser.splitOnce("desc only", " /by ");
        assertArrayEquals(new String[]{"desc only", ""}, p2);
    }
}
