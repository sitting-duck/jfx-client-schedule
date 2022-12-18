package Model;

/**
 * Model class for Customers. A Customer is associated with each Appointment that is made and that Customer must be
 * present at the Appointment.
 */
public class Customer {

    /**
     * A unique id created when the customer is added to the database.
     */
    private int id;

    /**
     * Customer name
     */
    private String name;

    /**
     * Customer address
     */
    private String address;

    /**
     * Customer postal code. (In USA this is sometimes called a "zip" code
     */
    private String postal;

    /**
     * Customer phone number
     */
    private String phone;

    /**
     * customer division id
     */
    private int divisionId;

    /**
     * Constructor for creating a customer
     * @param id - unique customer id
     * @param name - customer name
     * @param address - customer address
     * @param postal - customer postal code
     * @param phone - customer phone number
     * @param divisionId - customer division id
     */
    public Customer(int id, String name, String address, String postal, String phone, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    /**
     * Gets the customer id
     * @return customer id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the customer id
     * @param id - customer id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the customer name
     * @return - customer name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the customer name
     * @param name - customer name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the customer address
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the customer address
     * @param address - customer address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the customer postal code
     * @return - customer postal code
     */
    public String getPostal() {
        return postal;
    }

    /**
     * Sets the customer postal code
     * @param postal -  customer postal code
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * Gets the customer phone number
     * @return - customer phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the customer phone number
     * @param phone - customer phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the customer division id
     * @return - customer division id
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the customer division id
     * @param divisionId - customer division id
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
