package quokka;

public class Task {
    protected String description;
    protected boolean isDone;
    protected final TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    public Task(String description, TaskType type, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
        this.type = type;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public TaskType getType() {
        return type;
    }

    public void markAsDone() { isDone = true; }
    public void markAsNotDone() { isDone = false; }
    public String getDescription() { return description; }
    public boolean isDone() { return isDone; }

    @Override
    public String toString() {
        return "[" + type.getLabel() + "][" + getStatusIcon() + "] " + description;
    }

    public String toDataString() {
        return type.getLabel() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

}
