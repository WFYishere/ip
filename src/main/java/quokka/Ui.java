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
        return "Hello! I'm Quokka\nWhat can I do for you?";
    }

    /** Bye text used by GUI. */
    public String byeMessage() {
        return "Bye. Hope to see you again soon!";
    }

    /** Generic "unknown command" error string. */
    public String showUnknownCommandError(String cmd) {
        return "OOPS!!! I'm sorry, but I don't know what that means: " + safe(cmd);
    }

    /** Wraps a message with the standard OOPS prefix (string form). */
    public String showErrorString(String message) {
        return "OOPS!!! " + safe(message);
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

    /** Added-task message. */
    public String formatAdded(Task t, int newSize) {
        return "Got it. I've added this task:\n  " + t + "\nNow you have " + newSize + " tasks in the list.";
    }

    /** Marked-done message. */
    public String formatMarked(Task t) {
        return "Nice! I've marked this task as done:\n  " + t;
    }

    /** Marked-undone message. */
    public String formatUnmarked(Task t) {
        return "OK, I've marked this task as not done yet:\n  " + t;
    }

    /** Deleted-task message. */
    public String formatDeleted(Task t, int newSize) {
        return "Noted. I've removed this task:\n  " + t + "\nNow you have " + newSize + " tasks in the list.";
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
