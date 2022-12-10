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

public class CustomerReportController  implements Initializable {

    @FXML
    private ComboBox customerComboBox;

    @FXML
    private Label customerApptNumLabel;

    @FXML
    private TextArea reportTextAreaCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UXUtil.initCustomerIDComboBox(customerComboBox);
    }

    public void onCustomerSelected(ActionEvent actionEvent) throws IOException, SQLException {
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
