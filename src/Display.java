import java.util.ArrayList;

/**
 * Prints records as simple tables.
 */
public class Display {

    public static void printRooms(ArrayList<Room> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.println("---------------------------------------------");
        System.out.printf("%-8s %-10s %-14s %-12s%n", "Room", "Type", "Rate/Night", "Status");
        System.out.println("---------------------------------------------");
        for (Room r : rooms) {
            System.out.printf("%-8s %-10s PHP %-10.2f %-12s%n",
                    r.getRoomNumber(), r.getType(), r.getRatePerNight(), r.getStatus());
        }
    }

    public static void printGuests(ArrayList<Guest> guests) {
        if (guests.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("%-8s %-25s %-14s %s%n", "ID", "Name", "Phone", "Email");
        System.out.println("-------------------------------------------------------------------------------");
        for (Guest g : guests) {
            System.out.printf("%-8s %-25s %-14s %s%n",
                    g.getGuestId(), g.getName(), g.getPhone(), g.getEmail());
        }
    }

    public static void printBookings(ArrayList<Booking> bookings) {
        if (bookings.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-6s %-25s %-6s %-11s %-11s %-3s %-13s %-12s %-12s%n",
                "ID", "Guest", "Room", "Check-in", "Check-out", "Ngt", "Total", "Status", "Pricing");
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        for (Booking b : bookings) {
            System.out.printf("%-6s %-25s %-6s %-11s %-11s %-3d PHP %-9.2f %-12s %-12s%n",
                    b.getBookingId(), b.getGuest().getName(), b.getRoom().getRoomNumber(),
                    b.getCheckIn(), b.getCheckOut(), b.getNights(), b.getTotalCost(),
                    b.getStatus(), b.getPricingStrategy().getName());
        }
    }

    public static void printReceptionists(ArrayList<Receptionist> list) {
        if (list.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.println("------------------------------------------");
        System.out.printf("%-18s %-22s%n", "Username", "Full Name");
        System.out.println("------------------------------------------");
        for (Receptionist r : list) {
            System.out.printf("%-18s %-22s%n", r.getUsername(), r.getFullName());
        }
    }
}
