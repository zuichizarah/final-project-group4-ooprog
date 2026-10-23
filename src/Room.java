public class Room {
    public static final String[] TYPES = {"Single", "Double", "Deluxe", "Suite"};

    private String roomNumber;
    private String type;
    private double ratePerNight;
    private RoomStatus status;

    public Room(String roomNumber, String type, double ratePerNight) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.ratePerNight = ratePerNight;
        this.status = RoomStatus.AVAILABLE;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(double ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
