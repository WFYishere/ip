package quokka.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Small, reusable helpers demonstrating Java Streams and Optional, without changing app behavior.
 * You can adopt these in TaskList, Storage, Find/Filter commands, or UI formatting later.
 */
public final class StreamsHelper {

    private StreamsHelper() { /* no instances */ }

    /**
     * Returns a new list containing items that match the given predicate.
     * Null values in the source collection are ignored.
     */
    public static <T> List<T> filter(Collection<T> items, Predicate<? super T> predicate) {
        Objects.requireNonNull(items, "items");
        Objects.requireNonNull(predicate, "predicate");
        return items.stream()
            .filter(Objects::nonNull)
            .filter(predicate)
            .collect(Collectors.toList());
    }

    /**
     * Case-insensitive "grep" over strings; trims each line and excludes empties.
     */
    public static List<String> grepIgnoreCase(Collection<String> lines, String needle) {
        Objects.requireNonNull(lines, "lines");
        Objects.requireNonNull(needle, "needle");
        final String n = needle.toLowerCase(Locale.ROOT).trim();
        return lines.stream()
            .filter(Objects::nonNull)
            .map(s -> s == null ? "" : s.trim())
            .filter(s -> !s.isEmpty())
            .filter(s -> s.toLowerCase(Locale.ROOT).contains(n))
            .collect(Collectors.toList());
    }

    /**
     * Joins lines with newline separators after trimming; skips null/empty lines.
     */
    public static String joinLines(Collection<String> lines) {
        Objects.requireNonNull(lines, "lines");
        return lines.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * Returns a numbered list view of the given lines:
     * 1. first
     * 2. second
     * 3. third
     */
    public static String toNumberedList(List<String> lines) {
        Objects.requireNonNull(lines, "lines");
        return IntStream.range(0, lines.size())
            .mapToObj(i -> (i + 1) + ". " + safeTrim(lines.get(i)))
            .collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * Finds the first element matching the predicate wrapped in Optional; ignores nulls.
     */
    public static <T> Optional<T> firstMatching(Collection<T> items, Predicate<? super T> predicate) {
        Objects.requireNonNull(items, "items");
        Objects.requireNonNull(predicate, "predicate");
        return items.stream()
            .filter(Objects::nonNull)
            .filter(predicate)
            .findFirst();
    }

    /**
     * Returns a sorted copy of the list using the provided comparator; nulls are placed last.
     */
    public static <T> List<T> sortedCopy(Collection<T> items, Comparator<? super T> comparator) {
        Objects.requireNonNull(items, "items");
        Objects.requireNonNull(comparator, "comparator");
        return items.stream()
            .filter(Objects::nonNull)
            .sorted(Comparator.nullsLast(comparator))
            .collect(Collectors.toList());
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
