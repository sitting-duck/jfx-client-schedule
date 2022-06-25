package Utils;

import DBAccess.DBContact;
import DBAccess.DBCustomer;
import DBAccess.DBUser;
import Model.Contact;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UXUtil {

    public static void initAppointmentWidget(DatePicker dp, ComboBox hour, ComboBox minute, ComboBox amPm) {
        initDatePicker(dp);
        initHourComboBox(hour);
        initMinuteComboBox(minute);
        initAMPMComboBox(amPm);
    }
    public static void initDatePicker(DatePicker dp) {
        dp.getEditor().setEditable(false);
        dp.getEditor().setDisable(true);
        dp.getEditor().setOnKeyTyped(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                System.out.println(keyEvent.getText());
            }
        });
    }
    public static void initHourComboBox(ComboBox cb) {
        ObservableList<Integer> hours = FXCollections.observableList(IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList()));
        cb.setPromptText("Hour");
        cb.setVisibleRowCount(12);
        cb.setItems(hours);
    }

    public static void initMinuteComboBox(ComboBox cb) {
        ArrayList minuteIncrement15 = new ArrayList<Integer>();
        minuteIncrement15.add(0);
        minuteIncrement15.add(15);
        minuteIncrement15.add(30);
        minuteIncrement15.add(45);
        ObservableList<Integer> minutes = FXCollections.observableList(minuteIncrement15);
        cb.setPromptText("Minute");
        cb.setVisibleRowCount(4);
        cb.setItems(minutes);
    }

    public static void initAMPMComboBox(ComboBox cb) {
        ArrayList amPMList = new ArrayList<String>();
        amPMList.add("AM");
        amPMList.add("PM");
        ObservableList<String> amPm = FXCollections.observableList(amPMList);
        cb.setPromptText("AM/PM");
        cb.setVisibleRowCount(2);
        cb.setItems(amPm);
    }

    public static void initCustomerIDComboBox(ComboBox cb) {
        ArrayList customerIdList = new ArrayList<Integer>();
        for(Customer c: DBCustomer.getAllCustomers()) {
            customerIdList.add(c.getId());
        }
        ObservableList<Integer> customerIds = FXCollections.observableList(customerIdList);
        cb.setItems(customerIds);
    }

    public static void initUserIDComboBox(ComboBox cb) {
        ArrayList userIdList = new ArrayList<Integer>();
        for(User u: DBUser.getAllUsers()) {
            userIdList.add(u.getId());
        }
        ObservableList<Integer> userIds = FXCollections.observableList(userIdList);
        cb.setItems(userIds);
    }

    public static void initContactIDComboBox(ComboBox cb) {
        ArrayList contactIdList = new ArrayList<Integer>();
        for(Contact c: DBContact.getAllContacts()) {
            contactIdList.add(c.getId());
        }
        ObservableList<Integer> contactIds = FXCollections.observableList(contactIdList);
        cb.setItems(contactIds);
    }
}
