package DBAccess;

import Database.DBConnection;
import Model.Country;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public abstract class DBDivision {

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

    public static ObservableList<Division> getAllDivisionsWithCountryId(int countryId) {
        ObservableList<Division> dlist = getAllDivisions();
        ObservableList<Division> match =  FXCollections.observableArrayList();

        for(Division division : dlist) {
            if(division.getCountryId() == countryId) {
                match.add(division);
            }
        }
        return match;
    }

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
    public static Division getDivisionByName(String division) throws Exception, SQLException {
        try {
            String sql = "SELECT * from client_schedule.first_level_divisions where Division = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, division);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String divisionName = rs.getString("Division");
                if(divisionName.compareTo(division) == 0) {
                    int id = rs.getInt("Division_ID");
                    int countryId = rs.getInt("Country_ID");
                    Division c = new Division(id, division, countryId);
                    return c;
                }
            }
            throw new Exception("Error: could not find division with divisionName: " + division);
        } catch(SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public static ObservableList<Division> lookupDivision(String string) {
        ObservableList<Division> allDivisions = getAllDivisions();
        final ObservableList<Division> matchingDivisions = FXCollections.observableArrayList();
        allDivisions.forEach((division) -> {
            if(division.getDivision().contains(string)) {
                //if(contains) {
                matchingDivisions.add(division);
            }
        });
        return matchingDivisions;
    }

    public static ObservableList<Division> lookupDivision(int id) {
        ObservableList<Division> allDivisions = getAllDivisions();
        final ObservableList<Division> matchingDivisions = FXCollections.observableArrayList();
        allDivisions.stream().filter(division -> Integer.compare(division.getId(), id) == 0).forEach(matchingDivisions::add);
        return matchingDivisions;
    }

    public static int insertDivision(Division division) throws SQLException {
        String sql = "INSERT INTO client_schedule.first_level_divisions (Division, Country_ID) VALUES(?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, division.getDivision());
        ps.setInt(2, division.getCountryId());
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int insertDivision(String division, int countryId) throws SQLException {
        String sql = "INSERT INTO client_schedule.first_level_divisions (Division, Country_ID) VALUES(?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, division);
        ps.setInt(2, countryId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int updateDivision(int id, String division, int countryId) throws SQLException {
        String sql = "UPDATE client_schedule.first_level_divisions SET Division = ?, Country_ID = ? WHERE Division_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, division);
        ps.setInt(2, countryId);
        ps.setInt(3, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

}
