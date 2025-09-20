package quokka;

import quokka.util.Dates;

import java.time.LocalDate;

/** A deadline task that is due on a single date. */
public class Deadline extends Task {
    protected LocalDate by;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = Dates.parseFlexibleDate(by);
        assert this.by != null : "Deadline date must parse";
    }

    public Deadline(String description, String by, boolean isDone) {
        super(description, TaskType.DEADLINE, isDone);
        this.by = Dates.parseFlexibleDate(by);
        assert this.by != null : "Deadline date must parse";
    }

    /** Expose the due date for searches/filters. */
    public LocalDate getByDate() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + Dates.fmt(by) + ")";
    }

    @Override
    public String toDataString() {
        return TaskType.DEADLINE.getLabel() + " | " + (isDone ? "1" : "0")
            + " | " + description + " | " + by;
    }
}
