package DBAccess;

import Database.DBConnection;
import Model.Country;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * A class containing several convenient functions for fetching and inserting divisions to and from the database.
 */
public abstract class DBDivision {


    /**
     * Returns the set of all divisions in the database as an ObservableList. ObservableList are handy for attaching
     * to GUI items such as ComboBoxes and TableViews.
     * @return - An ObservableList of Divisions.
     */
    public static ObservableList<Division> getAllDivisions() {
        ObservableList<Division> dlist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from client_schedule.first_level_divisions";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");
                Division d = new Division(id, division, countryId);
                dlist.add(d);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return dlist;
    }

    /**
     * Returns the set of Divisions with matching Country name. For example a country name of "U.S" would return all
     * 50 states in the USA.
     * @param countryName - string for Country name. eg. "U.S", "UK", "Canada" etc.
     * @return - An ObservableList of Divisions
     */
    public static ObservableList<Division> getAllDivisionsWithCountryName(String countryName) {
        ObservableList<Division> dlist = getAllDivisions();
        ObservableList<Division> match =  FXCollections.observableArrayList();


        Country country = DBCountry.getAllCountriesWithName(countryName).get(0);
        Integer countryId = country.getId();

        for(Division division : dlist) {
            if(division.getCountryId() == countryId) {
                match.add(division);
            }
        }
        return match;
    }

    /**
     * Returns an ObservableList of Divisions with matching id.
     * @param id - Division Id
     * @return - ObservableList of Divisions with matching id
     */
    public static ObservableList<Division> lookupDivision(int id) {
        ObservableList<Division> allDivisions = getAllDivisions();
        final ObservableList<Division> matchingDivisions = FXCollections.observableArrayList();
        allDivisions.stream().filter(division -> Integer.compare(division.getId(), id) == 0).forEach(matchingDivisions::add);
        return matchingDivisions;
    }

}
