package DBAccess;

import Database.DBConnection;
import Model.Appointment;
import Utils.TimeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A class containing several convenient functions for fetching and inserting appointments to and from the database.
 */
public abstract class DBAppointment {

    /**
     * Returns the set of all appointments in the database as an ObservableList. ObservableList are handy for attaching
     * to GUI items such as ComboBoxes and TableViews.
     * @return - An ObservableList of Appointments.
     */
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

    /**
     * Returns the set of appointments with matching Title string.
     * @param string
     * @return - ObservableList array of Appointments with matching Title String.
     */
    public static ObservableList<Appointment> lookupAppointmentById(String string) {
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

    /**
     * Returns the set of all appointments in the database with matching appointment id. Since Appointment ids are unique
     * this should only ever return one value.
     * @param id - Appointment id
     * @return - ObservableList of Appointment objects. Will only ever return 1 appointment since appointment ids are unique.
     */
    public static ObservableList<Appointment> lookupAppointmentById(int id) {
        ObservableList<Appointment> allAppointments = getAllAppointments();
        final ObservableList<Appointment> matchingAppointments = FXCollections.observableArrayList();
        allAppointments.stream().filter(appointment -> Integer.compare(appointment.getId(), id) == 0).forEach(matchingAppointments::add);
        return matchingAppointments;
    }

    /**
     * Returns the set of all appointments in the database with matching Customer id. Since Customers can have as many
     * appointments as they want, this can return zero or more appointments.
     * @param id - Customer id
     * @return - ObservableList of Appointment objects. Since Customers can have as many appointments as they want, this
     * can return zero or more appointments.
     */
    public static ObservableList<Appointment> lookupAppointmentsForCustomerWithID(int id) {
        ObservableList<Appointment> allAppointments = getAllAppointments();
        final ObservableList<Appointment> matchingAppointments = FXCollections.observableArrayList();
        allAppointments.stream().filter(appointment -> Integer.compare(appointment.getCustomerId(), id) == 0).forEach(matchingAppointments::add);
        return matchingAppointments;
    }

    /**
     * Checks before appointment creation if the appointment overlaps with any existing appointments already in the
     * database.
     *
     * There are a few ways a collision can happen,
     *
     * one is if one appointment is within another appointment
     *
     * an appointment wraps around another appointment
     *
     * or
     *
     * an appintment starts exactly when another appointment ends, and so they are "touching"
     *
     * @param a - the appointment being created and checked for collisions
     * @return - true if there is a collision, false if there is no collision
     */
    public static boolean isCollision(Appointment a) {
        // get Customer ID and check all appointments for customer
        ObservableList<Appointment> appointments = lookupAppointmentsForCustomerWithID(a.getCustomerId());

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
                return true;
            }

            // aEnd > bStart && aEnd <= bEnd
            if((aEnd.isAfter(bStart)) && (aEnd.isBefore(bEnd) || aEnd.isEqual(bEnd))) {
                return true;
            }

            // aStart <= bStart && aEnd >= bEnd
            if((aStart.isBefore(bStart) || aStart.isEqual(bStart)) && (aEnd.isAfter(bEnd) || aEnd.isEqual(bEnd))) {
                return true;
            }
        }
        return false;
    }

    /***
     * Creates a new appointment in the database
     * @param title - Appointment title
     * @param description - Appointment description
     * @param location - Appointment location
     * @param type - Appointment type
     * @param start - Appointment start time as a Timestamp
     * @param end - Appointment end time as a Timestamp
     * @param customerId - customer Id
     * @param userId - user id
     * @param contactId - contact id
     * @return - the number of rows in the database affected. Will always be either zero or 1.
     * @throws SQLException - thrown if the appointment cannot be inserted into the database, for example if a null
     * value is passed in for location instead of a String.
     */
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

    /***
     * Updates an appointment in the database with matching id. All appointment ids are unique so there can only ever
     * be a single matching appointment using this method.
     *
     * @param title - Appointment title
     * @param description - Appointment description
     * @param location - Appointment location
     * @param type - Appointment type
     * @param start - Appointment start time as a Timestamp
     * @param end - Appointment end time as a Timestamp
     * @param customerId - customer Id
     * @param userId - user id
     * @param contactId - contact id
     * @return - the number of rows in the database affected. Will always be either zero or 1.
     * @throws SQLException - thrown if the appointment cannot be inserted into the database, for example if a null
     * value is passed in for location instead of a String.
     */
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

    /**
     * Deletes an appointment with matching appointment id from the database.
     * @param appointmentId
     * @return - the number of rows affected. Will always be either zero or one.
     * @throws SQLException - throws exception when appointment cannot be found in the database because there is no
     * matching id. This can happen if the appointment was never created or has already been deleted.
     */
    public static int deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, appointmentId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Returns the set of all appointments with matching type. String must match exactly.
     * @param type - String for Appointment type eg. "Meeting", "Interview" etc.
     * @return - ArrayList<Appointment> - an array list of appointments with matching type string.
     * @throws SQLException - throws exception if bad values such as null values are passed in. Will not throw an exception
     * if no appointments are found however, will just return an empty list.
     */
    public static ArrayList<Appointment> getAllAppointmentsOfType(String type) throws SQLException {
        String sql = "SELECT * FROM client_schedule.appointments WHERE Type = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, type);
        ResultSet rs = ps.executeQuery();

        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        while(rs.next()) {

            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");
            Appointment a = new Appointment(id, title, description, location, type, start, end, customerId, userId, contactId);
            appointments.add(a);
        }
        return appointments;
    }

    /**
     * Returns the set of all appointments with matching Contact id.
     * @param contactId - The id number for the contact
     * @return - ArrayList<Appointment> - an array list of appointments with matching contact id.
     * @throws SQLException - throws exception if bad values such as null values are passed in. Will not throw an exception
     * if no appointments are found however, will just return an empty list.
     */
    public static ArrayList<Appointment> getAllAppointmentsForContact(int contactId) throws SQLException {
        String sql = "SELECT * FROM client_schedule.appointments WHERE Contact_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, contactId);
        ResultSet rs = ps.executeQuery();

        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        while(rs.next()) {

            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            String type = rs.getString("Type");
            Appointment a = new Appointment(id, title, description, location, type, start, end, customerId, userId, contactId);
            appointments.add(a);
        }
        return appointments;
    }

    /**
     * Returns the set of all appointments with matching month. Search is case insensitive, but month names cannot be shortened.
     * @param month - A string value representing the month. eg. "January", "JANUARY" and "january" are all valid.
     * @return - ArrayList<Appointment> - an array list of appointments with matching month for the start date.
     * If an appointment starts in January and ends in February true is still returned for an appointment with start date
     * in January. Only start dates are checked. We assume no appointments run over a day anyway, but with different time
     * zones this could be relevant.
     * @throws SQLException - throws exception if bad values such as null values are passed in. Will not throw an exception
     * if no appointments are found however, will just return an empty list.
     */
    public static ArrayList<Appointment> getAllAppointmentsInMonth(String month) {
        List<Appointment> alist = getAllAppointments().stream().toList();
        ArrayList<Appointment> inMonth = new ArrayList<Appointment>();

        for(Appointment a : alist) {
            if(TimeUtils.inMonth(a.getStart(), month)) {
                inMonth.add(a);
            }
        }
        return inMonth;
    }

    /**
     * Returns the list of all unique appointment Type strings.
     * @return - An ArrayList of Unique strings from the database of all found type values for all appointments.
     * @throws SQLException - throws exception for improper SQL query
     */
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
