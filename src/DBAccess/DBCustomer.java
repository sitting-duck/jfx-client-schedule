package DBAccess;

import Database.DBConnection;
import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * A class containing several convenient functions for fetching and inserting Customers to and from the database.
 */
public abstract class DBCustomer {

    /**
     * Returns the set of all Customers in the database as an ObservableList. ObservableList are handy for attaching
     * to GUI items such as ComboBoxes and TableViews.
     * @return - An ObservableList of Customers.
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> clist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from client_schedule.customers";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postal = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                Customer c = new Customer(customerId, name, address, postal, phone, divisionId);
                clist.add(c);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return clist;
    }

    /**
     * Checks the database for a match of Customer name. Must be a complete match.
     * string
     * @param customerName - a customer name in string form
     * @return - returns true if customer with matching name string can be found, false if no match found.
     * @throws Exception - throws exception for poorly formed SQL
     * @throws SQLException - throws excaption for poorly formed SQL
     */
    public static Boolean customerExists(String customerName) throws Exception, SQLException {
        int numberOfRows = -1;
        try {
            String sql = "SELECT COUNT(*) from client_schedule.customers where Customer_Name = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                numberOfRows = rs.getInt(1);
                System.out.println("numberOfRows= " + numberOfRows);
            } else {
                System.out.println("error: could not get the record counts");
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
        return numberOfRows > 0; // true if there are results, false for no results
    }

    /**
     * Returns the Customer in Database with matching String name. Throws exception if this Customer cannot be found.
     * Developer can use customerExists() function in this class to check existence first.
     * @param customerName - string the Customer name string.
     * @return - matching Customer object
     * @throws Exception - throws exception if Customer match cannot be found.
     */
    public static Customer getCustomerByName(String customerName) throws Exception {
        try {
            String sql = "SELECT * from client_schedule.customers where Customer_Name = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("Customer_Name");
                if(name.compareTo(customerName) == 0) {
                    int id = rs.getInt("Customer_ID");
                    String address = rs.getString("Address");
                    String postal = rs.getString("Postal_Code");
                    String phone = rs.getString("Phone");
                    int divisionId = rs.getInt("Division_ID");
                    Customer c = new Customer(id, name, address, postal, phone, divisionId);
                    return c;
                }
            }
            throw new Exception("Error: could not find customer with customerName: " + customerName);
        } catch(SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    /**
     * Searches the database for Customers with a match or partial match for name string. All Customers with Name strings
     * containing the search string as a substring anywhere in the name will be returned.
     * eg. If string is 'a', all Customers with names containing 'a' will be returned. Case-sensitive.
     * @param string - search string
     * @return - list of Customers with a match or partial match in name string with the search string.
     */
    public static ObservableList<Customer> lookupCustomer(String string) {
        ObservableList<Customer> allCustomers = getAllCustomers();
        final ObservableList<Customer> matchingCustomers = FXCollections.observableArrayList();
        allCustomers.forEach((customer) -> {
            //boolean contains = Pattern.compile(Pattern.quote(string), Pattern.CASE_INSENSITIVE).matcher(customer.getName()).find();;
            if(customer.getName().contains(string)) {
            //if(contains) {
                matchingCustomers.add(customer);
            }
        });
        return matchingCustomers;
    }

    /**
     * returns the Customer with matching customer id.
     * @param id - id number
     * @return - customer object with matching id number.
     */
    public static ObservableList<Customer> lookupCustomer(int id) {
        ObservableList<Customer> allCustomers = getAllCustomers();
        final ObservableList<Customer> matchingCustomers = FXCollections.observableArrayList();
        allCustomers.stream().filter(customer -> Integer.compare(customer.getId(), id) == 0).forEach(matchingCustomers::add);
        return matchingCustomers;
    }

    /**
     * Deletes the Customer with matching id number.
     * @param id - id number
     * @return - rows affected. Will always be zero or 1 since Customer ids are unique.
     * @throws SQLException - throws exception on poorly formed SQL, will not throw exception if Customer not found.
     */
    public static int deleteCustomer(int id) throws SQLException {
        System.out.println("DBAppointment::deleteCustomer: id: " + id);
        ObservableList<Customer> customers = lookupCustomer(id);
        ObservableList<Appointment> appointments = DBAppointment.lookupAppointmentsForCustomerWithID(id);

        int rows = 0;
        for(Customer customer: customers) {
            System.out.println("Customer: " + customer.getName());
            for(Appointment appointment: appointments) {
                System.out.println("Deleting Appointment: " + appointment.getId() + ": " + appointment.getTitle());
                rows += DBAppointment.deleteAppointment(appointment.getId());
            }
            String sql = "DELETE FROM client_schedule.customers WHERE Customer_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            rows += ps.executeUpdate();
        }
        return rows;
    }

    /**
     * Creates and inserts a new Customer into the database.
     * @param name - name string
     * @param address - address string
     * @param postalCode - postal code string
     * @param phone - phone number string
     * @param divisionId - division id string eg. "Texas", "New Jersey" etc.
     * @return - rows affected. Will always be zero or 1.
     * @throws SQLException - throws exception on poorly formed SQL
     */
    public static int insertCustomer(String name, String address, String postalCode, String phone, int divisionId) throws SQLException {
        String sql = "INSERT INTO client_schedule.customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Updates Customer in the database with matching id.
     * @param id - id number
     * @param name - name string
     * @param address - address string
     * @param postalCode - postal code string
     * @param phone - phone number string
     * @param divisionId - division id string. eg. "Texas" "New Jersey" etc.
     * @return returns rows affected. Will always be zero or one since Customer ids are all unique.
     * @throws SQLException
     */
    public static int updateCustomer(int id, String name, String address, String postalCode, String phone, int divisionId) throws SQLException {
        String sql = "UPDATE client_schedule.customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?  WHERE Customer_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionId);
        ps.setInt(6, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

}
