package Controller;

import DBAccess.DBAppointment;
import Model.Appointment;
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
    private Label numApptsLabel;

    @FXML
    private TextArea reportTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<String> types = FXCollections.observableList(DBAppointment.getUniqueAppointmentTypes());
            apptTypeComboBox.setItems(types);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void onTypeSelected(ActionEvent actionEvent) throws IOException, SQLException {
        String type = apptTypeComboBox.getValue().toString();
        ArrayList<Appointment> appointments = DBAppointment.getAllAppointmentsOfType(type);
        numApptsLabel.setText(Integer.toString(appointments.size()));

        String s = "";
        for(Appointment a : appointments) {
            s += a.toString();
        }

        reportTextArea.setText(s);
    }

}
