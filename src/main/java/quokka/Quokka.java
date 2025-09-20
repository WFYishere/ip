package quokka;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;

public class Quokka {

    private static final Path SAVE_PATH = Paths.get("data", "duke.txt");

    private static final TaskList taskList = new TaskList(new ArrayList<>());
    private static final Ui ui = new Ui();

    public static void main(String[] args) throws IOException {
        Storage.load(SAVE_PATH, taskList.view());
        ui.showGreeting();
        runLoop();
        ui.showGoodbye();
    }

    // === Main loop ===
    private static void runLoop() throws IOException {
        boolean isExit = false;
        while (!isExit) {
            String line = ui.readCommand();
            if (line == null) break; // EOF
            String cmd = Parser.commandWord(line).toLowerCase();
            String rest = Parser.remainder(line);

            try {
                switch (cmd) {
                    case "bye":
                        isExit = true;
                        break;

                    case "list":
                        ui.showTasks(taskList.view());
                        break;

                    case "todo": {
                        if (rest.isEmpty()) throw new DukeException("☹ OOPS!!! The description of a todo cannot be empty.");
                        Task t = new Todo(rest);
                        taskList.add(t);
                        ui.showAdded(t, taskList.size());
                        Storage.save(SAVE_PATH, taskList.view());
                        break;
                    }

                    case "deadline": {
                        String[] pair = splitOnceFlexible(rest, "/by");
                        if (pair == null || pair[0].isEmpty() || pair[1].isEmpty()) {
                            throw new DukeException("Usage: deadline <desc> /by <yyyy-MM-dd>  (e.g., deadline return book /by 2019-10-15)");
                        }
                        String desc = pair[0];
                        String by = pair[1];
                        Task t = new Deadline(desc, by);
                        taskList.add(t);
                        ui.showAdded(t, taskList.size());
                        Storage.save(SAVE_PATH, taskList.view());
                        break;
                    }

                    case "event": {
                        // event <desc> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
                        String[] fromSplit = splitOnceFlexible(rest, "/from");
                        if (fromSplit == null || fromSplit[0].isEmpty() || fromSplit[1].isEmpty()) {
                            throw new DukeException("Usage: event <desc> /from <yyyy-MM-dd> /to <yyyy-MM-dd>");
                        }
                        String desc = fromSplit[0];
                        String afterFrom = fromSplit[1];

                        String[] toSplit = splitOnceFlexible(afterFrom, "/to");
                        if (toSplit == null || toSplit[0].isEmpty() || toSplit[1].isEmpty()) {
                            throw new DukeException("Usage: event <desc> /from <yyyy-MM-dd> /to <yyyy-MM-dd>");
                        }
                        String from = toSplit[0];
                        String to = toSplit[1];

                        Task t = new Event(desc, from, to);
                        taskList.add(t);
                        ui.showAdded(t, taskList.size());
                        Storage.save(SAVE_PATH, taskList.view());
                        break;
                    }

                    case "delete": {
                        if (rest.isEmpty()) throw new DukeException("Usage: delete <task-number>");
                        int idx0 = parseOneBasedIndex(rest);
                        Task removed = taskList.removeAt(idx0);
                        ui.showDeleted(removed, taskList.size());
                        Storage.save(SAVE_PATH, taskList.view());
                        break;
                    }

                    case "mark": {
                        if (rest.isEmpty()) throw new DukeException("Usage: mark <task-number>");
                        int idx0 = parseOneBasedIndex(rest);
                        Task t = taskList.get(idx0);
                        t.markAsDone();
                        ui.showMark(t, true);
                        Storage.save(SAVE_PATH, taskList.view());
                        break;
                    }

                    case "unmark": {
                        if (rest.isEmpty()) throw new DukeException("Usage: unmark <task-number>");
                        int idx0 = parseOneBasedIndex(rest);
                        Task t = taskList.get(idx0);
                        t.markAsNotDone();
                        ui.showMark(t, false);
                        Storage.save(SAVE_PATH, taskList.view());
                        break;
                    }

                    case "help":
                        ui.showError("Commands: todo, deadline, event, list, delete N, mark N, unmark N, bye");
                        break;

                    case "find": {
                        if (rest.isEmpty()) throw new DukeException("Usage: find <keyword>");
                        java.util.List<Task> matches = taskList.find(rest);
                        ui.showFindResults(matches);
                        break;
                    }

                    default:
                        ui.showError("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (DukeException | IllegalArgumentException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    // === Helpers ===

    private static String[] splitOnceFlexible(String input, String key) {
        if (input == null) return null;
        String s = input.trim();
        String token = " " + key + " ";
        int i = s.indexOf(token);
        if (i < 0) {
            // try variants without trailing space
            token = " " + key;
            i = s.indexOf(token);
            if (i < 0) return null;
            String left = s.substring(0, i).trim();
            String right = s.substring(i + token.length()).trim();
            return new String[]{left, right};
        } else {
            String left = s.substring(0, i).trim();
            String right = s.substring(i + (" " + key + " ").length()).trim();
            return new String[]{left, right};
        }
    }

    private static int parseOneBasedIndex(String numStr) throws DukeException {
        try {
            int n = Integer.parseInt(numStr.trim());
            if (n < 1 || n > taskList.size()) {
                throw new DukeException("Invalid task number: " + n);
            }
            return n - 1;
        } catch (NumberFormatException e) {
            throw new DukeException("Please provide a valid task number.");
        }
    }
}
