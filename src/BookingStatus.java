/**
 * The possible statuses of a booking.
 */
public enum BookingStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    CHECKED_IN("Checked-in"),
    CHECKED_OUT("Checked-out"),
    CANCELLED("Cancelled");

    private final String label;

    BookingStatus(String label) {
        this.label = label;
    }

    // A booking is "active" while it still holds a room (not finished or cancelled)
    public boolean isActive() {
        return this == PENDING || this == CONFIRMED || this == CHECKED_IN;
    }

    @Override
    public String toString() {
        return label;
    }
}
