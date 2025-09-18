package quokka;

public class Parser {
    public static String commandWord(String input) {
        if (input == null) return "";
        String s = input.trim();
        int sp = s.indexOf(' ');
        return sp == -1 ? s : s.substring(0, sp);
    }

    public static String remainder(String input) {
        if (input == null) return "";
        String s = input.trim();
        int sp = s.indexOf(' ');
        return sp == -1 ? "" : s.substring(sp + 1).trim();
    }
}
