import java.time.LocalDate;

/**
 * Receptionist: handles guests, bookings, room availability and check-in/out.
 * Cannot manage rooms or user accounts.
 */
public class Receptionist extends User {

    public Receptionist(String username, String password, String fullName) {
        super(username, password, fullName);
    }

    @Override
    public String getRole() {
        return "Receptionist";
    }

    @Override
    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n===== RECEPTIONIST MENU =====");
            System.out.println("1. Manage Guests");
            System.out.println("2. Manage Bookings");
            System.out.println("3. Check Room Availability");
            System.out.println("4. Check-in / Check-out");
            System.out.println("5. Logout");
            int choice = InputHelper.readInt("Enter choice: ", 1, 5);

            switch (choice) {
                case 1:
                    guestMenu();
                    break;
                case 2:
                    bookingMenu();
                    break;
                case 3:
                    checkAvailability();
                    break;
                case 4:
                    checkInOutMenu();
                    break;
                case 5:
                    running = false;
                    break;
            }
        }
    }

    // ===================== GUEST MANAGEMENT =====================

    private void guestMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Guest Management ---");
            System.out.println("1. Add Guest");
            System.out.println("2. View All Guests");
            System.out.println("3. Search Guest");
            System.out.println("4. Update Guest");
            System.out.println("5. Delete Guest");
            System.out.println("6. Back");
            int choice = InputHelper.readInt("Enter choice: ", 1, 6);

            switch (choice) {
                case 1:
                    addGuest();
                    break;
                case 2:
                    Display.printGuests(getHotel().getGuests());
                    break;
                case 3:
                    String keyword = InputHelper.readText("Search (guest ID or name): ");
                    Display.printGuests(getHotel().searchGuests(keyword));
                    break;
                case 4:
                    updateGuest();
                    break;
                case 5:
                    deleteGuest();
                    break;
                case 6:
                    back = true;
                    break;
            }
        }
    }

    private void addGuest() {
        try {
            String id = InputHelper.readGuestId();
            getHotel().checkGuestIdFree(id); // stop early if duplicate
            String name = InputHelper.readName("Full name: ");
            String phone = InputHelper.readPhone();
            String email = InputHelper.readEmail();
            getHotel().addGuest(new Guest(id, name, phone, email));
            System.out.println("Guest " + id + " added successfully.");
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    private void updateGuest() {
        try {
            Guest guest = getHotel().findGuest(InputHelper.readGuestId());
            System.out.println("1. Change name");
            System.out.println("2. Change phone");
            System.out.println("3. Change email");
            int choice = InputHelper.readInt("Enter choice: ", 1, 3);

            switch (choice) {
                case 1:
                    guest.setName(InputHelper.readName("New full name: "));
                    break;
                case 2:
                    guest.setPhone(InputHelper.readPhone());
                    break;
                case 3:
                    guest.setEmail(InputHelper.readEmail());
                    break;
            }
            System.out.println("Guest updated.");
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    private void deleteGuest() {
        try {
            String id = InputHelper.readGuestId();
            getHotel().findGuest(id);
            if (InputHelper.readYesNo("Delete guest " + id + "?")) {
                getHotel().deleteGuest(id);
                System.out.println("Guest deleted.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    // ===================== BOOKING MANAGEMENT =====================

    private void bookingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Booking Management ---");
            System.out.println("1. Create Booking");
            System.out.println("2. View All Bookings");
            System.out.println("3. Search Booking by ID");
            System.out.println("4. Update Booking Dates");
            System.out.println("5. Confirm Booking");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Back");
            int choice = InputHelper.readInt("Enter choice: ", 1, 7);

            switch (choice) {
                case 1:
                    createBooking();
                    break;
                case 2:
                    Display.printBookings(getHotel().getBookings());
                    break;
                case 3:
                    searchBooking();
                    break;
                case 4:
                    updateBooking();
                    break;
                case 5:
                    confirmBooking();
                    break;
                case 6:
                    cancelBooking();
                    break;
                case 7:
                    back = true;
                    break;
            }
        }
    }

    private PricingStrategy choosePricing() {
        System.out.println("Pricing options:");
        System.out.println("  1. Standard rate");
        System.out.println("  2. Peak season (+25%)");
        System.out.println("  3. Long stay (10% off for 5+ nights)");
        int choice = InputHelper.readInt("Choose pricing: ", 1, 3);

        // Polymorphism: all three are used through the same PricingStrategy type
        PricingStrategy strategy;
        if (choice == 1) {
            strategy = new StandardPricing();
        } else if (choice == 2) {
            strategy = new PeakSeasonPricing();
        } else {
            strategy = new LongStayPricing();
        }
        return strategy;
    }

    private void createBooking() {
        try {
            String guestId = InputHelper.readGuestId();
            getHotel().findGuest(guestId); // guest must exist first

            LocalDate checkIn = InputHelper.readDate("Check-in date (yyyy-MM-dd): ");
            LocalDate checkOut = InputHelper.readDate("Check-out date (yyyy-MM-dd): ");
            getHotel().validateDates(checkIn, checkOut);

            System.out.println("\nRooms available for those dates:");
            Display.printRooms(getHotel().getAvailableRooms(checkIn, checkOut));
            if (getHotel().getAvailableRooms(checkIn, checkOut).isEmpty()) {
                throw new RoomUnavailableException("No rooms are available for those dates.");
            }

            String roomNumber = InputHelper.readRoomNumber();
            getHotel().checkRoomBookable(roomNumber, checkIn, checkOut);
            PricingStrategy strategy = choosePricing();

            Booking b = getHotel().createBooking(guestId, roomNumber, checkIn, checkOut, strategy);
            System.out.println("\nBooking created! (Status: " + b.getStatus() + ")");
            System.out.println("Booking ID : " + b.getBookingId());
            System.out.println("Nights     : " + b.getNights());
            System.out.printf("Total cost : PHP %.2f (%s pricing)%n", b.getTotalCost(), strategy.getName());
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    private void searchBooking() {
        try {
            Booking b = getHotel().findBooking(InputHelper.readBookingId());
            java.util.ArrayList<Booking> result = new java.util.ArrayList<>();
            result.add(b);
            Display.printBookings(result);
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    private void updateBooking() {
        try {
            String id = InputHelper.readBookingId();
            getHotel().findBooking(id);
            LocalDate checkIn = InputHelper.readDate("New check-in date (yyyy-MM-dd): ");
            LocalDate checkOut = InputHelper.readDate("New check-out date (yyyy-MM-dd): ");
            getHotel().updateBookingDates(id, checkIn, checkOut);
            System.out.printf("Booking updated. New total: PHP %.2f%n", getHotel().findBooking(id).getTotalCost());
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    private void confirmBooking() {
        try {
            getHotel().confirmBooking(InputHelper.readBookingId());
            System.out.println("Booking confirmed.");
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    private void cancelBooking() {
        try {
            String id = InputHelper.readBookingId();
            getHotel().findBooking(id);
            if (InputHelper.readYesNo("Cancel booking " + id + "?")) {
                getHotel().cancelBooking(id);
                System.out.println("Booking cancelled.");
            } else {
                System.out.println("Cancellation aborted.");
            }
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    // ===================== ROOM AVAILABILITY =====================

    private void checkAvailability() {
        try {
            System.out.println("\nCurrent room status:");
            Display.printRooms(getHotel().getRooms());

            LocalDate checkIn = InputHelper.readDate("\nCheck-in date (yyyy-MM-dd): ");
            LocalDate checkOut = InputHelper.readDate("Check-out date (yyyy-MM-dd): ");
            getHotel().validateDates(checkIn, checkOut);

            System.out.println("\nRooms available from " + checkIn + " to " + checkOut + ":");
            Display.printRooms(getHotel().getAvailableRooms(checkIn, checkOut));
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    // ===================== CHECK-IN / CHECK-OUT =====================

    private void checkInOutMenu() {
        System.out.println("\n--- Check-in / Check-out ---");
        System.out.println("1. Check-in a guest");
        System.out.println("2. Check-out a guest");
        System.out.println("3. Back");
        int choice = InputHelper.readInt("Enter choice: ", 1, 3);

        try {
            if (choice == 1) {
                String id = InputHelper.readBookingId();
                getHotel().checkIn(id);
                System.out.println("Guest checked in. Room is now Occupied.");
            } else if (choice == 2) {
                String id = InputHelper.readBookingId();
                getHotel().checkOut(id);
                System.out.printf("Guest checked out. Total bill: PHP %.2f%n",
                        getHotel().findBooking(id).getTotalCost());
            }
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }
}
