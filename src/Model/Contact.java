package Model;

/**
 * Model class for Contacts. A Contact is associated with the appointment and can be contacted by any party for more
 * information about the appointment, but is not required to be present at the appointment. An example of a Contact
 * may be for example a hiring manager overseeing a bunch of interviews, but who may send individual developers of
 * various specializations to interview candidates and report back.
 */
public class Contact {

    /**
     * A unique id created when Contact is added to the database.
     */
    private int id;

    /**
     * The name of the Contact. eg. "John Doe", "Jane Doe", etc.
     */
    private String name;

    /**
     * The email address of the contact. eg. "john.doe@infosys.com"
     */
    private String email;

    /**
     * A Construction for creating a Contact object
     * @param id - unique id for the contact created when contact is INSERTED into the database
     *
     * A Note about ID Creation:
     * To be clear about ids and when they are created, they are created by the MySQL instance when the contact is
     * entered into the database. We let the database manage these ids and do not set them in the application, as this
     * application is meant to interact with the database for CRUD but is not itself the data manager.
     * Contact objects are most often created after pulling contact data from the database and then casting it
     * to a Contact object. Typically, when inserting an contact into the database we do not create a Contact
     * object first, we simply use an INSERT SQL statement, pass in the raw data, and let the database manage unique
     * id creation for contacts.
     *
     * @param name - name of the contact eg. "John Doe" "Jane Doe" etc.
     * @param email - email of the contact. eg. "jdoe@info.com"
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Gets the unique id of the Contact. Contact ids are created and managed by the database.
     * @return - the unique id of the Contact.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique id of the Contact. Contact ids are created and managed by the database.
     * @param id - the unique id of the Contact.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the Contact eg. "John Doe", "Jane Doe" etc.
     * @return - the name of the Contact
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Contact. eg. "John Doe", "Jane Doe" etc.
     * @param name - name of the Contact.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the Contact
     * @return - the email address of the Contact
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the Contact
     * @param email email address of the Contact
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Prints all the info for Contact with each item on a new line.
     */
    public void print() {
        System.out.print("Contact: " + this.id);
        System.out.print("\tname: " + this.name);
        System.out.print("\temail: " + this.email);
    }

}
