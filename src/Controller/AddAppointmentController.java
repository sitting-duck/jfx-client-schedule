package Controller;

import DBAccess.DBAppointment;
import Model.Appointment;
import Utils.TimeUtils;
import Utils.UXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

/**
 * This AddAppointmentController Class is linked to the add-appointment.fxml file which controls the view users will use
 * to fill in a form for adding an appointment to the database. The form includes text fields, combo boxes and calendar
 * widgets for picking the date and time of the appointment. When creating appointments the logic for checking the database
 * for appointment collisions and many other checks all happen within this class.
 */
public class AddAppointmentController extends AppointmentController implements Initializable {

    /**
     * This function initializes the AddAppointment view. It mainly uses classes from UXUtil to fetch lists of items
     * from the database, do some string slicing to create convenient strings for displaying, and set those lists of
     * strings to be the contents of various ComboBoxes in this view.
     * @param url - not used, however it is essential to pass this in because this class extends the Initializable class
     *            and so we must follow the structure of that interface exactly.
     * @param resourceBundle - not used, however it is essential to pass this in because this class extends the Initializable class
     *      *            and so we must follow the structure of that interface exactly.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }

    /**
     * This function runs when the user clicks the OK button in the AddAppointment view to indicate that they are done
     * creating the appointment, and they would like it added to the database now. This function runs many checks on the
     * data entered by the user into various fields and will display an appropriate warning if needed. For example, the
     * database is checked to make sure that the appointment the user is trying to create does not overlap with any other
     * appointments already created in the database. This is called Collision Detection. The function will not insert into
     * the database any appointments that create collisions and will show red text above any text fields and combo boxes
     * that need to be corrected by the user.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public boolean onOkButton(ActionEvent actionEvent) throws IOException, SQLException {
        boolean good = super.onOkButton(actionEvent);
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();

        Timestamp start = null;
        Timestamp end = null;

        try {
            Timestamp startDay = Timestamp.valueOf(startDatePicker.getValue().atStartOfDay());
            int startTimeHour = (int)startHourComboBox.getValue();
            int startTimeMinute = (int)startMinuteComboBox.getValue();
            String startTimeAmPm = (String)startAMPMComboBox.getValue();
            start = TimeUtils.buildTimeStamp(startDay, startTimeHour, startTimeMinute, startTimeAmPm);
            LocalDateTime startTime_ldt = start.toLocalDateTime();

            Timestamp endDay = Timestamp.valueOf(endDatePicker.getValue().atStartOfDay());
            int endTimeHour = (int)endHourComboBox.getValue();
            int endTimeMinute = (int)endMinuteComboBox.getValue();
            String endTimeAmPm = (String)endAMPMComboBox.getValue();
            end = TimeUtils.buildTimeStamp(endDay, endTimeHour, endTimeMinute, endTimeAmPm);
            LocalDateTime endTime_ldt = end.toLocalDateTime();

            long minutes = ChronoUnit.MINUTES.between(startTime_ldt, endTime_ldt);
            long hours = ChronoUnit.HOURS.between(startTime_ldt, endTime_ldt);

            System.out.println("Appointment is " + hours + " hours and " + minutes + " minutes.");

            if(minutes == 0) {
                good = false;
                startLabel.setTextFill(Color.color(1, 0, 0));
                startLabel.setText("Appointment cannot be zero length");
                endLabel.setTextFill(Color.color(1, 0, 0));
                endLabel.setText("Appointment cannot be zero length");
            }

            if(start.toLocalDateTime().isAfter(end.toLocalDateTime())) {
                startLabel.setTextFill(Color.color(1, 0, 0));
                startLabel.setText("Start date/time must come BEFORE end date/time");
                endLabel.setTextFill(Color.color(1, 0, 0));
                endLabel.setText("End date/time must come AFTER start date/time");
                good = false;
            }

        } catch(Exception e) {
            good = false;
        }

        if(good == false) {
            System.out.println("Input was not valid, Appointment NOT updated in database.");
            return good;
        }

        boolean withinOfficeHours = TimeUtils.withinOfficeHours(start, end);
        if(!withinOfficeHours) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: office hours");
            alert.setHeaderText("Error: office hours are 8am-10pm EST");
            alert.setContentText("Error: offices hours are 8am-10pm ESTError: offices 8am-10pm EST");
            alert.showAndWait();
            return good;
        }

        int customerId = UXUtil.getIdNumberFromComboBox(customerIdComboBox);
        int userId = UXUtil.getIdNumberFromComboBox(userIdComboBox);
        int contactId = UXUtil.getIdNumberFromComboBox(contactIdComboBox);
        Appointment newAppointment = new Appointment(0, title, description, location, type, start, end, customerId, userId, contactId);
        boolean isOverlap = DBAppointment.isCollision(newAppointment);
        System.out.println("isOverlap: " + isOverlap);

        if(isOverlap == false) {
            DBAppointment.insertAppointment(title, description, location, type, start, end, customerId, userId, contactId);

            Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1400, 400);
            stage.setTitle("Appointment Manager");
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: Appointment Overlap");
            alert.setHeaderText("Error: This appointment overlaps with another");
            alert.setContentText("Error: This appointment overlaps with another");
            alert.showAndWait();
            return good;
        }

        return good;
    }
}
