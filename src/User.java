/**
 * Abstract parent class of Admin and Receptionist.
 * Each child class must provide its own role and its own menu.
 */
public abstract class User {
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public boolean checkCredentials(String username, String password) {
        return this.username.equalsIgnoreCase(username) && this.password.equals(password);
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Shortcut used by the child classes to reach the Singleton
    protected HotelSystem getHotel() {
        return HotelSystem.getInstance();
    }

    public abstract String getRole();

    public abstract void showMenu();
}
