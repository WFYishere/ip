import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Storage {
    private static final String SEP_REGEX = "\\s*\\|\\s*";

    private static String stripBom(String s) {
        if (s != null && !s.isEmpty() && s.charAt(0) == '\uFEFF') {
            return s.substring(1);
        }
        return s;
    }


    private static void ensureParent(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    public static void load(Path file, List<Task> out) {
        try {
            ensureParent(file);
            if (!Files.exists(file)) {
                Files.createFile(file);
                return;
            }
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty()) continue;
                try {
                    Task t = parseLine(line);
                    if (t != null) out.add(t);
                } catch (Exception ex) {
                    System.err.println("Warning: skip corrupted line: \"" + raw + "\"");
                }
            }
        } catch (IOException ioe) {
            System.err.println("Warning: failed to load tasks: " + ioe.getMessage());
        }
    }

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


    private static Task parseLine(String line) throws DukeException {
        String cleaned = stripBom(line.trim());
        String[] parts = cleaned.split(SEP_REGEX);
        if (parts.length < 3) throw new DukeException("Bad line: " + line);
        String tag = parts[0].trim();
        boolean done = parseDone(parts[1].trim());
        String desc = parts[2].trim();
        switch (tag) {
            case "T":
                return new Todo(desc, done);
            case "D":
                if (parts.length < 4) throw new DukeException("Deadline missing /by");
                return new Deadline(desc, parts[3].trim(), done);
            case "E":
                if (parts.length >= 5) {
                    return new Event(desc, parts[3].trim(), parts[4].trim(), done);
                } else if (parts.length == 4) {
                    String single = parts[3].trim();
                    return new Event(desc, single, single, done);
                } else {
                    throw new DukeException("Event missing from/to");
                }
            default:
                throw new DukeException("Unknown type: " + tag);
        }
    }


    private static boolean parseDone(String s) throws DukeException {
        if ("1".equals(s)) return true;
        if ("0".equals(s)) return false;
        throw new DukeException("Invalid done flag: " + s);
    }
}
