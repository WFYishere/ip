package quokka;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Event extends Task {
    protected LocalDate from;
    protected LocalDate to;

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = parseFlexibleDate(from);
        this.to   = parseFlexibleDate(to);
    }

    public Event(String description, String from, String to, boolean isDone) {
        super(description, TaskType.EVENT, isDone);
        this.from = parseFlexibleDate(from);
        this.to   = parseFlexibleDate(to);
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
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d yyyy");
        return super.toString() + " (from: " + from.format(fmt) + " to: " + to.format(fmt) + ")";
    }

    @Override
    public String toDataString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }
}
