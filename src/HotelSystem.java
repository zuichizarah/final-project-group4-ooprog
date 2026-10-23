import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Singleton: only ONE HotelSystem exists, and it stores all hotel data
 * (users, rooms, guests, bookings) and applies the business rules.
 */
public class HotelSystem {
    private static HotelSystem instance;

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Guest> guests = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    private int bookingCounter = 1;

    // Private constructor: other classes cannot create another HotelSystem
    private HotelSystem() {
        loadSampleData();
    }

    public static HotelSystem getInstance() {
        if (instance == null) {
            instance = new HotelSystem();
        }
        return instance;
    }

    private void loadSampleData() {
        users.add(new Admin("admin", "admin123", "Hotel Administrator"));
        users.add(new Receptionist("reception1", "recep123", "Maria Santos"));

        rooms.add(new Room("101", "Single", 1500));
        rooms.add(new Room("102", "Single", 1500));
        rooms.add(new Room("201", "Double", 2500));
        rooms.add(new Room("202", "Deluxe", 3500));
        rooms.add(new Room("301", "Suite", 5000));

        guests.add(new Guest("G001", "Juan Dela Cruz", "09171234567", "juan@email.com"));
        guests.add(new Guest("G002", "Ana Reyes", "09281234567", "ana@email.com"));
    }

    // ===================== LOGIN =====================

    public User login(String username, String password) {
        for (User u : users) {
            if (u.checkCredentials(username, password)) {
                return u;
            }
        }
        return null;
    }

    // ===================== RECEPTIONIST ACCOUNTS =====================

    public void checkUsernameFree(String username) throws DuplicateRecordException {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                throw new DuplicateRecordException("Username '" + username + "' is already taken.");
            }
        }
    }

    public void addReceptionist(Receptionist receptionist) throws DuplicateRecordException {
        checkUsernameFree(receptionist.getUsername());
        users.add(receptionist);
    }

    public Receptionist findReceptionist(String username) throws RecordNotFoundException {
        for (User u : users) {
            if (u instanceof Receptionist && u.getUsername().equalsIgnoreCase(username)) {
                return (Receptionist) u;
            }
        }
        throw new RecordNotFoundException("Receptionist '" + username + "' was not found.");
    }

    public void deleteReceptionist(String username) throws RecordNotFoundException {
        users.remove(findReceptionist(username));
    }

    public ArrayList<Receptionist> getReceptionists() {
        ArrayList<Receptionist> list = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Receptionist) {
                list.add((Receptionist) u);
            }
        }
        return list;
    }

    // ===================== ROOMS =====================

    public void checkRoomNumberFree(String roomNumber) throws DuplicateRecordException {
        for (Room r : rooms) {
            if (r.getRoomNumber().equals(roomNumber)) {
                throw new DuplicateRecordException("Room " + roomNumber + " already exists.");
            }
        }
    }

    public void addRoom(Room room) throws DuplicateRecordException {
        checkRoomNumberFree(room.getRoomNumber());
        rooms.add(room);
    }

    public Room findRoom(String roomNumber) throws RecordNotFoundException {
        for (Room r : rooms) {
            if (r.getRoomNumber().equals(roomNumber)) {
                return r;
            }
        }
        throw new RecordNotFoundException("Room " + roomNumber + " was not found.");
    }

    public void deleteRoom(String roomNumber) throws HotelException {
        Room room = findRoom(roomNumber);
        for (Booking b : bookings) {
            if (b.getRoom() == room) {
                throw new InvalidInputException("Cannot delete a room that has booking records.");
            }
        }
        rooms.remove(room);
    }

    public ArrayList<Room> searchRooms(String keyword) {
        ArrayList<Room> results = new ArrayList<>();
        for (Room r : rooms) {
            if (r.getRoomNumber().contains(keyword)
                    || r.getType().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(r);
            }
        }
        return results;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    // Admin can put a room under maintenance or bring it back
    public void toggleMaintenance(Room room) throws InvalidInputException {
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            room.setStatus(RoomStatus.AVAILABLE);
            refreshRoomStatus(room);
        } else {
            if (hasActiveBooking(room)) {
                throw new InvalidInputException("Room has active bookings. Cancel or finish them first.");
            }
            room.setStatus(RoomStatus.MAINTENANCE);
        }
    }

    // Sets the room status based on its bookings
    private void refreshRoomStatus(Room room) {
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            return;
        }
        RoomStatus newStatus = RoomStatus.AVAILABLE;
        for (Booking b : bookings) {
            if (b.getRoom() == room) {
                if (b.getStatus() == BookingStatus.CHECKED_IN) {
                    newStatus = RoomStatus.OCCUPIED;
                    break;
                } else if (b.getStatus() == BookingStatus.PENDING
                        || b.getStatus() == BookingStatus.CONFIRMED) {
                    newStatus = RoomStatus.RESERVED;
                }
            }
        }
        room.setStatus(newStatus);
    }

    private boolean hasActiveBooking(Room room) {
        for (Booking b : bookings) {
            if (b.getRoom() == room && b.getStatus().isActive()) {
                return true;
            }
        }
        return false;
    }

    // ===================== GUESTS =====================

    public void checkGuestIdFree(String guestId) throws DuplicateRecordException {
        for (Guest g : guests) {
            if (g.getGuestId().equalsIgnoreCase(guestId)) {
                throw new DuplicateRecordException("Guest ID " + guestId + " already exists.");
            }
        }
    }

    public void addGuest(Guest guest) throws DuplicateRecordException {
        checkGuestIdFree(guest.getGuestId());
        guests.add(guest);
    }

    public Guest findGuest(String guestId) throws RecordNotFoundException {
        for (Guest g : guests) {
            if (g.getGuestId().equalsIgnoreCase(guestId)) {
                return g;
            }
        }
        throw new RecordNotFoundException("Guest " + guestId + " was not found.");
    }

    public void deleteGuest(String guestId) throws HotelException {
        Guest guest = findGuest(guestId);
        for (Booking b : bookings) {
            if (b.getGuest() == guest) {
                throw new InvalidInputException("Cannot delete a guest who has booking records.");
            }
        }
        guests.remove(guest);
    }

    public ArrayList<Guest> searchGuests(String keyword) {
        ArrayList<Guest> results = new ArrayList<>();
        for (Guest g : guests) {
            if (g.getGuestId().toLowerCase().contains(keyword.toLowerCase())
                    || g.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(g);
            }
        }
        return results;
    }

    public ArrayList<Guest> getGuests() {
        return guests;
    }

    // ===================== BOOKINGS =====================

    // Business rules for stay dates
    public void validateDates(LocalDate checkIn, LocalDate checkOut) throws InvalidInputException {
        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidInputException("Check-in date cannot be in the past.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new InvalidInputException("Check-out date must be after the check-in date.");
        }
        if (checkIn.isAfter(LocalDate.now().plusDays(365))) {
            throw new InvalidInputException("Bookings can only be made up to 1 year in advance.");
        }
        if (ChronoUnit.DAYS.between(checkIn, checkOut) > 30) {
            throw new InvalidInputException("Maximum stay is 30 nights.");
        }
    }

    // True if another active booking overlaps the given dates for this room
    private boolean hasConflict(Room room, LocalDate checkIn, LocalDate checkOut, Booking ignore) {
        for (Booking b : bookings) {
            if (b == ignore || b.getRoom() != room || !b.getStatus().isActive()) {
                continue;
            }
            if (checkIn.isBefore(b.getCheckOut()) && checkOut.isAfter(b.getCheckIn())) {
                return true;
            }
        }
        return false;
    }

    // Stops double booking: throws if the room cannot be used for these dates
    private void checkRoomAvailability(Room room, LocalDate checkIn, LocalDate checkOut, Booking ignore)
            throws RoomUnavailableException {
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            throw new RoomUnavailableException("Room " + room.getRoomNumber() + " is under maintenance.");
        }
        if (hasConflict(room, checkIn, checkOut, ignore)) {
            throw new RoomUnavailableException("Room " + room.getRoomNumber()
                    + " is already booked for the selected dates.");
        }
    }

    public ArrayList<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        ArrayList<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (r.getStatus() != RoomStatus.MAINTENANCE && !hasConflict(r, checkIn, checkOut, null)) {
                available.add(r);
            }
        }
        return available;
    }

    // Lets the Receptionist know right away if the chosen room cannot be booked
    public void checkRoomBookable(String roomNumber, LocalDate checkIn, LocalDate checkOut)
            throws HotelException {
        checkRoomAvailability(findRoom(roomNumber), checkIn, checkOut, null);
    }

    public Booking createBooking(String guestId, String roomNumber, LocalDate checkIn,
                                 LocalDate checkOut, PricingStrategy strategy) throws HotelException {
        Guest guest = findGuest(guestId);
        Room room = findRoom(roomNumber);
        validateDates(checkIn, checkOut);
        checkRoomAvailability(room, checkIn, checkOut, null);

        String id = String.format("B%03d", bookingCounter++);
        Booking booking = new Booking(id, guest, room, checkIn, checkOut, strategy);
        bookings.add(booking);
        refreshRoomStatus(room);
        return booking;
    }

    public Booking findBooking(String bookingId) throws RecordNotFoundException {
        for (Booking b : bookings) {
            if (b.getBookingId().equalsIgnoreCase(bookingId)) {
                return b;
            }
        }
        throw new RecordNotFoundException("Booking " + bookingId + " was not found.");
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void updateBookingDates(String bookingId, LocalDate checkIn, LocalDate checkOut)
            throws HotelException {
        Booking booking = findBooking(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidInputException("Only Pending or Confirmed bookings can be updated.");
        }
        validateDates(checkIn, checkOut);
        checkRoomAvailability(booking.getRoom(), checkIn, checkOut, booking);
        booking.setDates(checkIn, checkOut);
    }

    public void confirmBooking(String bookingId) throws HotelException {
        Booking booking = findBooking(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidInputException("Only Pending bookings can be confirmed.");
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        refreshRoomStatus(booking.getRoom());
    }

    public void cancelBooking(String bookingId) throws HotelException {
        Booking booking = findBooking(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidInputException("Only Pending or Confirmed bookings can be cancelled.");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        refreshRoomStatus(booking.getRoom());
    }

    public void checkIn(String bookingId) throws HotelException {
        Booking booking = findBooking(bookingId);
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidInputException("Only Confirmed bookings can be checked in.");
        }
        if (LocalDate.now().isBefore(booking.getCheckIn())) {
            throw new InvalidInputException("Too early to check in. The check-in date is "
                    + booking.getCheckIn() + ".");
        }
        booking.setStatus(BookingStatus.CHECKED_IN);
        refreshRoomStatus(booking.getRoom());
    }

    public void checkOut(String bookingId) throws HotelException {
        Booking booking = findBooking(bookingId);
        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new InvalidInputException("Only Checked-in bookings can be checked out.");
        }
        booking.setStatus(BookingStatus.CHECKED_OUT);
        refreshRoomStatus(booking.getRoom());
    }
}
