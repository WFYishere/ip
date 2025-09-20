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

    /** Case-insensitive search by keyword (non-destructive). */
    public java.util.List<Task> findByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return java.util.List.of();
        }
        final String k = keyword.toLowerCase();
        return tasks.stream()
            .filter(t -> t.getDescription().toLowerCase().contains(k))
            .collect(java.util.stream.Collectors.toList());
    }

    /** Count how many tasks are marked done. */
    public long countDone() {
        return tasks.stream().filter(Task::isDone).count();
    }

    public boolean containsDuplicate(Task candidate) {
        String keyB = dupKey(candidate);
        for (Task t : tasks) {
            if (dupKey(t).equals(keyB)) return true;
        }
        return false;
    }

    private static String dupKey(Task t) {
        String[] p = t.toDataString().split("\\s*\\|\\s*");
        if (p.length < 3) {
            return t.toDataString().trim();
        }
        String type = p[0].trim();
        String desc = p[2].trim().toLowerCase();

        switch (type) {
            case "T":
                return "T|" + desc;
            case "D": {
                String by = (p.length >= 4) ? p[3].trim() : "";
                return "D|" + desc + "|" + by;
            }
            case "E": {
                String from = (p.length >= 4) ? p[3].trim() : "";
                String to   = (p.length >= 5) ? p[4].trim() : "";
                return "E|" + desc + "|" + from + "|" + to;
            }
            default:
                StringBuilder sb = new StringBuilder(type).append('|').append(desc);
                for (int i = 3; i < p.length; i++) sb.append('|').append(p[i].trim());
                return sb.toString();
        }
    }

}
