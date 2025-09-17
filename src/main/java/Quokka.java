import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Quokka {
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) throws IOException {
        printGreeting();
        runEchoLoop();
        printGoodbye();
    }

    private static void printGreeting() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm Quokka");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    private static void runEchoLoop() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = br.readLine()) != null) {
            if (input.equals("bye")) {
                break;
            }
            printEcho(input);
        }
    }

    private static void printEcho(String message) {
        System.out.println(LINE);
        System.out.println(" " + message);
        System.out.println(LINE);
    }

    private static void printGoodbye() {
        System.out.println(LINE);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
