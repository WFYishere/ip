package quokka;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


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

    private static java.time.LocalDate parseFlexibleDate(String raw) {
        if (raw == null) throw new IllegalArgumentException("date is null");
        String s = raw.trim();

        try { return java.time.LocalDate.parse(s); } catch (java.time.format.DateTimeParseException ignored) {}

        s = s.replaceAll("(?i)(\\d{1,2})(st|nd|rd|th)", "$1").trim().replaceAll("\\s{2,}", " ");

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
            try { return java.time.LocalDate.parse(s, java.time.format.DateTimeFormatter.ofPattern(p)); }
            catch (java.time.format.DateTimeParseException ignored) {}
        }

        String[] noYear = { "MMM d", "MMMM d", "d MMM", "d MMMM" };
        for (String p : noYear) {
            try {
                java.time.LocalDate base = java.time.LocalDate.parse(
                        s, java.time.format.DateTimeFormatter.ofPattern(p)
                );
                return base.withYear(java.time.LocalDate.now().getYear());
            } catch (java.time.format.DateTimeParseException ignored) {}
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
