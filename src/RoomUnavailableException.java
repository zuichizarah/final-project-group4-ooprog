/**
 * Thrown when a room cannot be booked (maintenance or already reserved).
 */
public class RoomUnavailableException extends HotelException {
    public RoomUnavailableException(String message) {
        super(message);
    }
}
