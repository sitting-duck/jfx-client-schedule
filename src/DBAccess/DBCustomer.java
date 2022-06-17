package DBAccess;

import Database.DBConnection;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public abstract class DBCustomer {

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

    public static Customer getCustomerByName(String customerName) throws Exception, SQLException {
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
                    String postal = rs.getString("Postal");
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

    public static ObservableList<Customer> lookupCustomer(int id) {
        ObservableList<Customer> allCustomers = getAllCustomers();
        final ObservableList<Customer> matchingCustomers = FXCollections.observableArrayList();
        allCustomers.stream().filter(customer -> Integer.compare(customer.getId(), id) == 0).forEach(matchingCustomers::add);
        return matchingCustomers;
    }

    public static int insertCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO client_schedule.customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostal());
        ps.setString(4, customer.getPhone());
        ps.setInt(5, customer.getDivisionId());
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

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
