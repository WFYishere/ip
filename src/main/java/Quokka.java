import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Quokka {
    private static final String LINE = "____________________________________________________________";
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        printGreeting();
        runLoop();
        printGoodbye();
    }

    private static void runLoop() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = br.readLine()) != null) {
            try {
                input = input.trim();
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    printList();
                } else if (input.startsWith("mark ")) {
                    int idx = parseIndex(input.substring(5));
                    if (idx < 0 || idx >= tasks.size()) {
                        throw new DukeException("No such task to mark.");
                    }
                    markTask(idx);
                } else if (input.startsWith("unmark ")) {
                    int idx = parseIndex(input.substring(7));
                    if (idx < 0 || idx >= tasks.size()) {
                        throw new DukeException("No such task to unmark.");
                    }
                    unmarkTask(idx);
                } else if (input.startsWith("delete ")) {
                    int idx = parseIndex(input.substring(7));
                    if (idx < 0 || idx >= tasks.size()) {
                        throw new DukeException("No such task to delete.");
                    }
                    deleteTask(idx);
                } else if (input.startsWith("todo")) {
                    String desc = input.length() > 4 ? input.substring(5).trim() : "";
                    if (desc.isEmpty()) {
                        throw new DukeException("The description of a todo cannot be empty.");
                    }
                    addTask(new Todo(desc));
                } else if (input.startsWith("deadline")) {
                    String body = input.length() > 8 ? input.substring(9).trim() : "";
                    if (body.isEmpty()) {
                        throw new DukeException("The description of a deadline cannot be empty.");
                    }
                    int byPos = body.indexOf(" /by ");
                    if (byPos == -1) {
                        throw new DukeException("A deadline must have '/by <time>'!");
                    }
                    String desc = body.substring(0, byPos).trim();
                    String by = body.substring(byPos + 5).trim();
                    addTask(new Deadline(desc, by));
                } else if (input.startsWith("event")) {
                    String body = input.length() > 5 ? input.substring(6).trim() : "";
                    if (body.isEmpty()) {
                        throw new DukeException("The description of an event cannot be empty.");
                    }
                    String fromToken = " /from ";
                    String toToken = " /to ";
                    int fromPos = body.indexOf(fromToken);
                    int toPos = body.indexOf(toToken);
                    if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                        throw new DukeException("An event must have '/from <time> /to <time>'!");
                    }
                    String desc = body.substring(0, fromPos).trim();
                    String from = body.substring(fromPos + fromToken.length(), toPos).trim();
                    String to = body.substring(toPos + toToken.length()).trim();
                    addTask(new Event(desc, from, to));
                } else if (!input.isEmpty()) {
                    throw new DukeException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (DukeException e) {
                printError(e.getMessage());
            }
        }
    }

    private static int parseIndex(String s) {
        try {
            return Integer.parseInt(s.trim()) - 1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addTask(Task task) {
        tasks.add(task);
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tasks.size()
                + (tasks.size() == 1 ? " task" : " tasks") + " in the list.");
        System.out.println(LINE);
    }

    private static void deleteTask(int idx) {
        Task removed = tasks.remove(idx);
        System.out.println(LINE);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + removed);
        System.out.println(" Now you have " + tasks.size()
                + (tasks.size() == 1 ? " task" : " tasks") + " in the list.");
        System.out.println(LINE);
    }

    private static void printList() {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    private static void markTask(int idx) {
        Task t = tasks.get(idx);
        t.markAsDone();
        System.out.println(LINE);
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + t);
        System.out.println(LINE);
    }

    private static void unmarkTask(int idx) {
        Task t = tasks.get(idx);
        t.markAsNotDone();
        System.out.println(LINE);
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + t);
        System.out.println(LINE);
    }

    private static void printError(String message) {
        System.out.println(LINE);
        System.out.println(" OOPS!!! " + message);
        System.out.println(LINE);
    }

    private static void printGreeting() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm Quokka");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    private static void printGoodbye() {
        System.out.println(LINE);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
