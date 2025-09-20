package quokka;

/**
 * Small DTO passed from backend to GUI.
 * Encapsulates the reply text, an error flag for styling, and an exit flag for closing the app.
 */
public class Reply {
    public final String message;
    public final boolean error;
    public final boolean exit;

    private Reply(String message, boolean error, boolean exit) {
        this.message = message;
        this.error = error;
        this.exit = exit;
    }

    public static Reply ok(String message) {
        return new Reply(message, false, false);
    }

    public static Reply error(String message) {
        return new Reply(message, true, false);
    }

    public Reply withExit() {
        return new Reply(this.message, this.error, true);
    }
}
