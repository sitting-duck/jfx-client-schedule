package DBAccess;

import Database.DBConnection;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.regex.Pattern;

public class DBCustomer {

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

}
