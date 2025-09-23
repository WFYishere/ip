/**
 * Parse a date using strict patterns (yyyy-MM-dd, d/M/yyyy,
 * d MMM yyyy, etc.). Rejects invalid calendar dates.
 *
 * @param raw user input string
 * @return parsed LocalDate
 * @throws IllegalArgumentException if format is unrecognized/invalid
 */


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

    public static java.time.LocalDate parseStrictDate(String raw) {
        if (raw == null) throw new IllegalArgumentException("date is null");
        String s = raw.trim();
        java.time.format.ResolverStyle STRICT = java.time.format.ResolverStyle.STRICT;

        String[] patterns = new String[]{
            "uuuu-MM-dd", "d/M/uuuu", "d-M-uuuu", "d.M.uuuu",
            "uuuu/M/d", "uuuu-M-d", "M/d/uuuu", "d MMM uuuu", "MMM d uuuu"
        };
        for (String p : patterns) {
            try {
                java.time.format.DateTimeFormatter f =
                    new java.time.format.DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern(p)
                        .toFormatter()
                        .withResolverStyle(STRICT);
                return java.time.LocalDate.parse(s, f);
            } catch (java.time.format.DateTimeParseException ignored) {}
        }
        throw new IllegalArgumentException("Unparseable or invalid calendar date: " + raw);
    }

    /**
     * Validate a 24-hour HHmm time and return minutes since 00:00.
     *
     * @param raw four-digit time string in HHmm
     * @return minutes in [0, 1439]
     * @throws IllegalArgumentException if not HHmm or out of range
     */
    public static int validateHHmm(String raw) {
        if (raw == null) throw new IllegalArgumentException("time is null");
        String s = raw.trim();
        if (!s.matches("\\d{4}")) {
            throw new IllegalArgumentException("Invalid time, expected HHmm: " + raw);
        }
        int hh = Integer.parseInt(s.substring(0, 2));
        int mm = Integer.parseInt(s.substring(2, 4));
        if (hh < 0 || hh > 23 || mm < 0 || mm > 59) {
            throw new IllegalArgumentException("Invalid time, expected 0000..2359: " + raw);
        }
        return hh * 60 + mm;
    }

}
