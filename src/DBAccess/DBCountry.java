package DBAccess;

import Database.DBConnection;
import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * A class containing several convenient functions for fetching Countries from the database.
 */
public abstract class DBCountry {

    /**
     * Returns the set of all countries in the database as an ObservableList. ObservableList are handy for attaching
     * to GUI items such as ComboBoxes and TableViews.
     * @return - An ObservableList of Countries.
     */
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> clist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from client_schedule.countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country C = new Country(countryId, countryName);
                clist.add(C);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return clist;
    }

    /**
     * Returns the set of all appointments with matching type. String must match exactly.
     * @param name - String for Country name eg. "Scotland", "Canada" etc.
     * @return - an array list of appointments with matching name string.
     * if no appointments are found however, will just return an empty list.
     */
    public static ObservableList<Country> getAllCountriesWithName(String name) {
        ObservableList<Country> clist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from client_schedule.countries WHERE Country = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country C = new Country(countryId, countryName);
                clist.add(C);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return clist;
    }

    /**
     * Returns the set of all Countries in the database with matching appointment id. Since Country ids are unique
     * this should only ever return one value.
     * @param id - Country id
     * @return - ObservableList of Country objects. Will only ever return 1 country since Country ids are unique.
     */
    public static ObservableList<Country> getAllCountriesWithID(int id) {
        ObservableList<Country> clist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from client_schedule.countries WHERE Country_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country C = new Country(countryId, countryName);
                clist.add(C);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return clist;
    }
}
