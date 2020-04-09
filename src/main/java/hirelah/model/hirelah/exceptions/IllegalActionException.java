package hirelah.model.hirelah.exceptions;

/**
 * Signals that a method was invoked that results in an illegal model state.
 */
public class IllegalActionException extends Exception {
    /**
     * @param message should contain relevant information on the illegal operation attempted.
     */
    public IllegalActionException(String message) {
        super(message);
    }
}
