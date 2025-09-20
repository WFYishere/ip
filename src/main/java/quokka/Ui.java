/**
 * Handles all user interaction: printing messages and reading commands from stdin.
 */


package quokka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    /** Prints greeting banner. */

    public void showGreeting() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm quokka.Quokka");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    public void showLine() { System.out.println(LINE); }

    public String readCommand() throws IOException {
        return in.readLine();
    }

    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /** Shows one or more error lines (each on its own line). */
    public void showError(String... messages) {
        System.out.println(LINE);
        if (messages != null) {
            for (String m : messages) {
                if (m != null && !m.isEmpty()) {
                    System.out.println(" " + m);
                }
            }
        }
        System.out.println(LINE);
    }

    public void showAdded(Task task, int size) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + size + (size == 1 ? " task" : " tasks") + " in the list.");
        System.out.println(LINE);
    }

    public void showDeleted(Task t, int size) {
        System.out.println(LINE);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + size + (size == 1 ? " task" : " tasks") + " in the list.");
        System.out.println(LINE);
    }

    public void showMark(Task t, boolean done) {
        System.out.println(LINE);
        System.out.println(done ? " Nice! I've marked this task as done:" :
                " OK, I've marked this task as not done yet:");
        System.out.println("   " + t);
        System.out.println(LINE);
    }

    public void showTasks(java.util.List<Task> tasks) {
        System.out.println(LINE);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    public void showFindResults(java.util.List<Task> matches) {
        System.out.println(LINE);
        if (matches.isEmpty()) {
            System.out.println(" No matching tasks found.");
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matches.get(i));
            }
        }
        System.out.println(LINE);
    }
}
