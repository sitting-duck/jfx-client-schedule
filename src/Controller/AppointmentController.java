package Controller;

import Utils.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AppointmentController {

    /**
     * The label for the text field where the user enters the Title of the Appointment. eg. "Planning Meeting"
     */
    @FXML
    protected Label titleLabel;

    /**
     * The text field where the user enters the Title of the Appointment eg. "Planning Meeting"
     */
    @FXML
    protected TextField titleTextField;

    /**
     * The label for the text field where the user will enter the description for the Appointment. eg. "We will do
     * planning at this meeting for the Holiday Event"
     */
    @FXML
    protected Label descriptionLabel;

    /**
     * The text field where the user will enter the description for the Appointment. eg. "We will do
     * planning at this meeting for the Holiday Event"
     */
    @FXML
    protected TextField descriptionTextField;

    /**
     * The label for the text field where the user will enter the description for the Appointment. eg. "Room 241"
     */
    @FXML
    protected Label locationLabel;

    /**
     * The text field where the user will enter the description for the Appointment. eg. "Room 241"
     */
    @FXML
    protected TextField locationTextField;

    /**
     * The label for the text field where the user will enter the type for the appointment.
     */
    @FXML
    protected Label typeLabel;

    /**
     * The text field where the user will enter the type for the appointment. eg. "Interview", "Meeting", "Dinner" etc.
     */
    @FXML
    protected TextField typeTextField;

    /**
     * Shows the current time in the time zone the user is in. The user will see Eastern time as
     * well to the right of this label, and this serves to remind the user that the appointment being made will be
     * tested to see if it fits within the offices hours of 8am-10pm in the Eastern Time zone. Since most people don't
     * memorize the time zone differences in their head, these labels will serve as a reminder to help them set good
     * appointment times.
     */
    @FXML
    protected Label localTimeLabel;

    /**
     * Shows the current time in Eastern time. The user will see their own local time as
     * well to the left of this label, and this serves to remind the user that the appointment being made will be
     * tested to see if it fits within the offices hours of 8am-10pm in the Eastern Time zone. Since most people don't
     * memorize the time zone differences in their head, these labels will serve as a reminder to help them set good
     * appointment times.
     */
    @FXML
    protected Label easternTimeLabel;

    /**
     * A label next to the calendar widget the user will use to the set the start time for the appointment they are
     * creating. It is simply labelled "start" and should prompt the user to start thinking about when their appointment
     * should start.
     */
    @FXML
    protected Label startLabel;

    /**
     * A calendar widget that allows the user select the day, month, and year ie. the date that they want their
     * appointment to start on.
     */
    @FXML
    protected DatePicker startDatePicker;

    /**
     * A ComboBox that allows the user to select the hour they want their appointment to start on.
     */
    @FXML
    protected ComboBox startHourComboBox;

    /**
     * A ComboBox that allows the user to select which minute they want their appointment to start on. The options are
     * [0, 15, 30, 45] for convenience. The minimum appointment duration is 15 minutes. It is assumed no users are going
     * to want to create an appointment that is 1 minute long for example.
     */
    @FXML
    protected ComboBox startMinuteComboBox;

    /**
     * A ComboBox that allows the user to select "AM" or "PM" for the Appointment start time of the appointment they are
     * creating. A selection of Hour of 12 with selection "AM" would result in an appointment that starts at midnight,
     * A selection of Hour of 12 with selection "PM" would result in an appointment that starts at noon.
     */
    @FXML
    protected ComboBox startAMPMComboBox;

    /**
     * A label next to the calendar widget the user will use to the set the end time for the appointment they are
     * creating. It is simply labelled "end" and should prompt the user to start thinking about when their appointment
     * should end.
     */
    @FXML
    protected Label endLabel;

    /**
     * A calendar widget that allows the user select the day, month, and year ie. the date that they want their
     * appointment to end on.
     */
    @FXML
    protected DatePicker endDatePicker;

    /**
     * A ComboBox that allows the user to select the hour they want their appointment to end on.
     */
    @FXML
    protected ComboBox endHourComboBox;

    /**
     * A ComboBox that allows the user to select which minute they want their appointment to end on. The options are
     * [0, 15, 30, 45] for convenience. The minimum appointment duration is 15 minutes. It is assumed no users are going
     * to want to create an appointment that is 1 minute long for example.
     */
    @FXML
    protected ComboBox endMinuteComboBox;

    /**
     * A ComboBox that allows the user to select "AM" or "PM" for the Appointment end time of the appointment they are
     * creating. A selection of Hour of 12 with selection "AM" would result in an appointment that ends at midnight,
     * A selection of Hour of 12 with selection "PM" would result in an appointment that ends at noon.
     */
    @FXML
    protected ComboBox endAMPMComboBox;

    /**
     * All label indicating that the ComboBox below it will be used to the select the Customer by ID that will be present
     * at the Appointment being created.
     */
    @FXML
    protected Label customerIdLabel;

    /**
     * A ComboBox used to the select the Customer that will be present at the appointment being created.
     */
    @FXML
    protected ComboBox customerIdComboBox;

    /**
     * A label indicating that the ComboBox below it will be used to select the User associated with the Appointment
     * being created by user id.
     */
    @FXML
    protected Label userIdLabel;

    /**
     * A ComboBox the user will use to select the User associated with the Appointment by user id. This ComboBox is
     * populated with the set of all users available in the database. The ComboBox displays in each row first the user
     * id and then the string for the User_Name. eg. "1: Sally"
     */
    @FXML
    protected ComboBox userIdComboBox;

    /**
     * A label indicating that the ComboBox below it will be used to the select the Contact associated with the
     * Appointment being created by the Contact Id.
     */
    @FXML
    protected Label contactIdLabel;

    /**
     * A ComboBox that the user will use to select the Contact that will be associated with the Appointment being
     * created.
     */
    @FXML
    protected ComboBox contactIdComboBox;

    /**
     * This function runs when the user selects the cancel button in the modify-appointment.fxml view. This function will return
     * the user to the main view in main.fxml and no appointments or appointment edits will be added to the database.
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        SceneLoader.goToMainView(actionEvent);
    }

}
