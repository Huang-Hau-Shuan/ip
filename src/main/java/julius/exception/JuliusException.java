package julius.exception;

/**
 * Represents a known, user-facing error within the Julius application.
 * <p>
 * Thrown when user input is invalid or an operation cannot be completed
 * due to a foreseeable condition (e.g. missing task description, out-of-range
 * task number). The message is displayed directly to the user via {@link julius.ui.Ui}.
 * </p>
 */
public class JuliusException extends Exception {

    /**
     * Creates a JuliusException with the given user-facing error message.
     *
     * @param message description of the error, suitable for display to the user
     */
    public JuliusException(String message) {
        super(message);
    }
}
