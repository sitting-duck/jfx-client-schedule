package Model;

/**
 * Model class for Divisions. A Division is similar to a state or province within a country. The Division "Texas"
 * exists within the country "U.S" for example.
 */
public class Division {

    /**
     * unique division id created when inserting into the database
     */
    private int id;

    /**
     * Division string
     */
    private String division;

    /**
     * Id for the country assocated with this division
     */
    private int countryId;

    /**
     * Constructor for creating a division
     * @param id - division id
     * @param division - division name string
     * @param countryId - id of the country associated with this division
     */
    public Division(int id, String division, int countryId) {
        this.id = id;
        this.division = division;
        this.countryId = countryId;
    }

    /**
     * gets the unique id for this division
     * @return - unique id for this division
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique id for this division
     * @param id - unique id for this division
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets the division string for this division
     * @return - division string for this division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets the division string for this division
     * @param division - division string
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Gets the country id for this division
     * @return - country id for this division
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the country id for this division
     * @param countryId - country id for this division
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Returns a convenient string for printing
     * @return - a convenient string for printing
     */
    @Override
    public String toString() {
        return this.id + ": " + this.division;
    }

}
