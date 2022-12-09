package Controller;

import DBAccess.DBAppointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentReportController implements Initializable {

    @FXML
    private ComboBox apptTypeComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<String> types = FXCollections.observableList(DBAppointment.getUniqueAppointmentTypes());
            apptTypeComboBox.setItems(types);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void onApptsByTypeTabSelected(ActionEvent actionEvent) throws IOException, SQLException {

    }

}
