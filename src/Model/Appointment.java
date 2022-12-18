package Model;

import java.sql.Timestamp;

/**
 * Model class for Appointments. An appointment is made by a User, and associated with a Customer, as well as a Contact.
 * The Customer needs to attend the Appointment, but the Contact and the User do not need to be present.
 */
public class Appointment {

    /**
     * A unique id created when appointment is added to the database.
     *
     * A Note about ID Creation:
     * To be clear about ids and when they are created, they are created by the MySQL instance when the appointment is
     * entered into the database. We let the database manage these ids and do not set them in the application, as this
     * application is meant to interact with the database for CRUD but is not itself the data manager.
     * Appointment objects are most often created after pulling appointment data from the database and then casting it
     * to an Appointment object. Typically, when inserting an appointment into the database we do not create an Appointment
     * object first, we simply use an INSERT SQL statement, pass in the raw data, and let the database manage unique
     * id creation for appointments.
     *
     */
    private int id;

    /**
     * The title of the appointment. eg. "All Hands Meeting", "Interview Candidate #50" etc.
     */
    private String title;

    /**
     * Description of the Appointment. eg. "Interview for new software engineer"
     */
    private String description;

    /**
     * Location string for the appointment. Can be any string, such as an address or description of the location.
     * eg. "West Wing", "Room 204", "2905 Fort Knox Ln. St. 120, Dallas TX 75093" etc.
     */
    private String location;

    /**
     * The type of the appointment. eg. "Consultation", "Meeting", "Interview" etc.
     */
    private String type;

    /**
     * The start date and time of the appointment
     */
    private Timestamp start;

    /**
     * The end date and time of the appointment.
     */
    private Timestamp end;

    /**
     * The id of the Customer attending the appointment. The Customer must be present at the appointment.
     */
    private int customerId;

    /**
     * The id of the user associated with the appointment. The user does not need to be present at the appointment.
     */
    private int userId;

    /**
     * The id of the contact associated with the appointment. The contact does not need to be present at the appointment
     */
    private int contactId;

    /**
     * Appointment Constructor
     *
     * A Note about ID Creation:
     * To be clear about ids and when they are created, they are created by the MySQL instance when the appointment is
     * entered into the database. We let the database manage these ids and do not set them in the application, as this
     * application is meant to interact with the database for CRUD but is not itself the data manager.
     * Appointment objects are most often created after pulling appointment data from the database and then casting it
     * to an Appointment object. Typically, when inserting an appointment into the database we do not create an appointment
     * object first, we simply use an INSERT SQL statement, pass in the raw data, and let the database manage unique
     * id creation for appointments.
     *
     * @param id - unique appointment id created when appointment is inserted into the database.
     * @param title - appointment title.
     * @param description - appointment description
     * @param location - appointment location
     * @param type - appointment type
     * @param start - appointment start date and time
     * @param end - appointment end date and time
     * @param customerId - id of the Customer attending the appointment
     * @param userId - id of the user associated with the appointment
     * @param contactId - id of the contact associated with the appointment
     */
    public Appointment(int id, String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * returns the appointment id
     * @return appointment id
     */
    public int getId() {
        return id;
    }

    /**
     * sets the appointment id
     * @param id - appointment id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets the appointment title
     * @return appointment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the appointment title
     * @param title - appointment title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the appointment description
     * @return - appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the appointment description
     * @param description - appointment description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the appointment location
     * @return - appointment location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the appointment location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the appointment type
     * @return - appointment type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the appointment type
     * @param type - appointment type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the appointment start date and time in Timestamp format
     * @return - Timestamp containing the start date and time
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * Sets the appointment start date and time in Timestamp format
     * @param start - Timestamp containing the start date and time
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * Gets the appointment end date and time in Timestamp format
     * @return - Timestamp containing the end date and time
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * Sets the appointment end date and time in Timestamp format
     * @param end - Timestamp containing the end date and time
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * gets the id of the Customer that will be attending the appointment
     * @return - id of the Customer that will be attending the appointment
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the id of the Customer that will be attending the appointment
     * @param customerId - id of the Customer that will be attending the appointment
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * gets the Id of the user associated with the appointment. The user does not need to be present at the appointment.
     * @return - id of the user associated with the appointment
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the id of the User associated with the appointment. The user does not need to be present at the appointment.
     * @param userId - id of the user associated with the appointment.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * gets the Id of the Contact associated with the appointment. The Contact does not need to be present at the
     * appointment.
     * @return - id of the contact associated with the appointment.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the id of the contact associated with the appointment. The Contact does not need to be present at the
     * appointment.
     * @param contactId - id of the contact associated with the appointment.
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * A convenience function that returns a string containing all the information with the Appointment. Used to printing
     *
     * @return - String containing all the information in the Appointment, each item printed on its own line, with an
     * extra newline at the end to making printing a list of Appointments convenient.
     */
    public String toString() {
        String s = title + ": " + description + "\n";
        s += "\tid: " + id + "\n";
        s += "\tlocation: " + location + "\n";
        s += "\ttype: " + type + "\n";
        s += "\tstart: " + start.toString() + "\n";
        s += "\tend: " + end.toString() + "\n";
        s += "\tcustomer id: " + customerId + "\n";
        s += "\tuser id: " + userId + "\n";
        s += "\tcontact id: " + contactId + "\n\n";
        return s;
    }

}
