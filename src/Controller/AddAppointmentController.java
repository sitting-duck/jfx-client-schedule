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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

        // Init Date pickers
        startDatePicker.getEditor().setEditable(false);
        endDatePicker.getEditor().setEditable(false);
        startDatePicker.getEditor().setDisable(true);
        endDatePicker.getEditor().setDisable(true);
        startDatePicker.getEditor().setOnKeyTyped(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                System.out.println(keyEvent.getText());
            }
        });

        endDatePicker.getEditor().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                System.out.println(keyEvent.getText());
            }
        });

        // Populate time pickers
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

        // Populate customer ID ComboBox
        ArrayList customerIdList = new ArrayList<Integer>();
        for(Customer c: DBCustomer.getAllCustomers()) {
            customerIdList.add(c.getId());
        }
        ObservableList<Integer> customerIds = FXCollections.observableList(customerIdList);
        customerIdComboBox.setItems(customerIds);

        // Populate User ID ComboBox
        ArrayList userIdList = new ArrayList<Integer>();
        for(User u: DBUser.getAllUsers()) {
            userIdList.add(u.getId());
        }
        ObservableList<Integer> userIds = FXCollections.observableList(userIdList);
        userIdComboBox.setItems(userIds);

        // Populate Contact ID ComboBox
        ArrayList contactIdList = new ArrayList<Integer>();
        for(Contact c: DBContact.getAllContacts()) {
            contactIdList.add(c.getId());
        }
        ObservableList<Integer> contactIds = FXCollections.observableList(contactIdList);
        contactIdComboBox.setItems(contactIds);

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

        } catch(Exception e) {
            good = false;
        }

        if(good == false) {
            System.out.println("Input was not valid, Appointment NOT updated in database.");
            return;
        }

        Appointment newAppointment = new Appointment(0, title, description, location, type, start, end, customerId, userId, contactId);
        boolean isOverlap = DBAppointment.isCollision(newAppointment);
        System.out.println("isOverlap: " + isOverlap);
        if(isOverlap == false) {
            DBAppointment.insertAppointment(title, description, location, type, start, end, customerId, userId, contactId);

            Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 400);
            stage.setTitle("Appointment Manager");
            stage.setScene(scene);
            stage.show();
        }
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
