/**
 * Admin: manages rooms and receptionist accounts, and can view guests and bookings.
 */
public class Admin extends User {

    public Admin(String username, String password, String fullName) {
        super(username, password, fullName);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. Manage Rooms");
            System.out.println("2. Manage Receptionist Accounts");
            System.out.println("3. View Guests");
            System.out.println("4. View Bookings");
            System.out.println("5. Logout");
            int choice = InputHelper.readInt("Enter choice: ", 1, 5);

            switch (choice) {
                case 1:
                    roomMenu();
                    break;
                case 2:
                    accountMenu();
                    break;
                case 3:
                    Display.printGuests(getHotel().getGuests());
                    break;
                case 4:
                    Display.printBookings(getHotel().getBookings());
                    break;
                case 5:
                    running = false;
                    break;
            }
        }
    }

    // ===================== ROOM MANAGEMENT =====================

    private void roomMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Room Management ---");
            System.out.println("1. Add Room");
            System.out.println("2. View All Rooms");
            System.out.println("3. Search Room");
            System.out.println("4. Update Room");
            System.out.println("5. Delete Room");
            System.out.println("6. Back");
            int choice = InputHelper.readInt("Enter choice: ", 1, 6);

            switch (choice) {
                case 1:
                    addRoom();
                    break;
                case 2:
                    Display.printRooms(getHotel().getRooms());
                    break;
                case 3:
                    String keyword = InputHelper.readText("Search (room number or type): ");
                    Display.printRooms(getHotel().searchRooms(keyword));
                    break;
                case 4:
                    updateRoom();
                    break;
                case 5:
                    deleteRoom();
                    break;
                case 6:
                    back = true;
                    break;
            }
        }
    }

    private void addRoom() {
        try {
            String number = InputHelper.readRoomNumber();
            getHotel().checkRoomNumberFree(number); // stop early if duplicate
            String type = InputHelper.readRoomType();
            double rate = InputHelper.readDouble("Rate per night (500 - 50000): ", 500, 50000);
            getHotel().addRoom(new Room(number, type, rate));
            System.out.println("Room " + number + " added successfully.");
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    private void updateRoom() {
        try {
            Room room = getHotel().findRoom(InputHelper.readRoomNumber());
            System.out.println("1. Change room type");
            System.out.println("2. Change rate per night");
            System.out.println("3. Toggle maintenance");
            int choice = InputHelper.readInt("Enter choice: ", 1, 3);

            switch (choice) {
                case 1:
                    room.setType(InputHelper.readRoomType());
                    break;
                case 2:
                    room.setRatePerNight(InputHelper.readDouble("New rate (500 - 50000): ", 500, 50000));
                    break;
                case 3:
                    getHotel().toggleMaintenance(room);
                    break;
            }
            System.out.println("Room updated. Current status: " + room.getStatus());
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    private void deleteRoom() {
        try {
            String number = InputHelper.readRoomNumber();
            getHotel().findRoom(number); // make sure it exists before confirming
            if (InputHelper.readYesNo("Delete room " + number + "?")) {
                getHotel().deleteRoom(number);
                System.out.println("Room deleted.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    // ===================== RECEPTIONIST ACCOUNTS =====================

    private void accountMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Receptionist Accounts ---");
            System.out.println("1. Add Account");
            System.out.println("2. View Accounts");
            System.out.println("3. Update Account");
            System.out.println("4. Delete Account");
            System.out.println("5. Back");
            int choice = InputHelper.readInt("Enter choice: ", 1, 5);

            switch (choice) {
                case 1:
                    addAccount();
                    break;
                case 2:
                    Display.printReceptionists(getHotel().getReceptionists());
                    break;
                case 3:
                    updateAccount();
                    break;
                case 4:
                    deleteAccount();
                    break;
                case 5:
                    back = true;
                    break;
            }
        }
    }

    private void addAccount() {
        try {
            String username = InputHelper.readUsername();
            getHotel().checkUsernameFree(username);
            String password = InputHelper.readPassword();
            String name = InputHelper.readName("Full name: ");
            getHotel().addReceptionist(new Receptionist(username, password, name));
            System.out.println("Receptionist account created.");
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    private void updateAccount() {
        try {
            Receptionist r = getHotel().findReceptionist(InputHelper.readUsername());
            System.out.println("1. Change full name");
            System.out.println("2. Change password");
            int choice = InputHelper.readInt("Enter choice: ", 1, 2);

            if (choice == 1) {
                r.setFullName(InputHelper.readName("New full name: "));
            } else {
                r.setPassword(InputHelper.readPassword());
            }
            System.out.println("Account updated.");
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    private void deleteAccount() {
        try {
            String username = InputHelper.readUsername();
            getHotel().findReceptionist(username);
            if (InputHelper.readYesNo("Delete account '" + username + "'?")) {
                getHotel().deleteReceptionist(username);
                System.out.println("Account deleted.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (HotelException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }
}
