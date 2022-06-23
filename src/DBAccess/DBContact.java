package DBAccess;

import Database.DBConnection;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBContact {

    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> ulist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from client_schedule.contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact u = new Contact(id, name, email);
                ulist.add(u);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return ulist;
    }

    public static Contact getContactByContactName(String name) throws Exception, SQLException {
        try {
            String sql = "SELECT * from client_schedule.contacts where Contact_Name = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String cname = rs.getString("Contact_Name");
                if(cname.compareTo(name) == 0) {
                    int id = rs.getInt("Contact_ID");
                    String email = rs.getString("Email");
                    Contact u = new Contact(id, name, email);
                    return u;
                }
            }
            throw new Exception("Error: could not find contact with name: " + name);
        } catch(SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    private static void printResultSet(ResultSet rs) throws SQLException {
        try {
            while(rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact c = new Contact(id, name, email);
                c.print();
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }
}
