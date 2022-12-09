package DBAccess;

import Database.DBConnection;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
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
    public static ObservableList<Appointment> lookupAppointmentByTitle(String string) {
        System.out.println("lookupAppointmentByTitle(): " + string);
        ObservableList<Appointment> allAppointments = getAllAppointments();
        final ObservableList<Appointment> matchingAppointments = FXCollections.observableArrayList();
        allAppointments.forEach((appointment) -> {
            if(appointment.getTitle().contains(string)) {
                matchingAppointments.add(appointment);
            }
        });
        return matchingAppointments;
    }
    public static ObservableList<Appointment> lookupAppointmentByTitle(int id) {
        ObservableList<Appointment> allAppointments = getAllAppointments();
        final ObservableList<Appointment> matchingAppointments = FXCollections.observableArrayList();
        allAppointments.stream().filter(appointment -> Integer.compare(appointment.getId(), id) == 0).forEach(matchingAppointments::add);
        return matchingAppointments;
    }

    public static ObservableList<Appointment> lookupAppointmentsForCustomer(int id) {
        System.out.println("lookupAppointmentsForCustomer(): " + id);
        ObservableList<Appointment> allAppointments = getAllAppointments();
        final ObservableList<Appointment> matchingAppointments = FXCollections.observableArrayList();
        allAppointments.stream().filter(appointment -> Integer.compare(appointment.getCustomerId(), id) == 0).forEach(matchingAppointments::add);
        System.out.println("Num of matching appointments: " + matchingAppointments.size());
        return matchingAppointments;
    }

    public static int insertAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, appointment.getStart());
        ps.setTimestamp(6, appointment.getEnd());
        ps.setInt(7, appointment.getCustomerId());
        ps.setInt(8, appointment.getUserId());
        ps.setInt(9, appointment.getContactId());
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static boolean isCollision(Appointment a) {
        // get Customer ID and check all appointments for customer
        ObservableList<Appointment> appointments = lookupAppointmentsForCustomer(a.getCustomerId());

        // convert the timestamps to LocalDateTime
        for (Appointment b: appointments) {

            if(a.getId() == b.getId()) {
                // Appointment cannot collide with itself
                continue;
            }

            LocalDateTime aStart = a.getStart().toLocalDateTime();
            LocalDateTime aEnd = a.getEnd().toLocalDateTime();

            LocalDateTime bStart = b.getStart().toLocalDateTime();
            LocalDateTime bEnd = b.getEnd().toLocalDateTime();

            // aStart - start of meeting a
            // aEnd   - end of meeting a
            // bStart - start of meeting b
            // bEnd   - end of meeting b

            // aStart >= bStart && aStart < bEnd
            if((aStart.isAfter(bStart) || aStart.isEqual(bStart)) && (aStart.isBefore(bEnd))) {
                System.out.println("case 1");
                return true;
            }

            // aEnd > bStart && aEnd <= bEnd
            if((aEnd.isAfter(bStart)) && (aEnd.isBefore(bEnd) || aEnd.isEqual(bEnd))) {
                System.out.println("case 2");
                return true;
            }

            // aStart <= bStart && aEnd >= bEnd
            if((aStart.isBefore(bStart) || aStart.isEqual(bStart)) && (aEnd.isAfter(bEnd) || aEnd.isEqual(bEnd))) {
                System.out.println("case 3");
                return true;
            }
        }
        return false;
    }

    public static int insertAppointment(String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {
        String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    public static int updateAppointment(int id, String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {
        String sql = "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?  WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, appointmentId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static ArrayList<String> getUniqueAppointmentTypes() throws SQLException {
        String sql = "SELECT DISTINCT Type FROM client_schedule.appointments";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList<String> uniqueTypeStrings = new ArrayList<String>();
        while(rs.next()) {
            uniqueTypeStrings.add(rs.getString("Type"));
        }
        return uniqueTypeStrings;
    }

}
