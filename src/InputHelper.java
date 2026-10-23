import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Handles all keyboard input and input validation.
 * Every method keeps asking until the user enters a valid value,
 * so the program never crashes on bad input.
 */
public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);

    // Reads one line and rejects blank input
    private static String readLine(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new InvalidInputException("This field cannot be blank.");
        }
        return input;
    }

    private static void showError(String message) {
        System.out.println("  [Error] " + message);
    }

    public static String readText(String prompt) {
        while (true) {
            try {
                return readLine(prompt);
            } catch (InvalidInputException e) {
                showError(e.getMessage());
            }
        }
    }

    // Reads text that must match a pattern (regex)
    public static String readMatching(String prompt, String regex, String errorMessage) {
        while (true) {
            try {
                String input = readLine(prompt);
                if (!input.matches(regex)) {
                    throw new InvalidInputException(errorMessage);
                }
                return input;
            } catch (InvalidInputException e) {
                showError(e.getMessage());
            }
        }
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(readLine(prompt));
                if (value < min || value > max) {
                    throw new InvalidInputException("Please enter a number from " + min + " to " + max + ".");
                }
                return value;
            } catch (NumberFormatException e) {
                showError("Invalid input. Please enter a whole number.");
            } catch (InvalidInputException e) {
                showError(e.getMessage());
            }
        }
    }

    public static double readDouble(String prompt, double min, double max) {
        while (true) {
            try {
                String input = readLine(prompt);
                // digits only, optional 1-2 decimals (rejects letters, negatives, 1e5, NaN)
                if (!input.matches("\\d+(\\.\\d{1,2})?")) {
                    throw new InvalidInputException("Enter a valid positive amount (example: 1500 or 1500.50).");
                }
                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    throw new InvalidInputException(String.format("Amount must be between %.0f and %.0f.", min, max));
                }
                return value;
            } catch (InvalidInputException e) {
                showError(e.getMessage());
            }
        }
    }

    public static LocalDate readDate(String prompt) {
        while (true) {
            try {
                return LocalDate.parse(readLine(prompt));
            } catch (DateTimeParseException e) {
                showError("Invalid date. Use the format yyyy-MM-dd (example: 2026-12-25).");
            } catch (InvalidInputException e) {
                showError(e.getMessage());
            }
        }
    }

    public static boolean readYesNo(String prompt) {
        String answer = readMatching(prompt + " (y/n): ", "[YyNn]", "Please enter y or n.");
        return answer.equalsIgnoreCase("y");
    }

    // ---------- Ready-made validated inputs ----------

    public static String readName(String prompt) {
        return readMatching(prompt, "\\p{L}[\\p{L} .'-]{1,24}",
                "Name must be 2-25 characters, letters only (spaces . - ' allowed).");
    }

    public static String readPhone() {
        return readMatching("Phone number (09XXXXXXXXX): ", "09\\d{9}",
                "Phone number must be 11 digits and start with 09.");
    }

    public static String readEmail() {
        return readMatching("Email address: ", "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$",
                "Invalid email format (example: name@email.com).");
    }

    public static String readGuestId() {
        return readMatching("Guest ID (format G001): ", "[Gg]\\d{3}",
                "Guest ID must be the letter G followed by 3 digits.").toUpperCase();
    }

    public static String readRoomNumber() {
        return readMatching("Room number (3-4 digits): ", "\\d{3,4}",
                "Room number must be 3 to 4 digits.");
    }

    public static String readBookingId() {
        return readMatching("Booking ID (format B001): ", "[Bb]\\d{3,}",
                "Booking ID must be the letter B followed by digits (example: B001).").toUpperCase();
    }

    public static String readUsername() {
        return readMatching("Username (4-15 letters, numbers or _): ", "[A-Za-z0-9_]{4,15}",
                "Username must be 4-15 characters (letters, numbers, underscore only).");
    }

    public static String readPassword() {
        return readMatching("Password (at least 6 characters, no spaces): ", "\\S{6,20}",
                "Password must be 6-20 characters with no spaces.");
    }

    public static String readRoomType() {
        System.out.println("Room types:");
        for (int i = 0; i < Room.TYPES.length; i++) {
            System.out.println("  " + (i + 1) + ". " + Room.TYPES[i]);
        }
        int choice = readInt("Choose room type: ", 1, Room.TYPES.length);
        return Room.TYPES[choice - 1];
    }
}
