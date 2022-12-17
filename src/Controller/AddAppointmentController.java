package Controller;

import DBAccess.DBAppointment;
import Model.Appointment;
import Utils.TimeUtils;
import Utils.UXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
public class AddAppointmentController implements Initializable  {

    /**
     * The label for the text field where the user enters the Title of the Appointment. eg. "Planning Meeting"
     */
    @FXML
    private Label titleLabel;

    /**
     * The text field where the user enters the Title of the Appointment eg. "Planning Meeting"
     */
    @FXML
    private TextField titleTextField;

    /**
     * The label for the text field where the user will enter the description for the Appointment. eg. "We will do
     * planning at this meeting for the Holiday Event"
     */
    @FXML
    private Label descriptionLabel;

    /**
     * The text field where the user will enter the description for the Appointment. eg. "We will do
     * planning at this meeting for the Holiday Event"
     */
    @FXML
    private TextField descriptionTextField;

    /**
     * The label for the text field where the user will enter the description for the Appointment. eg. "Room 241"
     */
    @FXML
    private Label locationLabel;

    /**
     * The text field where the user will enter the description for the Appointment. eg. "Room 241"
     */
    @FXML
    private TextField locationTextField;

    /**
     * The label for the text field where the user will enter the type for the appointment.
     */
    @FXML
    private Label typeLabel;

    /**
     * The text field where the user will enter the type for the appointment. eg. "Interview", "Meeting", "Dinner" etc.
     */
    @FXML
    private TextField typeTextField;

    /**
     * Shows the current time in the time zone the user is in. The user will see Eastern time as
     * well to the right of this label, and this serves to remind the user that the appointment being made will be
     * tested to see if it fits within the offices hours of 8am-10pm in the Eastern Time zone. Since most people don't
     * memorize the time zone differences in their head, these labels will serve as a reminder to help them set good
     * appointment times.
     */
    @FXML
    private Label localTimeLabel;

    /**
     * Shows the current time in Eastern time. The user will see their own local time as
     * well to the left of this label, and this serves to remind the user that the appointment being made will be
     * tested to see if it fits within the offices hours of 8am-10pm in the Eastern Time zone. Since most people don't
     * memorize the time zone differences in their head, these labels will serve as a reminder to help them set good
     * appointment times.
     */
    @FXML
    private Label easternTimeLabel;

    /**
     * A label next to the calendar widget the user will use to the set the start time for the appointment they are
     * creating. It is simply labelled "start" and should prompt the user to start thinking about when their appointment
     * should start.
     */
    @FXML
    private Label startLabel;

    /**
     * A calendar widget that allows the user select the day, month, and year ie. the date that they want their
     * appointment to start on.
     */
    @FXML
    private DatePicker startDatePicker;

    /**
     * A ComboBox that allows the user to select the hour they want their appointment to start on.
     */
    @FXML
    private ComboBox startHourComboBox;

    /**
     * A ComboBox that allows the user to select which minute they want their appointment to start on. The options are
     * [0, 15, 30, 45] for convenience. The minimum appointment duration is 15 minutes. It is assumed no users are going
     * to want to create an appointment that is 1 minute long for example.
     */
    @FXML
    private ComboBox startMinuteComboBox;

    /**
     * A ComboBox that allows the user to select "AM" or "PM" for the Appointment start time of the appointment they are
     * creating. A selection of Hour of 12 with selection "AM" would result in an appointment that starts at midnight,
     * A selection of Hour of 12 with selection "PM" would result in an appointment that starts at noon.
     */
    @FXML
    private ComboBox startAMPMComboBox;

    /**
     * A label next to the calendar widget the user will use to the set the end time for the appointment they are
     * creating. It is simply labelled "end" and should prompt the user to start thinking about when their appointment
     * should end.
     */
    @FXML
    private Label endLabel;

    /**
     * A calendar widget that allows the user select the day, month, and year ie. the date that they want their
     * appointment to end on.
     */
    @FXML
    private DatePicker endDatePicker;

    /**
     * A ComboBox that allows the user to select the hour they want their appointment to end on.
     */
    @FXML
    private ComboBox endHourComboBox;

    /**
     * A ComboBox that allows the user to select which minute they want their appointment to end on. The options are
     * [0, 15, 30, 45] for convenience. The minimum appointment duration is 15 minutes. It is assumed no users are going
     * to want to create an appointment that is 1 minute long for example.
     */
    @FXML
    private ComboBox endMinuteComboBox;

    /**
     * A ComboBox that allows the user to select "AM" or "PM" for the Appointment end time of the appointment they are
     * creating. A selection of Hour of 12 with selection "AM" would result in an appointment that ends at midnight,
     * A selection of Hour of 12 with selection "PM" would result in an appointment that ends at noon.
     */
    @FXML
    private ComboBox endAMPMComboBox;

    /**
     * All label indicating that the ComboBox below it will be used to the select the Customer by ID that will be present
     * at the Appointment being created.
     */
    @FXML
    private Label customerIdLabel;

    /**
     * A ComboBox used to the select the Customer that will be present at the appointment being created.
     */
    @FXML
    private ComboBox customerIdComboBox;

    /**
     * A label indicating that the ComboBox below it will be used to select the User associated with the Appointment
     * being created by user id.
     */
    @FXML
    private Label userIdLabel;

    /**
     * A ComboBox the user will use to select the User associated with the Appointment by user id. This ComboBox is
     * populated with the set of all users available in the database. The ComboBox displays in each row first the user
     * id and then the string for the User_Name. eg. "1: Sally"
     */
    @FXML
    private ComboBox userIdComboBox;

    /**
     * A label indicating that the ComboBox below it will be used to the select the Contact associated with the
     * Appointment being created by the Contact Id.
     */
    @FXML
    private Label contactIdLabel;

    /**
     * A ComboBox that the user will use to select the Contact that will be associated with the Appointment being
     * created.
     */
    @FXML
    private ComboBox contactIdComboBox;

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

        titleLabel.setText("Title");
        descriptionLabel.setText("Description");
        locationLabel.setText("Location");
        typeLabel.setText("Type");
        startLabel.setText("Start");
        endLabel.setText("End");
        customerIdLabel.setText("Customer ID");
        userIdLabel.setText("User ID");
        contactIdLabel.setText("Contact ID");

        UXUtil.initAppointmentWidget(startDatePicker, startHourComboBox, startMinuteComboBox, startAMPMComboBox);
        UXUtil.initAppointmentWidget(endDatePicker, endHourComboBox, endMinuteComboBox, endAMPMComboBox);
        UXUtil.initCustomerIDComboBox(customerIdComboBox);
        UXUtil.initUserIDComboBox(userIdComboBox);
        UXUtil.initContactIDComboBox(contactIdComboBox);

        localTimeLabel.setText(TimeUtils.getNowLocalTimeString());
        easternTimeLabel.setText(TimeUtils.getNowEasternTimeString());
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
    public void onOkButton(ActionEvent actionEvent) throws IOException, SQLException {
        boolean good = true;
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();

        Timestamp start = null;
        Timestamp end = null;

        good = UXUtil.getSelectionFromComboBox(customerIdComboBox, customerIdLabel, "Customer");
        good = UXUtil.getSelectionFromComboBox(userIdComboBox, userIdLabel, "User");
        good = UXUtil.getSelectionFromComboBox(contactIdComboBox, contactIdLabel, "Contact");

        if(description.compareTo("") == 0) {
            descriptionLabel.setTextFill(Color.color(1, 0, 0));
            descriptionLabel.setText("Description Cannot Be Empty");
            good = false;
        } else {
            descriptionLabel.setTextFill(Color.color(0, 0, 0));
            descriptionLabel.setText("Decsription");
        }
        if(title.compareTo("") == 0) {
            titleLabel.setTextFill(Color.color(1, 0, 0));
            titleLabel.setText("Title Cannot Be Empty");
            good = false;
        } else {
            titleLabel.setTextFill(Color.color(0, 0, 0));
            titleLabel.setText("Title");
        }
        if(location.compareTo("") == 0) {
            locationLabel.setTextFill(Color.color(1, 0, 0));
            locationLabel.setText("Location Cannot Be Empty");
            good = false;
        } else {
            locationLabel.setTextFill(Color.color(0, 0, 0));
            locationLabel.setText("Location");
        }
        if(type.compareTo("") == 0) {
            typeLabel.setTextFill(Color.color(1, 0, 0));
            typeLabel.setText("Type Cannot Be Empty");
            good = false;
        } else {
            typeLabel.setTextFill(Color.color(0, 0, 0));
            typeLabel.setText("Type");
        }
        if(startDatePicker.getValue() == null) {
            startLabel.setTextFill(Color.color(1, 0, 0));
            startLabel.setText("Start Date Cannot Be Empty");
            good = false;
        } else {
            startLabel.setTextFill(Color.color(0, 0, 0));
            startLabel.setText("Start");
        }
        if(startHourComboBox.getValue() == null) {
            startLabel.setTextFill(Color.color(1, 0, 0));
            startLabel.setText("Start Hour Cannot Be Empty");
            good = false;
        } else {
            startLabel.setTextFill(Color.color(0, 0, 0));
            startLabel.setText("Start");
        }
        if(startMinuteComboBox.getValue() == null) {
            startLabel.setTextFill(Color.color(1, 0, 0));
            startLabel.setText("Start Minute Cannot Be Empty");
            good = false;
        } else {
            startLabel.setTextFill(Color.color(0, 0, 0));
            startLabel.setText("Start");
        }
        if(startAMPMComboBox.getValue() == null) {
            startLabel.setTextFill(Color.color(1, 0, 0));
            startLabel.setText("Start AM/PM Cannot Be Empty");
            good = false;
        } else {
            startLabel.setTextFill(Color.color(0, 0, 0));
            startLabel.setText("Start");
        }

        if(endDatePicker.getValue() == null) {
            endLabel.setTextFill(Color.color(1, 0, 0));
            endLabel.setText("End Date Cannot Be Empty");
            good = false;
        } else {
            endLabel.setTextFill(Color.color(0, 0, 0));
            endLabel.setText("End");
        }

        if(endHourComboBox.getValue() == null) {
            endLabel.setTextFill(Color.color(1, 0, 0));
            endLabel.setText("End  Hour Cannot Be Empty");
            good = false;
        } else {
            endLabel.setTextFill(Color.color(0, 0, 0));
            endLabel.setText("End");
        }
        if(endMinuteComboBox.getValue() == null) {
            endLabel.setTextFill(Color.color(1, 0, 0));
            endLabel.setText("End Minute Cannot Be Empty");
            good = false;
        } else {
            endLabel.setTextFill(Color.color(0, 0, 0));
            endLabel.setText("End");
        }
        if(endAMPMComboBox.getValue() == null) {
            endLabel.setTextFill(Color.color(1, 0, 0));
            endLabel.setText("End AM/PM Cannot Be Empty");
            good = false;
        } else {
            endLabel.setTextFill(Color.color(0, 0, 0));
            endLabel.setText("End");
        }

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

        } catch(Exception e) {
            good = false;
        }

        if(good == false) {
            System.out.println("Input was not valid, Appointment NOT updated in database.");
            return;
        }

        boolean withinOfficeHours = TimeUtils.withinOfficeHours(start, end);
        if(!withinOfficeHours) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: office hours");
            alert.setHeaderText("Error: office hours are 8am-10pm EST");
            alert.setContentText("Error: offices hours are 8am-10pm ESTError: offices 8am-10pm EST");
            alert.showAndWait();
            return;
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
            return;
        }
    }

    /**
     * This function runs when the user selects the cancel button in the AddAppointment view. This function will return
     * the user to the main view in main.fxml and no appointments will be added to the database.
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 400);
        stage.setTitle("Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }
}
