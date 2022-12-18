package Controller;

import DBAccess.DBAppointment;
import Model.Appointment;
import Utils.UXUtil;
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
 * This is the controller class for the customer-report.fxml view. You can see this controller set in the root node of that
 * fxml file with this node property:
 * ```
 *      fx:controller="Controller.CustomerReportController"
 * ```
 * This view shows a ComboBox the user can use to select a Customer they want to see a report generated for.
 * The list of all customers is fetched from the database and used to populate this combobox.
 * The report shows in a text format the set of all appointments for that customer.
 *
 */
public class CustomerReportController  implements Initializable {

    /**
     * Allows the user to select the Customer they want to see the report generated for. The report will contain the list
     * of all appointments for the selected customer.
     */
    @FXML
    private ComboBox customerComboBox;

    /**
     * Shows the number of appointments returned for the chosen Customer.
     */
    @FXML
    private Label customerApptNumLabel;

    /**
     * Displays the generated text report showing a list of all the appointments for the chosen customer in no
     * particular order.
     */
    @FXML
    private TextArea reportTextAreaCustomer;

    /**
     * Called when customer-report.fxml is loaded. This function initilizes the Customer ComboBox with the set of all
     * Customers returned from the Database.
     * @param url - not used
     * @param resourceBundle - not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UXUtil.initCustomerIDComboBox(customerComboBox);
    }

    /**
     * Called when the user selects a Customer from the ComboBox. On customer selected, a text report will be generated
     * and displayed in the TextArea showing the set of all appointments for that Customer in no particular order.
     * @param actionEvent - not used
     */
    public void onCustomerSelected(ActionEvent actionEvent) {
        int customerId = UXUtil.getIdNumberFromComboBox(customerComboBox);
        ObservableList<Appointment> appointments = DBAppointment.lookupAppointmentsForCustomer(customerId);
        customerApptNumLabel.setText(Integer.toString(appointments.size()));

        String s = "";
        for(Appointment a : appointments) {
            s += a.toString();
        }
        reportTextAreaCustomer.setText(s);
    }
}
