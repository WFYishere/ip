/**
 * Mutable list of tasks. Provides operations to add/remove/get and to search (Level-9).
 */


package quokka;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() { this.tasks = new ArrayList<>(); }
    public TaskList(List<Task> existing) { this.tasks = existing; }
    /** Returns tasks whose description contains the keyword (case-insensitive). */
    public void add(Task... items) {
        if (items == null) {
            return;
        }
        for (Task t : items) {
            if (t != null) {
                tasks.add(t);
            }
        }
    }
    /** Returns tasks whose description contains the keyword (case-insensitive). */
    public Task removeAt(int idx0) { return tasks.remove(idx0); }
    /** Returns tasks whose description contains the keyword (case-insensitive). */
    public Task get(int idx0) { return tasks.get(idx0); }
    /** Returns tasks whose description contains the keyword (case-insensitive). */
    public int size() { return tasks.size(); }
    /** Returns tasks whose description contains the keyword (case-insensitive). */
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
