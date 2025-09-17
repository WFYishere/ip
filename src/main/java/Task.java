public class Task {
    protected String description;
    protected boolean isDone;
    protected final TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
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

    @Override
    public String toString() {
        return "[" + type.getLabel() + "][" + getStatusIcon() + "] " + description;
    }
}
