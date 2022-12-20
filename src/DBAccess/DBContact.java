package DBAccess;

import Database.DBConnection;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * A class for CRUD Contacts from the database
 */
public class DBContact {

    /**
     * Returns all the Contacts in the database as Contact objects in an ObservableList array.
     * @return an ObservableList array of all the Contacts in the database as Contact objects.
     */
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

    /**
     * Returns a Contact with a matching name to the String entered. Throws an exception if there is no such contact.
     * @param name - the search string for Contact name. It needs to match the value in the "Contact_Name" column in the
     *             database. This is equivalent to the value returned by Contact.getName().*
     * @return - A single Contact object with a matching name to the search string passed to this function. An exception
     *           is thrown when no contact is found. If there is more than one Contact found with matching name, the first
     *           Contact found is returned without regard to if this is desired. If multiple Contacts in the database
     *           have the same name you may wish to narrow your search even more with other Contact attributes using
     *           another search function.
     * @throws Exception - throws on bad data returned from database
     * @throws SQLException - throws on poorly formed SQL
     */
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

    /**
     * Prints the ResultSet passed into this function. The function is just for convenient printing and does not edit or
     * the ResultSet at all.
     * @param rs - a ResultSet returned from a query of the database.
     * @throws SQLException
     */
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
