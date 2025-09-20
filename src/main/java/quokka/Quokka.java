package quokka;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import quokka.Reply;


/**
 * Entry point and command dispatcher for the Quokka chatbot.
 * <p>
 * Wires together Ui, TaskList, and Storage, and exposes a process(String) method
 * that converts a raw user input into a formatted reply (with error/exit flags)
 * without printing. Also provides a simple CLI run() loop for text mode.
 */
public class Quokka {

    private final Ui ui;
    private final TaskList taskList;
    private final Path dataFile;

    /** Creates a bot backed by data/tasks.txt. */
    public Quokka() {
        this(Paths.get("data", "tasks.txt"));
    }

    /** Creates a bot backed by the given data file (relative or absolute). */
    public Quokka(String filePath) {
        this(Paths.get(filePath));
    }

    private Quokka(Path dataFile) {
        this.ui = new Ui();
        this.taskList = new TaskList();
        this.dataFile = dataFile;

        // Load tasks, creating folders/files if missing. Skip malformed lines.
        Storage.load(this.dataFile, this.taskList.view());
    }

    /**
     * Main CLI loop (text UI).
     * Prints the greeting, then processes lines until "bye".
     */
    public void run() {
        printGreeting();
        while (true) {
            String line = ui.readCommand();
            if (line == null) { // EOF behaves like bye
                ui.showGoodbye();
                break;
            }
            Reply r = process(line);
            if (r.error) {
                ui.showError(r.message);
            } else if (!r.message.isEmpty()) {
                System.out.println(r.message);
            }
            if (r.exit) {
                ui.showGoodbye();
                break;
            }
        }
    }

    /** Processes one command and returns a reply (no printing). */
    public Reply process(String fullCommand) {
        try {
            String cmd = Parser.commandWord(fullCommand);
            String rem = Parser.remainder(fullCommand);

            switch (cmd) {
                case "":
                    return Reply.ok(""); // ignore pure whitespace
                case "list": {
                    return Reply.ok(renderTaskList(taskList.view(), "Here are the tasks in your list:"));
                }
                case "todo": {
                    if (rem.isBlank()) {
                        return Reply.error(ui.showErrorString("The description of a todo cannot be empty."));
                    }
                    Task t = new Todo(rem);
                    taskList.add(t);
                    Storage.save(dataFile, taskList.view());
                    return Reply.ok(formatAdded(t, taskList.size()));
                }
                case "deadline": {
                    if (rem.isBlank()) {
                        return Reply.error(ui.showErrorString("The description of a todo cannot be empty."));
                    }
                    int sep = rem.indexOf(" /by ");
                    if (sep < 0) {
                        return Reply.error(ui.showErrorString("Missing '/by' in deadline."));
                    }
                    String desc = rem.substring(0, sep).trim();
                    String by = rem.substring(sep + 5).trim();
                    if (desc.isEmpty() || by.isEmpty()) {
                        return Reply.error(ui.showErrorString("Use: deadline <desc> /by <when>"));
                    }
                    Task t = new Deadline(desc, by); // Dates parsed inside Deadline
                    taskList.add(t);
                    Storage.save(dataFile, taskList.view());
                    return Reply.ok(formatAdded(t, taskList.size()));
                }
                case "event": {
                    if (rem.isBlank()) {
                        return Reply.error(ui.showErrorString("OOPS!!! The description of an event cannot be empty."));
                    }
                    int f = rem.indexOf(" /from ");
                    int tIdx = rem.indexOf(" /to ");
                    if (f < 0 || tIdx < 0 || tIdx <= f) {
                        return Reply.error(ui.showErrorString("Use: event <desc> /from <start> /to <end>"));
                    }
                    String desc = rem.substring(0, f).trim();
                    String from = rem.substring(f + 7, tIdx).trim();
                    String to = rem.substring(tIdx + 5).trim();
                    if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        return Reply.error(ui.showErrorString("Use: event <desc> /from <start> /to <end>"));
                    }
                    Task t = new Event(desc, from, to); // Dates parsed inside Event
                    taskList.add(t);
                    Storage.save(dataFile, taskList.view());
                    return Reply.ok(formatAdded(t, taskList.size()));
                }
                case "mark": {
                    int idx0 = parseOneBasedIndex(rem);
                    Task t = taskList.get(idx0);
                    t.markAsDone();
                    Storage.save(dataFile, taskList.view());
                    return Reply.ok("Nice! I've marked this task as done:\n  " + t);
                }
                case "unmark": {
                    int idx0 = parseOneBasedIndex(rem);
                    Task t = taskList.get(idx0);
                    t.markAsNotDone();
                    Storage.save(dataFile, taskList.view());
                    return Reply.ok("OK, I've marked this task as not done yet:\n  " + t);
                }
                case "delete": {
                    int idx0 = parseOneBasedIndex(rem);
                    Task removed = taskList.removeAt(idx0);
                    Storage.save(dataFile, taskList.view());
                    return Reply.ok("Noted. I've removed this task:\n  " + removed
                        + "\nNow you have " + taskList.size() + " tasks in the list.");
                }
                case "find": {
                    if (rem.isBlank()) {
                        return Reply.error(ui.showErrorString("Provide a keyword to find."));
                    }
                    List<Task> matches = taskList.find(rem);
                    return Reply.ok(renderTaskList(matches, "Here are the matching tasks in your list:"));
                }
                case "bye":
                    return Reply.ok("Bye. Hope to see you again soon!").withExit();
                default:
                    return Reply.error(ui.showUnknownCommandError(cmd));
            }
        } catch (DukeException e) {
            return Reply.error(ui.showErrorString(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return Reply.error(ui.showErrorString(e.getMessage()));
        } catch (Exception e) {
            return Reply.error(ui.showErrorString(e.getClass().getSimpleName() + ": " + e.getMessage()));
        }
    }

    /* ===================== helpers ===================== */

    private void printGreeting() {
        String line = "____________________________________________________________";
        System.out.println(line);
        System.out.println(" Hello! I'm Quokka");
        System.out.println(" What can I do for you?");
        System.out.println(line);
    }

    private static String renderTaskList(List<Task> list, String header) {
        if (list == null || list.isEmpty()) {
            return "Your list is empty.";
        }
        StringBuilder sb = new StringBuilder(header).append('\n');
        for (int i = 0; i < list.size(); i++) {
            sb.append(i + 1).append('.').append(list.get(i)).append('\n');
        }
        return sb.toString().trim();
    }

    private static String formatAdded(Task t, int newSize) {
        return "Got it. I've added this task:\n  " + t + "\nNow you have " + newSize + " tasks in the list.";
    }

    private int parseOneBasedIndex(String numStr) throws DukeException {
        if (numStr == null || numStr.trim().isEmpty()) {
            throw new DukeException("Please provide a task number.");
        }
        try {
            int n = Integer.parseInt(numStr.trim());
            if (n < 1 || n > taskList.size()) {
                throw new DukeException("Index out of range. Provide a number between 1 and " + taskList.size() + ".");
            }
            return n - 1; // convert to 0-based
        } catch (NumberFormatException e) {
            throw new DukeException("Please provide a valid task number.");
        }
    }
}
