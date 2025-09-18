import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



public class Deadline extends Task {
    protected LocalDate by;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = parseFlexibleDate(by);
    }

    public Deadline(String description, String by, boolean isDone) {
        super(description, TaskType.DEADLINE, isDone);
        this.by = parseFlexibleDate(by);
    }

    private static LocalDate parseFlexibleDate(String raw) {
        if (raw == null) throw new IllegalArgumentException("date is null");
        String s = raw.trim();

        try { return LocalDate.parse(s); } catch (DateTimeParseException ignored) {}

        // 1) Normalise (remove ordinals and trailing time-like fragments)
        s = s.replaceAll("(?i)(\\d{1,2})(st|nd|rd|th)", "$1");
        s = s.replaceAll("(?i)\\b\\d{1,2}\\s*-\\s*\\d{1,2}\\s*[ap]m\\b.*$", "");
        s = s.replaceAll("(?i)\\b\\d{3,4}\\b.*$", "");
        s = s.replaceAll("(?i)\\b\\d{1,2}:\\d{2}\\b.*$", "");
        s = s.trim().replaceAll("\\s{2,}", " ");

        // 2) Try common full patterns (with year)
        String[] patterns = {
                "yyyy-MM-dd", "d/M/uuuu", "d-M-uuuu", "d MMM uuuu",
                "d MMMM uuuu", "MMM d uuuu", "MMMM d uuuu"
        };
        for (String p : patterns) {
            try { return LocalDate.parse(s, DateTimeFormatter.ofPattern(p)); }
            catch (DateTimeParseException ignored) {}
        }

        // 3) If year omitted ("June 6"), assume current year
        String[] noYear = { "MMM d", "MMMM d", "d MMM", "d MMMM" };
        for (String p : noYear) {
            try {
                LocalDate base = LocalDate.parse(s, DateTimeFormatter.ofPattern(p));
                return base.withYear(LocalDate.now().getYear());
            } catch (DateTimeParseException ignored) {}
        }

        throw new IllegalArgumentException("Unrecognized date format: \"" + raw + "\"");
    }



    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }

    @Override
    public String toDataString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}
