package DBAccess;

import Database.DBConnection;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DBAppointment {

    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> alist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from client_schedule.appointments";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Date start = rs.getDate("Start");
                Date end = rs.getDate("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment a = new Appointment(id, title, description, location, type, start, end, customerId, userId, contactId);
                alist.add(a);
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return alist;
    }
    public static ObservableList<Appointment> lookupAppointment(String string) {
        ObservableList<Appointment> allAppointments = getAllAppointments();
        final ObservableList<Appointment> matchingAppointments = FXCollections.observableArrayList();
        allAppointments.forEach((appointment) -> {
            if(appointment.getTitle().contains(string)) {
                matchingAppointments.add(appointment);
            }
        });
        return matchingAppointments;
    }

    public static ObservableList<Appointment> lookupAppointment(int id) {
        ObservableList<Appointment> allAppointments = getAllAppointments();
        final ObservableList<Appointment> matchingAppointments = FXCollections.observableArrayList();
        allAppointments.stream().filter(appointment -> Integer.compare(appointment.getId(), id) == 0).forEach(matchingAppointments::add);
        return matchingAppointments;
    }

    public static int insertAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setDate(5, appointment.getStart());
        ps.setDate(6, appointment.getEnd());
        ps.setInt(7, appointment.getCustomerId());
        ps.setInt(8, appointment.getUserId());
        ps.setInt(9, appointment.getContactId());
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int insertAppointment(String title, String description, String location, String type, Date start, Date end, int customerId, int userId, int contactId) throws SQLException {
        String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setDate(5, start);
        ps.setDate(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    public static int updateAppointment(int id, String title, String description, String location, String type, Date start, Date end, int customerId, int userId, int contactId) throws SQLException {
        String sql = "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?  WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setDate(5, start);
        ps.setDate(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
