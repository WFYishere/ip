package quokka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeadlineTest {
    @Test
    void isoParseAndFormat() {
        Deadline d = new Deadline("return book", "2019-10-15");
        assertEquals("D | 0 | return book | 2019-10-15", d.toDataString());
        assertTrue(d.toString().contains("2019"), d.toString());
    }

    @Test
    void flexibleParsing_dMY_withTimeIgnored() {
        Deadline d = new Deadline("return book", "2/12/2019 1800");
        assertEquals("D | 0 | return book | 2019-12-02", d.toDataString());
        assertTrue(d.toString().contains("2019"), d.toString());
    }
}
