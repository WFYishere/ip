package quokka;

import java.util.List;
import java.util.Scanner;

/**
 * Handles user interaction concerns.
 */
public class Ui {

    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner in = new Scanner(System.in);

    /* ===================== CLI helpers ===================== */

    /** Reads one line from stdin. Returns null on EOF. */
    public String readCommand() {
        try {
            if (in.hasNextLine()) {
                return in.nextLine();
            }
            return null; // EOF
        } catch (Exception e) {
            return null;
        }
    }

    /** Prints a friendly goodbye (CLI). GUI uses the string-returning variant instead. */
    public void showGoodbye() {
        System.out.println(DIVIDER);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }

    /** Prints an error block to the console (CLI). In GUI, pass the same message into a bot bubble. */
    public void showError(String message) {
        System.out.println(DIVIDER);
        System.out.println(" " + message);
        System.out.println(DIVIDER);
    }

    /* ===================== String-returning helpers (GUI or CLI formatting) ===================== */

    public String getWelcomeMessage() {
        return "Greetings, wanderer. I am the Chronicler of Quokka Hollow.\n" +
            "What tale shall we inscribe today?";
    }

    public String byeMessage() {
        return "Farewell, brave one. May the path ahead be lit, even in shadow.";
    }

    /** Generic "unknown command" error string. */
    public String showUnknownCommandError(String cmd) {
        return "Alas… I do not know this incantation: " + safe(cmd);
    }

    public String showErrorString(String message) {
        return "The Hollow stirs uneasily: " + safe(message);
    }


    /** Formats a full task list. */
    public String showTaskList(List<Task> list) {
        if (list == null || list.isEmpty()) {
            return "Your list is empty.";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append(i + 1).append('.').append(list.get(i)).append('\n');
        }
        return rstrip(sb);
    }

    /** Formats matching tasks (for 'find'). */
    public String showMatchingTasks(List<Task> list) {
        if (list == null || list.isEmpty()) {
            return "No matching tasks found.";
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append(i + 1).append('.').append(list.get(i)).append('\n');
        }
        return rstrip(sb);
    }

    public String formatAdded(Task t, int newSize) {
        return "A new entry etched into the Chronicle:\n  " + t +
            "\nYou now carry " + newSize + " burdens.";
    }

    public String formatMarked(Task t) {
        return "The deed is done, etched with certainty:\n  " + t;
    }

    public String formatUnmarked(Task t) {
        return "The ink fades… the task remains unfinished:\n  " + t;
    }

    public String formatDeleted(Task t, int newSize) {
        return "A memory erased from the Chronicle:\n  " + t +
            "\nOnly " + newSize + " tales remain.";
    }


    /* ===================== small utilities ===================== */
    private static String rstrip(StringBuilder sb) {
        int len = sb.length();
        while (len > 0 && Character.isWhitespace(sb.charAt(len - 1))) {
            len--;
        }
        return sb.substring(0, len);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
