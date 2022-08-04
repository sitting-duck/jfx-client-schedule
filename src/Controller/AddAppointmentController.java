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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
    }

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
        }
    }

    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 400);
        stage.setTitle("Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }
}
