package DBAccess;

import Database.DBConnection;
import Model.Countries;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBUser {

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

    public static User getUserByUserName(String username) throws Exception, SQLException {
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

            //System.out.println("Table contains "+rs.getRow()+" rows");
            //printResultSet(rs);

            //int id = rs.getInt("User_ID");
            //String name = rs.getString("User_Name");
            //String password = rs.getString("Password");
            //User u = new User(id, name, password);
            //return u;
        } catch(SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

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
