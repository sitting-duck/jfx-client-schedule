package DBAccess;

import Database.DBConnection;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * A class containing several convenient functions for fetching and inserting Users to and from the database.
 */
public class DBUser {

    /**
     * Returns the set of all Users in the database as an ObservableList. ObservableList are handy for attaching
     * to GUI items such as ComboBoxes and TableViews.
     * @return - An ObservableList of Users.
     */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> ulist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from client_schedule.users";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                User u = new User(userId, userName, password);
                ulist.add(u);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return ulist;
    }

    /**
     * Returns the Customer in Database with matching String username. Throws exception if this User cannot be found.
     * Developer can use userExists() function in this class to check existence first.
     * @param username - string: the User name string.
     * @return - matching User object
     * @throws Exception - throws exception if User match cannot be found.
     */
    public static User getUserByUserName(String username) throws Exception {
        try {
            String sql = "SELECT * from client_schedule.users where User_Name = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("User_Name");
                if(name.compareTo(username) == 0) {
                    int id = rs.getInt("User_ID");
                    String password = rs.getString("Password");
                    User u = new User(id, name, password);
                    return u;
                }
            }
            throw new Exception("Error: could not find user with username: " + username);
        } catch(SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    /**
     * Convenience function for printing a ResultSet returned from the database.
     * @param rs - ResultSet to be printed
     * @throws SQLException - exception thrown if column name cannot be found
     */
    private static void printResultSet(ResultSet rs) throws SQLException {
        try {
            while(rs.next()) {
                int id = rs.getInt("User_ID");
                String name = rs.getString("User_Name");
                String password = rs.getString("Password");
                User u = new User(id, name, password);
                u.print();
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }
}
