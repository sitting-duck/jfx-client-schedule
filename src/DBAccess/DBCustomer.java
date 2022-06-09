package DBAccess;

import Database.DBConnection;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBCustomer {

    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> clist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from client_schedule.customers";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int customerId = rs.getInt("Customer_Name");
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
}
