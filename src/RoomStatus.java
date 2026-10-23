/**
 * The possible statuses of a hotel room.
 */
public enum RoomStatus {
    AVAILABLE("Available"),
    RESERVED("Reserved"),
    OCCUPIED("Occupied"),
    MAINTENANCE("Maintenance");

    private final String label;

    RoomStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
