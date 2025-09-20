/**
 * Persists tasks to a human-editable text file and loads them on startup.
 * Tolerates legacy formats and corrupted lines where possible.
 */
package quokka;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Storage {

    /** Split regex that tolerates spaces around the '|' delimiter. */
    private static final String SPLIT_REGEX = "\\s*\\|\\s*";

    private Storage() {
        // utility class: no instances
    }

    /** Removes a UTF-8 BOM if present (common on Windows editors). */
    private static String stripBom(String s) {
        if (s != null && !s.isEmpty() && s.charAt(0) == '\uFEFF') {
            return s.substring(1);
        }
        return s;
    }

    /** Ensures parent directory exists before file I/O. */
    private static void ensureParent(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    /**
     * Loads tasks into {@code out}. Creates file/dirs if missing; skips malformed lines.
     *
     * @param file path to the data file
     * @param out  list to receive loaded tasks
     */
    public static void load(Path file, List<Task> out) {
        try {
            ensureParent(file);
            if (!Files.exists(file)) {
                Files.createFile(file);
                return;
            }
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            int lineNo = 0;
            for (String raw : lines) {
                lineNo++;
                String line = stripBom(raw).trim();
                if (line.isEmpty()) {
                    continue; // ignore blanks
                }
                try {
                    Task t = parseLine(line);
                    out.add(t);
                } catch (Exception ex) {
                    System.err.println("Warning: skip corrupted line " + lineNo + ": \"" + raw + "\"");
                }
            }
        } catch (IOException ioe) {
            System.err.println("Warning: failed to load tasks: " + ioe.getMessage());
        }
    }

    /**
     * Saves all tasks to disk in a stable, human-editable format using each task's {@code toDataString()}.
     *
     * @param file  path to write
     * @param tasks tasks to persist
     */
    public static void save(Path file, List<Task> tasks) {
        try {
            ensureParent(file);
            try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                for (Task t : tasks) {
                    w.write(t.toDataString());
                    w.newLine();
                }
            }
        } catch (IOException ioe) {
            System.err.println("Warning: failed to save tasks: " + ioe.getMessage());
        }
    }

    /** Parses one persisted line into a concrete {@link Task}. */
    private static Task parseLine(String line) throws DukeException {
        String cleaned = stripBom(line.trim());

        String[] parts = cleaned.split(SPLIT_REGEX, -1);
        if (parts.length < 3) {
            throw new DukeException("Bad line (too few fields): " + line);
        }

        String tag  = parts[0].trim();
        boolean done = parseDone(parts[1].trim());
        String desc = parts[2].trim();

        switch (tag) {
            case "T":
                return new Todo(desc, done);

            case "D": {
                if (parts.length < 4 || parts[3].trim().isEmpty()) {
                    throw new DukeException("Deadline missing /by");
                }
                String by = parts[3].trim();
                return new Deadline(desc, by, done);
            }

            case "E": {
                if (parts.length >= 5 && !parts[3].trim().isEmpty() && !parts[4].trim().isEmpty()) {
                    return new Event(desc, parts[3].trim(), parts[4].trim(), done);
                }
                if (parts.length == 4 && !parts[3].trim().isEmpty()) {
                    String d = parts[3].trim();
                    return new Event(desc, d, d, done);
                }
                throw new DukeException("Event missing from/to");
            }

            default:
                throw new DukeException("Unknown type: " + tag);
        }
    }

    /** Parses the done flag ("1" or "0"). */
    private static boolean parseDone(String s) throws DukeException {
        if ("1".equals(s)) {
            return true;
        }
        if ("0".equals(s)) {
            return false;
        }
        throw new DukeException("Invalid done flag: " + s);
    }
}
