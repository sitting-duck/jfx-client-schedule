package Model;

/**
 * Model class for Countries. A Country is used to select where the Appointment will take place. All Divisions are
 * subdivisions within a Country. In the USA these are called states, and in the UK, these would be provinces and so on
 */
public class Country {

    /**
     * Unique country id
     */
    private int id;

    /**
     * Country name
     */
    private String name;

    /**
     * Country constructor
     * @param id - unique country id
     * @param name - country name
     */
    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * gets the country id
     * @return - the country id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the country id
     * @param id - country id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the country name
     * @return - country name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the country name
     * @param name - country name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns the country name
     * @return - country name
     */
    public String toString() {
        return name;
    }

}
