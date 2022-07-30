package Controller;

import DBAccess.DBAppointment;
import DBAccess.DBContact;
import DBAccess.DBCustomer;
import DBAccess.DBUser;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.User;
import Utils.TimeUtils;
import Utils.UXUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModifyAppointmentController implements Initializable  {

    private static Appointment appointment = null;

    @FXML
    private TextField idTextField;
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

    public static void setAppointment(Appointment _appointment) {
        appointment = _appointment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(appointment == null) {
            System.out.println("Error: no appointment selected");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No appointment selected");
            alert.setHeaderText("No appointment selected");
            alert.setContentText("No appointment selected. Please select a appointment to modify.");
            alert.showAndWait();
        }

        // Init UI
        UXUtil.initAppointmentWidget(startDatePicker, startHourComboBox, startMinuteComboBox, startAMPMComboBox);
        UXUtil.initAppointmentWidget(endDatePicker, endHourComboBox, endMinuteComboBox, endAMPMComboBox);
        UXUtil.initCustomerIDComboBox(customerIdComboBox);
        UXUtil.initUserIDComboBox(userIdComboBox);
        UXUtil.initContactIDComboBox(contactIdComboBox);

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
        startHour = TimeUtils.militaryToCivilianHour(startHour);
        startHourComboBox.setValue(startHour);
        int endHour = appointment.getEnd().toLocalDateTime().getHour();
        endHour = TimeUtils.militaryToCivilianHour(endHour);
        endHourComboBox.setValue(endHour);

        startMinuteComboBox.setValue(appointment.getStart().toLocalDateTime().getMinute());
        endMinuteComboBox.setValue(appointment.getEnd().toLocalDateTime().getMinute());
        startAMPMComboBox.setValue(TimeUtils.getAMPMFromHour(startHour));
        endAMPMComboBox.setValue(TimeUtils.getAMPMFromHour(endHour));
        customerIdComboBox.setValue(appointment.getCustomerId());
        userIdComboBox.setValue(appointment.getUserId());
        contactIdComboBox.setValue(appointment.getContactId());
    }

    public void onOkButton(ActionEvent actionEvent) throws IOException, SQLException {
        boolean good = true;
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();

        Timestamp start = null;
        Timestamp end = null;

        int customerId = -1;
        if(customerIdComboBox.getSelectionModel().getSelectedItem() != null) {
            customerId = (int) customerIdComboBox.getSelectionModel().getSelectedItem();
        }
        if(customerId == -1) {
            customerIdLabel.setTextFill(Color.color(1, 0, 0));
            customerIdLabel.setText("Cannot be empty");
            good = false;
        }

        int userId = -1;
        if(userIdComboBox.getSelectionModel().getSelectedItem() != null) {
            userId = (int) userIdComboBox.getSelectionModel().getSelectedItem();
        }
        if(userId == -1) {
            userIdLabel.setTextFill(Color.color(1, 0, 0));
            userIdLabel.setText("Cannot be empty");
            good = false;
        }

        int contactId = -1;
        if(contactIdComboBox.getSelectionModel().getSelectedItem() != null) {
            contactId = (int) contactIdComboBox.getSelectionModel().getSelectedItem();
        }
        if(contactId == -1) {
            contactIdLabel.setTextFill(Color.color(1, 0, 0));
            contactIdLabel.setText("Cannot be empty");
            good = false;
        }

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

        if(startDatePicker.getValue() == null) {
            startLabel.setTextFill(Color.color(1, 0, 0));
            startLabel.setText("Cannot be empty");
            good = false;
        }
        if(startHourComboBox.getValue() == null) {
            startLabel.setTextFill(Color.color(1, 0, 0));
            startLabel.setText("Cannot be empty");
            good = false;
        }
        if(startMinuteComboBox.getValue() == null) {
            startLabel.setTextFill(Color.color(1, 0, 0));
            startLabel.setText("Cannot be empty");
            good = false;
        }
        if(startAMPMComboBox.getValue() == null) {
            startLabel.setTextFill(Color.color(1, 0, 0));
            startLabel.setText("Cannot be empty");
            good = false;
        }

        if(endDatePicker.getValue() == null) {
            endLabel.setTextFill(Color.color(1, 0, 0));
            endLabel.setText("Cannot be empty");
            good = false;
        }
        if(endHourComboBox.getValue() == null) {
            endLabel.setTextFill(Color.color(1, 0, 0));
            endLabel.setText("Cannot be empty");
            good = false;
        }
        if(endMinuteComboBox.getValue() == null) {
            endLabel.setTextFill(Color.color(1, 0, 0));
            endLabel.setText("Cannot be empty");
            good = false;
        }
        if(endAMPMComboBox.getValue() == null) {
            endLabel.setTextFill(Color.color(1, 0, 0));
            endLabel.setText("Cannot be empty");
            good = false;
        }

        try {
            start = Timestamp.valueOf(startDatePicker.getValue().atStartOfDay());
            int startTimeHour = (int)startHourComboBox.getValue();
            int startTimeMinute = (int)startMinuteComboBox.getValue();
            String startTimeAmPm = (String)startAMPMComboBox.getValue();
            start = TimeUtils.getTime(start, startTimeHour, startTimeMinute, startTimeAmPm);
            LocalDateTime startTime = start.toLocalDateTime();

            end = Timestamp.valueOf(endDatePicker.getValue().atStartOfDay());
            int endTimeHour = (int)endHourComboBox.getValue();
            int endTimeMinute = (int)endMinuteComboBox.getValue();
            String endTimeAmPm = (String)endAMPMComboBox.getValue();
            end = TimeUtils.getTime(end, endTimeHour, endTimeMinute, endTimeAmPm);
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
            return;
        }

        boolean isOverlap = DBAppointment.isCollision(appointment);
        System.out.println("isOverlap: " + isOverlap);
        if(isOverlap == false) {
            DBAppointment.updateAppointment(appointment.getId(), title, description, location, type, start, end, customerId, userId, contactId);

            Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1400, 400);
            stage.setTitle("Appointment Manager");
            stage.setScene(scene);
            stage.show();
        }

    }

    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 400);
        stage.setTitle("Appointment Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }

}
