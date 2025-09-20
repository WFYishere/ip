/** A concrete task type. */

package quokka;

import java.time.LocalDate;
import quokka.util.Dates;



public class Event extends Task {
    protected LocalDate from;
    protected LocalDate to;

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = Dates.parseFlexibleDate(from);
        this.to   = Dates.parseFlexibleDate(to);
        assert this.from != null && this.to != null : "Event dates must parse";
        assert !this.from.isAfter(this.to) : "Event: start date must be <= end date";
    }

    public Event(String description, String from, String to, boolean isDone) {
        super(description, TaskType.EVENT, isDone);
        this.from = Dates.parseFlexibleDate(from);
        this.to   = Dates.parseFlexibleDate(to);
        assert this.from != null && this.to != null : "Event dates must parse";
        assert !this.from.isAfter(this.to) : "Event: start date must be <= end date";
    }


    @Override
    public String toString() {
        return super.toString() + " (from: " + Dates.fmt(from) + " to: " + Dates.fmt(to) + ")";
    }


    @Override
    public String toDataString() {
        return TaskType.EVENT.getLabel() + " | " + (isDone ? "1" : "0")
            + " | " + description + " | " + from + " | " + to;
    }
}
