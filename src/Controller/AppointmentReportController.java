package Controller;

import DBAccess.DBAppointment;
import Model.Appointment;
import Utils.UXUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppointmentReportController implements Initializable {

    @FXML
    private ComboBox apptTypeComboBox;

    @FXML
    private ComboBox apptMonthComboBox;

    @FXML
    private Label numApptsLabel;

    @FXML
    private TextArea reportTextAreaType;

    @FXML
    private TextArea reportTextAreaMonth;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<String> types = FXCollections.observableList(DBAppointment.getUniqueAppointmentTypes());
            apptTypeComboBox.setItems(types);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        UXUtil.initMonthComboBox(apptMonthComboBox);
    }
    public void onTypeSelected(ActionEvent actionEvent) throws IOException, SQLException {
        String type = apptTypeComboBox.getValue().toString();
        ArrayList<Appointment> appointments = DBAppointment.getAllAppointmentsOfType(type);
        numApptsLabel.setText(Integer.toString(appointments.size()));

        String s = "";
        for(Appointment a : appointments) {
            s += a.toString();
        }
        reportTextAreaType.setText(s);
    }

    public void onMonthSelected(ActionEvent actionEvent) throws IOException, SQLException {
        String month = apptMonthComboBox.getValue().toString();
        ArrayList<Appointment> appointments = DBAppointment.getAllAppointmentsInMonth(month);
        numApptsLabel.setText(Integer.toString(appointments.size()));

        String s = "";
        for(Appointment a : appointments) {
            s += a.toString();
        }
        reportTextAreaMonth.setText(s);
    }
}
