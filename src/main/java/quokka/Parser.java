/**
 * Lightweight parser that splits the command word and the remainder.
 */
package quokka;

public class Parser {

    /** Normalize unicode spaces (e.g., NBSP) and trim. */
    private static String normalize(String s) {
        if (s == null) return "";
        String t = s.replace('\u00A0', ' ');
        t = t.trim();
        t = t.replaceAll("\\s+", " ");
        return t;
    }


    public static String commandWord(String input) {
        String s = normalize(input);
        if (s.isEmpty()) return "";
        int sp = s.indexOf(' ');
        String result = (sp == -1) ? s : s.substring(0, sp);
        assert result != null : "Parser.commandWord must not return null";
        assert result.indexOf(' ') == -1 : "commandWord should contain no spaces";
        return result;
    }

    public static String remainder(String input) {
        String s = normalize(input);
        if (s.isEmpty()) return "";
        int sp = s.indexOf(' ');
        String result = (sp == -1) ? "" : s.substring(sp + 1).trim();
        assert result != null : "Parser.remainder must not return null";
        assert result.equals(result.trim()) : "remainder should be trimmed";
        return result;
    }


    /** Returns the number of occurrences of a substring token in text (non-overlapping). */
    public static int countToken(String text, String token) {
        if (text == null || token == null || token.isEmpty()) return 0;
        int count = 0, pos = 0;
        while ((pos = text.indexOf(token, pos)) >= 0) { count++; pos += token.length(); }
        return count;
    }

    /**
     * Split 'source' by the first occurrence of 'token', returning {left,right} trimmed.
     * If token not found, returns {source.trim(), ""}.
     */
    public static String[] splitOnce(String source, String token) {
        String s = normalize(source);
        int i = s.indexOf(token);
        if (i < 0) return new String[]{ s, "" };
        String left = s.substring(0, i).trim();
        String right = s.substring(i + token.length()).trim();
        return new String[]{ left, right };
    }
}
