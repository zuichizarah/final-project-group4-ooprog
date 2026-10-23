/**
 * Thrown when the user enters invalid data or breaks a business rule.
 */
public class InvalidInputException extends HotelException {
    public InvalidInputException(String message) {
        super(message);
    }
}
