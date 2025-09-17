import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Quokka {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;
    private static final Task[] tasks = new Task[MAX_TASKS];
    private static int size = 0;

    public static void main(String[] args) throws IOException {
        printGreeting();
        runLoop();
        printGoodbye();
    }

    private static void runLoop() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = br.readLine()) != null) {
            input = input.trim();
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                printList();
            } else if (input.startsWith("mark ")) {
                int idx = parseIndex(input.substring(5));
                if (validIndex(idx)) markTask(idx);
            } else if (input.startsWith("unmark ")) {
                int idx = parseIndex(input.substring(7));
                if (validIndex(idx)) unmarkTask(idx);
            } else if (input.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                addTask(new Todo(desc));
            } else if (input.startsWith("deadline ")) {
                String body = input.substring(9).trim();
                int byPos = body.indexOf(" /by ");
                if (byPos == -1) {
                    addTask(new Deadline(body, "")); // accept raw if /by missing (dates treated as strings)
                } else {
                    String desc = body.substring(0, byPos).trim();
                    String by = body.substring(byPos + 5).trim();
                    addTask(new Deadline(desc, by));
                }
            } else if (input.startsWith("event ")) {
                String body = input.substring(6).trim();
                String fromToken = " /from ";
                String toToken = " /to ";
                int fromPos = body.indexOf(fromToken);
                int toPos = body.indexOf(toToken);
                if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                    addTask(new Event(body, "", ""));
                } else {
                    String desc = body.substring(0, fromPos).trim();
                    String from = body.substring(fromPos + fromToken.length(), toPos).trim();
                    String to = body.substring(toPos + toToken.length()).trim();
                    addTask(new Event(desc, from, to));
                }
            } else if (!input.isEmpty()) {
                addTask(new Todo(input));
            }
        }
    }

    private static int parseIndex(String s) {
        try {
            return Integer.parseInt(s.trim()) - 1; // user is 1-based
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static boolean validIndex(int idx) {
        return idx >= 0 && idx < size;
    }

    private static void addTask(Task task) {
        if (size < MAX_TASKS) {
            tasks[size++] = task;
            System.out.println(LINE);
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + task);
            System.out.println(" Now you have " + size + (size == 1 ? " task" : " tasks") + " in the list.");
            System.out.println(LINE);
        } else {
            System.out.println(LINE);
            System.out.println(" Sorry, I can only store up to " + MAX_TASKS + " tasks.");
            System.out.println(LINE);
        }
    }

    private static void printList() {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < size; i++) {
            System.out.println(" " + (i + 1) + "." + tasks[i]);
        }
        System.out.println(LINE);
    }

    private static void markTask(int idx) {
        tasks[idx].markAsDone();
        System.out.println(LINE);
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + tasks[idx]);
        System.out.println(LINE);
    }

    private static void unmarkTask(int idx) {
        tasks[idx].markAsNotDone();
        System.out.println(LINE);
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + tasks[idx]);
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