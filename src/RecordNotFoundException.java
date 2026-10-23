/**
 * Thrown when a room, guest, booking or user cannot be found.
 */
public class RecordNotFoundException extends HotelException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
