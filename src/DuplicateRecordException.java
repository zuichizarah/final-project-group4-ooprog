/**
 * Thrown when an ID or username already exists.
 */
public class DuplicateRecordException extends HotelException {
    public DuplicateRecordException(String message) {
        super(message);
    }
}
