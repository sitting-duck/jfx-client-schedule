package Model;

/**
 * Model class for users
 */
public class User {

    /**
     * unique id created when User is added to the database
     */
    private int id;

    /**
     * username
     */
    private String name;

    /**
     * password
     */
    private String password;

    /**
     * A constructor for creating a user
     * @param id - unique user id
     * @param name - user name
     * @param password - user password
     */
    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    /**
     * Gets the user name
     * @return - user name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user name
     * @param name - user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user password
     * @return - user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user password
     * @param password - user password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user id
     * @return - user id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user id
     * @param id - user id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Prints all user info with each item on a new line.
     */
    public void print() {
        System.out.print("User: " + this.id);
        System.out.print("\tname: " + this.name);
        System.out.print("\tpassword: " + this.password + "\n");

    }
}
