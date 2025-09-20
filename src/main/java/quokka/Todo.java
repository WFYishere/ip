/** A concrete task type. */

package quokka;

public class Todo extends Task {
    public Todo(String description) {
        super(description, TaskType.TODO);
    }

    public Todo(String description, boolean isDone) {
        super(description, TaskType.TODO, isDone);
    }

    @Override
    public String toDataString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
