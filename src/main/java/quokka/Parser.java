/**
 * Lightweight parser that splits the command word and the remainder.
 */
package quokka;

public class Parser {
    public static String commandWord(String input) {
        if (input == null) {
            return "";
        }
        String s = input.trim();
        int sp = s.indexOf(' ');
        String result = (sp == -1) ? s : s.substring(0, sp);

        assert result != null : "Parser.commandWord must not return null";
        assert result.indexOf(' ') == -1 : "commandWord should contain no spaces";
        return result;
    }

    public static String remainder(String input) {
        if (input == null) {
            return "";
        }
        String s = input.trim();
        int sp = s.indexOf(' ');
        String result = (sp == -1) ? "" : s.substring(sp + 1).trim();

        assert result != null : "Parser.remainder must not return null";
        assert result.equals(result.trim()) : "remainder should be trimmed";
        return result;
    }
}
