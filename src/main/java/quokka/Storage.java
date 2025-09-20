/**
 * Persists tasks to a human-editable text file and loads them on startup.
 * Tolerates legacy formats and corrupted lines where possible.
 */
package quokka;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public final class Storage {

    /** Split regex that tolerates spaces around the '|' delimiter. */
    private static final String SPLIT = "\\s*\\|\\s*";

    private Storage() {}

    /** Save all tasks to file atomically (write to temp, then move). */
    public static void save(Path file, List<Task> tasks) throws DukeException {
        try {
            Path parent = file.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            Path tmp = Files.createTempFile(parent != null ? parent : file.getParent(), "quokka-", ".tmp");
            try (BufferedWriter bw = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
                for (Task t : tasks) {
                    bw.write(t.toDataString());
                    bw.newLine();
                }
            }
            // Try atomic move; if not supported, fall back to replace.
            try {
                Files.move(tmp, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new DukeException("Unable to save data: " + e.getMessage());
        }
    }

    /** Load tasks from file; creates the file if absent. Corrupted lines are skipped with warnings. */
    public static void load(Path file, List<Task> out) throws DukeException {
        try {
            Path parent = file.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(file)) {
                Files.createFile(file);
                return;
            }
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            int lineNo = 0;
            for (String raw : lines) {
                lineNo++;
                String line = stripBom(raw).trim();
                if (line.isEmpty()) continue;
                try {
                    Task t = parseLine(line);
                    out.add(t);
                } catch (Exception ex) {
                    System.err.println("Warning: skipped corrupted line " + lineNo + ": \"" + raw + "\" (" + ex.getMessage() + ")");
                }
            }
        } catch (AccessDeniedException e) {
            throw new DukeException("Access denied to data file: " + file.toAbsolutePath());
        } catch (IOException e) {
            throw new DukeException("Unable to load data: " + e.getMessage());
        }
    }

    /** Parse one serialized line into a Task. */
    private static Task parseLine(String line) throws DukeException {
        String[] parts = line.split(SPLIT);
        if (parts.length < 3) throw new DukeException("Too few fields: " + line);

        String type = parts[0].trim();
        boolean done = parseDone(parts[1].trim());
        String desc = parts[2].trim();

        Task t;
        switch (type) {
            case "T":
                t = new Todo(desc);
                break;
            case "D":
                if (parts.length < 4) throw new DukeException("Deadline missing date: " + line);
                t = new Deadline(desc, parts[3].trim());
                break;
            case "E":
                if (parts.length < 5) throw new DukeException("Event missing dates: " + line);
                t = new Event(desc, parts[3].trim(), parts[4].trim());
                break;
            default:
                throw new DukeException("Unknown type: " + type);
        }
        if (done) t.markAsDone();
        return t;
    }

    /** Remove UTF-8 BOM if present. */
    private static String stripBom(String s) {
        if (s != null && !s.isEmpty() && s.charAt(0) == '\uFEFF') {
            return s.substring(1);
        }
        return s;
    }

    /** Parses the done flag ("1" or "0"). */
    private static boolean parseDone(String s) throws DukeException {
        if ("1".equals(s)) return true;
        if ("0".equals(s)) return false;
        throw new DukeException("Invalid done flag: " + s);
    }
}
