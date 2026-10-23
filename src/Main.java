import java.util.NoSuchElementException;

/**
 * HotelEase - Hotel Booking and Reservation System
 * Entry point of the program: handles login, then opens the menu of the user's role.
 */
public class Main {

    public static void main(String[] args) {
        HotelSystem hotel = HotelSystem.getInstance();

        System.out.println("=========================================");
        System.out.println("   HotelEase - Hotel Booking System");
        System.out.println("=========================================");

        try {
            boolean running = true;
            while (running) {
                System.out.println("\n1. Login");
                System.out.println("2. Exit");
                int choice = InputHelper.readInt("Enter choice: ", 1, 2);

                if (choice == 1) {
                    User user = login(hotel);
                    if (user != null) {
                        System.out.println("\nWelcome, " + user.getFullName() + " (" + user.getRole() + ")!");
                        user.showMenu(); // Polymorphism: Admin or Receptionist menu
                        System.out.println("You have been logged out.");
                    }
                } else {
                    running = false;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nInput closed. Exiting program.");
        }
        System.out.println("Thank you for using HotelEase. Goodbye!");
    }

    // Gives the user 3 attempts to log in
    private static User login(HotelSystem hotel) {
        int attempts = 0;
        while (attempts < 3) {
            String username = InputHelper.readText("Username: ");
            String password = InputHelper.readText("Password: ");

            User user = hotel.login(username, password);
            if (user != null) {
                return user;
            }
            attempts++;
            System.out.println("  [Error] Invalid username or password. Attempts left: " + (3 - attempts));
        }
        System.out.println("Too many failed attempts. Returning to the main menu.");
        return null;
    }
}
