package Model;

public class FirstLevelDivisions {

    public int id;
    public String division;
    public int countryId;

    FirstLevelDivisions(int id, String division, int countryId) {
        this.id = id;
        this.division = division;
        this.countryId = countryId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }


}
