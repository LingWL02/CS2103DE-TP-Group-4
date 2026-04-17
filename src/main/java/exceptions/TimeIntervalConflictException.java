package exceptions;

/**
 * Signals that two time intervals overlap in a context that requires uniqueness.
 *
 * <p>This exception is used by trip and activity scheduling logic and surfaced to calling
 * services or UI controllers to prevent invalid timeline updates.</p>
 */
public class TimeIntervalConflictException extends Exception {
    public TimeIntervalConflictException(String message) {
        super(message);
    }
}
