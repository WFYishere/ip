public class Quokka {
    public static void main(String[] args) {
        printGreeting();
        printGoodbye();
    }

    private static void printGreeting() {
        String line = "____________________________________________________________";
        System.out.println(line);
        System.out.println(" Hello! I'm Quokka");
        System.out.println(" What can I do for you?");
        System.out.println(line);
    }

    private static void printGoodbye() {
        String line = "____________________________________________________________";
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(line);
    }
}
