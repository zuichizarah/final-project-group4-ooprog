import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {
    private String bookingId;
    private Guest guest;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BookingStatus status;
    private PricingStrategy pricingStrategy;
    private double totalCost;

    public Booking(String bookingId, Guest guest, Room room, LocalDate checkIn,
                   LocalDate checkOut, PricingStrategy pricingStrategy) {
        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.pricingStrategy = pricingStrategy;
        this.status = BookingStatus.PENDING;
        calculateTotal();
    }

    public int getNights() {
        return (int) ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    // The Strategy object decides how the total is computed
    private void calculateTotal() {
        totalCost = pricingStrategy.calculatePrice(room.getRatePerNight(), getNights());
    }

    // Changing the dates also recomputes the total cost
    public void setDates(LocalDate checkIn, LocalDate checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        calculateTotal();
    }

    public String getBookingId() {
        return bookingId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
