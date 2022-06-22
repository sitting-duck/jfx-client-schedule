package Controller;

import DBAccess.DBAppointment;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddAppointmentController implements Initializable  {
    @FXML
    private Label titleLabel;

    @FXML
    private TextField titleTextField;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Label locationLabel;

    @FXML
    private TextField locationTextField;

    @FXML
    private Label typeLabel;

    @FXML
    private TextField typeTextField;

    @FXML
    private Label startLabel;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox startHourComboBox;

    @FXML
    private ComboBox startMinuteComboBox;

    @FXML
    private ComboBox startAMPMComboBox;

    @FXML
    private Label endLabel;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox endHourComboBox;

    @FXML
    private ComboBox endMinuteComboBox;

    @FXML
    private ComboBox endAMPMComboBox;

    @FXML
    private Label customerIdLabel;

    @FXML
    private ComboBox customerIdComboBox;

    @FXML
    private Label userIdLabel;

    @FXML
    private ComboBox userIdComboBox;

    @FXML
    private Label contactIdLabel;

    @FXML
    private ComboBox contactIdComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalTime start = LocalTime.of(1, 0);
        LocalTime end = LocalTime.of(12, 0);

        while(start.isBefore(end.plusSeconds(1))) {
            startHourComboBox.getItems().add(start);
            start = start.plusHours(1);
        }
        startHourComboBox.getSelectionModel().select(LocalTime.of(9, 0));

        ObservableList<Integer> hours = FXCollections.observableList(IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList()));
        startHourComboBox.setPromptText("Hour");
        startHourComboBox.setVisibleRowCount(12);
        startHourComboBox.setItems(hours);
        endHourComboBox.setPromptText("Hour");
        endHourComboBox.setVisibleRowCount(12);
        endHourComboBox.setItems(hours);

        ArrayList minuteIncrement15 = new ArrayList<Integer>();
        minuteIncrement15.add(0);
        minuteIncrement15.add(15);
        minuteIncrement15.add(30);
        minuteIncrement15.add(45);
        ObservableList<Integer> minutes = FXCollections.observableList(minuteIncrement15);
        startMinuteComboBox.setPromptText("Minute");
        startMinuteComboBox.setVisibleRowCount(4);
        startMinuteComboBox.setItems(minutes);
        endMinuteComboBox.setPromptText("Minute");
        endMinuteComboBox.setVisibleRowCount(4);
        endMinuteComboBox.setItems(minutes);

        ArrayList amPMList = new ArrayList<String>();
        amPMList.add("AM");
        amPMList.add("PM");
        ObservableList<String> amPm = FXCollections.observableList(amPMList);
        startAMPMComboBox.setPromptText("AM/PM");
        startAMPMComboBox.setVisibleRowCount(2);
        startAMPMComboBox.setItems(amPm);
        endAMPMComboBox.setPromptText("AM/PM");
        endAMPMComboBox.setVisibleRowCount(2);
        endAMPMComboBox.setItems(amPm);

    }

    public void onOkButton(ActionEvent actionEvent) throws IOException, SQLException {
        boolean good = true;
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        Date start = new Date(2022, 6, 16); //todo
        Date end = new Date(2022, 6, 16); // todo

        int customerId = (int) customerIdComboBox.getSelectionModel().getSelectedItem();
        int userId = (int) userIdComboBox.getSelectionModel().getSelectedItem();
        int contactId = (int) contactIdComboBox.getSelectionModel().getSelectedItem();

        if(description.compareTo("") == 0) {
            descriptionLabel.setTextFill(Color.color(1, 0, 0));
            descriptionLabel.setText("Cannot be empty");
            good = false;
        }
        if(title.compareTo("") == 0) {
            titleLabel.setTextFill(Color.color(1, 0, 0));
            titleLabel.setText("Cannot be empty");
            good = false;
        }
        if(location.compareTo("") == 0) {
            locationLabel.setTextFill(Color.color(1, 0, 0));
            locationLabel.setText("Cannot be empty");
            good = false;
        }
        if(type.compareTo("") == 0) {
            typeLabel.setTextFill(Color.color(1, 0, 0));
            typeLabel.setText("Cannot be empty");
            good = false;
        }

        if(good == false) {
            System.out.println("Input was not valid, Appointment NOT updated in database.");
            return;
        }
        DBAppointment.insertAppointment(title, description, location, type, start, end, customerId, userId, contactId);

        Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 400);
        stage.setTitle("Appointment Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }

    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 400);
        stage.setTitle("Appointment Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }
}
