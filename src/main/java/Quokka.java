import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Quokka {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;
    private static final String[] tasks = new String[MAX_TASKS];
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
            } else {
                addTask(input);
            }
        }
    }

    private static void addTask(String task) {
        if (size < MAX_TASKS) {
            tasks[size++] = task;
            System.out.println(LINE);
            System.out.println(" added: " + task);
            System.out.println(LINE);
        } else {
            System.out.println(LINE);
            System.out.println(" Sorry, I can only store up to " + MAX_TASKS + " tasks.");
            System.out.println(LINE);
        }
    }

    private static void printList() {
        System.out.println(LINE);
        for (int i = 0; i < size; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
        if (size == 0) {
            System.out.println(" (no tasks yet)");
        }
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
