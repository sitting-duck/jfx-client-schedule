package Controller;

import DBAccess.DBAppointment;
import Model.Appointment;
import Utils.AlertUtil;
import Utils.SceneLoader;
import Utils.TimeUtils;
import Utils.UXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

/**
 * This ModifyAppointmentController Class is linked to the modify-appointment.fxml file which controls the view users will use
 * to edit a form for modifying an appointment from the database. The form includes text fields, combo boxes and calendar
 * widgets for picking the date and time of the appointment. When creating appointments the logic for checking the database
 * for appointment collisions and many other checks all happen within this class.
 */
public class ModifyAppointmentController extends AppointmentController  {

    /**
     * This Appointment object is passed in the from the main view and it is the Appointment the user selected in the
     * Appointments table right before clicking the Modify button under the Appointments table in the main view. In this
     * modify view, the user will be editing and modifying exactly this Appointment passed in, and if they click the OK
     * button and all checks pass, the Appointment will be entered into the datasbase with the new data.
     */
    private static Appointment appointment = null;

    /**
     * A non-editable text field that shows the 'Appointment_ID' value from the database for this Appointment. All
     * Appointments have a unique id created for them when they are added to the database.
     */
    @FXML
    private TextField idTextField;

    /**
     * Called when the modify-appointment.fxml view is loaded. It is meant to pass the selected Appointment from the
     * Appointment table in the main view (main.fxml) to thie modify-customer.fxml view. All the data members of this
     * Appointment class are extracted during the execution of the initialize() function and used to set various text
     * fields and combo boxes that the user can interact with in order to edit the appointment selected before they
     * clicked the "Modify" button under the Appointment table in main view. 
     * @param _appointment - An appointment object created from the Appointment selected in the appointment table at the
     *                     time that the user clicked the "Modify"button under the Appointments table in main view. 
     *                     
     *                     The idea here is that the user first selects the appointment they would like to modify, 
     *                     clicks the "Modify" button, and that appointment is passed using this function to set the 
     *                     views for the various tools the user will use to modify. 
     */
    public static void setAppointment(Appointment _appointment) {
        appointment = _appointment;
    }

    /**
     * Called when the modify-appointment.fxml file for the Add Customer view is loaded. This function initializes any GUI
     * elements necessary and pulls items from the database if needed.
     * @param url - not used, however it is essential to pass this in because this class extends the Initializable class
     *      *            and so we must follow the structure of that interface exactly.
     * @param resourceBundle - not used, however it is essential to pass this in because this class extends the Initializable class
     *      *            and so we must follow the structure of that interface exactly.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        if(appointment == null) {
            AlertUtil.appointmentSelectWarningModify();
        }

        // Set Values
        // appointment id is auto generated and should not be edited by the user
        idTextField.setText(Integer.toString(appointment.getId()));
        idTextField.setEditable(false);
        idTextField.setDisable(true);

        titleTextField.setText(appointment.getTitle());
        descriptionTextField.setText(appointment.getDescription());
        locationTextField.setText(appointment.getLocation());
        typeTextField.setText(appointment.getType());

        customerIdComboBox.setValue(appointment.getCustomerId());
        userIdComboBox.setValue(appointment.getUserId());
        contactIdComboBox.setValue(appointment.getContactId());

        startDatePicker.setValue(appointment.getStart().toLocalDateTime().toLocalDate());
        endDatePicker.setValue(appointment.getEnd().toLocalDateTime().toLocalDate());
        int startHour = appointment.getStart().toLocalDateTime().getHour();
        int startHourCivilian = TimeUtils.militaryToCivilianHour(startHour);
        startHourComboBox.setValue(startHourCivilian);
        int endHour = appointment.getEnd().toLocalDateTime().getHour();
        int endHourCivilian = TimeUtils.militaryToCivilianHour(endHour);
        endHourComboBox.setValue(endHourCivilian);

        startMinuteComboBox.setValue(appointment.getStart().toLocalDateTime().getMinute());
        endMinuteComboBox.setValue(appointment.getEnd().toLocalDateTime().getMinute());
        startAMPMComboBox.setValue(TimeUtils.getAMPMFromHour(startHour));
        endAMPMComboBox.setValue(TimeUtils.getAMPMFromHour(endHour));
        customerIdComboBox.setValue(appointment.getCustomerId());
        userIdComboBox.setValue(appointment.getUserId());
        contactIdComboBox.setValue(appointment.getContactId());

    }

    /**
     * This function runs when the user clicks the OK button in the modify-appointment.fxml view to indicate that they are done
     * editing the appointment, and they would like it added to the database now. This function runs many checks on the
     * data entered by the user into various fields and will display an appropriate warning if needed. For example, the
     * database is checked to make sure that the appointment the user is trying to create does not overlap with any other
     * appointments already created in the database. This is called Collision Detection. The function will not insert into
     * the database any appointments or edits that create collisions and will show red text above any text fields and combo boxes
     * that need to be corrected by the user.
     *
     * @param actionEvent
     * @return
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
            start = Timestamp.valueOf(startDatePicker.getValue().atStartOfDay());
            int startTimeHour = (int)startHourComboBox.getValue();
            int startTimeMinute = (int)startMinuteComboBox.getValue();
            String startTimeAmPm = (String)startAMPMComboBox.getValue();
            start = TimeUtils.buildTimeStamp(start, startTimeHour, startTimeMinute, startTimeAmPm);
            LocalDateTime startTime = start.toLocalDateTime();

            end = Timestamp.valueOf(endDatePicker.getValue().atStartOfDay());
            int endTimeHour = (int)endHourComboBox.getValue();
            int endTimeMinute = (int)endMinuteComboBox.getValue();
            String endTimeAmPm = (String)endAMPMComboBox.getValue();
            end = TimeUtils.buildTimeStamp(end, endTimeHour, endTimeMinute, endTimeAmPm);
            LocalDateTime endTime = end.toLocalDateTime();

            long minutes = ChronoUnit.MINUTES.between(startTime, endTime);
            long hours = ChronoUnit.HOURS.between(startTime, endTime);

            System.out.println("Appointment is " + hours + " hours and " + minutes + " minutes.");

            if(minutes == 0) {
                good = false;
                startLabel.setTextFill(Color.color(1, 0, 0));
                startLabel.setText("Appointment cannot be zero length");
                endLabel.setTextFill(Color.color(1, 0, 0));
                endLabel.setText("Appointment cannot be zero length");
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
            AlertUtil.officeHoursWarning();
            return good;
        }

        boolean isOverlap = DBAppointment.isCollision(appointment);
        System.out.println("isOverlap: " + isOverlap);
        if(isOverlap == false) {
            int customerId = UXUtil.getIdNumberFromComboBox(customerIdComboBox);
            int userId = UXUtil.getIdNumberFromComboBox(userIdComboBox);
            int contactId = UXUtil.getIdNumberFromComboBox(contactIdComboBox);
            DBAppointment.updateAppointment(appointment.getId(), title, description, location, type, start, end, customerId, userId, contactId);

            SceneLoader.goToMainView(actionEvent);
        }

        return good;
    }

}
