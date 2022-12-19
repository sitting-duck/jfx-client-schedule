package Controller;

import DBAccess.DBAppointment;
import Model.Appointment;
import Utils.SceneLoader;
import Utils.UXUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This is the controller class for the appt-reports.fxml view. You can see this controller set in the root node of that
 * fxml file with this node property:
 * ```
 *      fx:controller="Controller.AppointmentReportController"
 * ```
 *
 *  This view has a few tabs, where the user can select what they want to see and see a text string report of the results
 *  fetched from the database. For example, users can view a report generated for appointments by Type, by Month, and/or
 *  by Contact.
 *
 */
public class AppointmentReportController implements Initializable {

    /**
     * Allows the user to select appointment Type and see all appointments with matching type shown in a text format
     * below the ComboBox
     */
    @FXML
    private ComboBox apptTypeComboBox;

    /**
     * Allows the user to select appointment month and see all appointments with matching month shown in a text format
     * below the ComboBox
     */
    @FXML
    private ComboBox apptMonthComboBox;

    /**
     * Allows the user to select appointment Contact and see all appointments with matching Contact shown in a text format
     * below the ComboBox
     */
    @FXML
    private ComboBox contactComboBox;

    /**
     * Shows the number of appointments returned matching chosen Type in integer form.
     */
    @FXML
    private Label numApptsLabelType;

    /**
     * Shows the number of appointments returned matching chosen month in integer form.
     */
    @FXML
    private Label numApptsLabelMonth;

    /**
     * Shows the number of appointments returned matching chosen Contact in integer form.
     */
    @FXML
    private Label numApptsLabelContact;

    /**
     * The text area showing the report for appointments matching the chosen Type
     */
    @FXML
    private TextArea reportTextAreaType;

    /**
     * The text area showing the report for appointments matching the chosen month
     */
    @FXML
    private TextArea reportTextAreaMonth;

    /**
     * The text area showing the report for appointments matching the chosen Contact
     */
    @FXML
    private TextArea reportTextAreaContact;

    /**
     * Called when the appt-reports.fxml file is loaded. This function initializes some view items with list of
     * Strings created from items returned from the database.
     * @param url - not used
     * @param resourceBundle - not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Init Type ComboBox
        try {
            ObservableList<String> types = FXCollections.observableList(DBAppointment.getUniqueAppointmentTypes());
            apptTypeComboBox.setItems(types);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // init month and contact combobox
        UXUtil.initMonthComboBox(apptMonthComboBox);
        UXUtil.initContactComboBox(contactComboBox);
    }

    /**
     * Called after user selects a Type from the ComboBox. The appropriate TextArea will be populated with a text
     * report of all the appointments returned from the database that match the chosen type.
     *
     * @param actionEvent - not used
     * @throws SQLException - thrown if database cannot find a match
     */
    public void onTypeSelected(ActionEvent actionEvent) throws SQLException {
        String type = apptTypeComboBox.getValue().toString();
        ArrayList<Appointment> appointments = DBAppointment.getAllAppointmentsOfType(type);
        numApptsLabelType.setText(Integer.toString(appointments.size()));

        String s = "";
        for(Appointment a : appointments) {
            s += a.toString();
        }
        reportTextAreaType.setText(s);
    }

    /**
     * Called after user selects a month from the ComboBox. The appropriate TextArea will be populated with a text
     * report of all the appointments returned from the database that match the chosen month.
     *
     * @param actionEvent - not used
     * @throws SQLException - thrown if database cannot find a match
     */
    public void onMonthSelected(ActionEvent actionEvent) throws SQLException {
        String month = apptMonthComboBox.getValue().toString();
        ArrayList<Appointment> appointments = DBAppointment.getAllAppointmentsInMonth(month);
        numApptsLabelMonth.setText(Integer.toString(appointments.size()));

        String s = "";
        for(Appointment a : appointments) {
            s += a.toString();
        }
        reportTextAreaMonth.setText(s);
    }

    /**
     * Called after user selects a Contact from the ComboBox. The appropriate TextArea will be populated with a text
     * report of all the appointments returned from the database that match the chosen Contact.
     *
     * @param actionEvent - not used
     * @throws SQLException - thrown if database cannot find a match
     */
    public void onContactSelected(ActionEvent actionEvent) throws SQLException {
        int contactId = UXUtil.getIdNumberFromComboBox(contactComboBox);
        ArrayList<Appointment> appointments = DBAppointment.getAllAppointmentsForContact(contactId);
        numApptsLabelContact.setText(Integer.toString(appointments.size()));
        String s = "";
        for(Appointment a : appointments) {
            s += a.toString();
        }
        reportTextAreaContact.setText(s);
    }

    public void onHomeButtonClicked(ActionEvent actionEvent) throws IOException {
        SceneLoader.goToMainView(actionEvent);
    }
}
