package quokka;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() { this.tasks = new ArrayList<>(); }
    public TaskList(List<Task> existing) { this.tasks = existing; }

    public void add(Task t) { tasks.add(t); }
    public Task removeAt(int idx0) { return tasks.remove(idx0); }
    public Task get(int idx0) { return tasks.get(idx0); }
    public int size() { return tasks.size(); }
    public List<Task> view() { return tasks; }

    public List<Task> find(String keyword) {
        String kw = keyword.toLowerCase();
        List<Task> out = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getDescription().toLowerCase().contains(kw)) {
                out.add(t);
            }
        }
        return out;
    }
}
