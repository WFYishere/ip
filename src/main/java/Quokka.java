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
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                printList();
            } else if (input.startsWith("mark ")) {
                int idx = Integer.parseInt(input.substring(5)) - 1;
                markTask(idx);
            } else if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7)) - 1;
                unmarkTask(idx);
            } else {
                addTask(input);
            }
        }
    }

    private static void addTask(String description) {
        if (size < MAX_TASKS) {
            tasks[size++] = new Task(description);
            System.out.println(LINE);
            System.out.println(" added: " + description);
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
        if (idx >= 0 && idx < size) {
            tasks[idx].markAsDone();
            System.out.println(LINE);
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + tasks[idx]);
            System.out.println(LINE);
        }
    }

    private static void unmarkTask(int idx) {
        if (idx >= 0 && idx < size) {
            tasks[idx].markAsNotDone();
            System.out.println(LINE);
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + tasks[idx]);
            System.out.println(LINE);
        }
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