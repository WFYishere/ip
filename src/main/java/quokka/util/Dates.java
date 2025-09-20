package quokka.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Date helpers: flexible parse + standard format. */
public final class Dates {
    private Dates() {}

    private static final DateTimeFormatter OUT_FMT = DateTimeFormatter.ofPattern("MMM d yyyy");

    /** Parse many human inputs into a LocalDate (throws IllegalArgumentException if fails). */
    public static LocalDate parseFlexibleDate(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("date is null");
        }
        String s = raw.trim();

        try { return LocalDate.parse(s); } catch (DateTimeParseException ignored) {}

        s = s.replaceAll("(?i)(\\d{1,2})(st|nd|rd|th)", "$1").trim().replaceAll("\\s{2,}", " ");

        // strip trailing time-ish parts
        s = s.replaceFirst("(?i)\\s+(\\d{3,4})$", "");
        s = s.replaceFirst("(?i)\\s+\\d{1,2}:\\d{2}([ap]m)?$", "");
        s = s.replaceFirst("(?i)\\s+\\d{1,2}\\s*-\\s*\\d{1,2}\\s*[ap]m$", "");

        String[] patterns = {
            "yyyy-MM-dd",
            "d/M/uuuu", "d-M-uuuu",
            "d MMM uuuu", "d MMMM uuuu",
            "MMM d uuuu", "MMMM d uuuu"
        };
        for (String p : patterns) {
            try { return LocalDate.parse(s, DateTimeFormatter.ofPattern(p)); }
            catch (DateTimeParseException ignored) {}
        }

        String[] noYear = { "MMM d", "MMMM d", "d MMM", "d MMMM" };
        for (String p : noYear) {
            try {
                LocalDate base = LocalDate.parse(s, DateTimeFormatter.ofPattern(p));
                return base.withYear(LocalDate.now().getYear());
            } catch (DateTimeParseException ignored) {}
        }

        throw new IllegalArgumentException("Unrecognized date format: \"" + raw + "\"");
    }

    /** Format a date consistently for UI. */
    public static String fmt(LocalDate date) {
        return date.format(OUT_FMT);
    }
}
