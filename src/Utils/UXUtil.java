package Utils;

import DBAccess.*;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
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
    public static void initWeekComboBox(ComboBox cb) {
        cb.setPromptText("Week");
        cb.setVisibleRowCount(4);
        ObservableList<String> weeks = FXCollections.observableArrayList();
        weeks.add("All");
        weeks.add("1");
        weeks.add("2");
        weeks.add("3");
        weeks.add("4");
        cb.setItems(weeks);
    }

    public static boolean isMonth(String string) {
        return string.equals("January") || string.equals("February") || string.equals("March") || string.equals("April") || string.equals("May") || string.equals("June") || string.equals("July") || string.equals("August") || string.equals("September") || string.equals("October") || string.equals("Novemeber") || string.equals("December");
    }

    public static Integer getMonthAsNumber(String string) {
        switch(string) {
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
            default:
                return -1;
        }
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
        ArrayList combinedStringList = new ArrayList<String>(); // customer id + customer name in single string
        for(Customer c: DBCustomer.getAllCustomers()) {
            combinedStringList.add(c.getId() + ": " + c.getName());
        }
        ObservableList<String> combinedStrings = FXCollections.observableList(combinedStringList);
        cb.setItems(combinedStrings);
    }

    public static void initCountryComboBox(ComboBox cb) {
        ArrayList combinedStringList = new ArrayList<String>();
        for(Country c: DBCountry.getAllCountries()) {
            combinedStringList.add(c.getId() + ": " + c.getName());
        }
        ObservableList<String> stringList = FXCollections.observableList(combinedStringList);
        cb.setItems(stringList);
    }

    public static void initDivisionIdComboBox(ComboBox cb, String countryString) {
        ArrayList combinedStringList = new ArrayList<String>();
        for(Division d: DBDivision.getAllDivisionsWithCountryName(countryString)) {
            combinedStringList.add(d.getId() + ": " + d.getDivision());
        }
        ObservableList<String> stringList = FXCollections.observableList(combinedStringList);
        cb.setItems(stringList);
    }

    public static void initUserIDComboBox(ComboBox cb) {
        ArrayList combinedStringList = new ArrayList<String>(); // customer id + customer name in single string
        for(User u: DBUser.getAllUsers()) {
            combinedStringList.add(u.getId() + ": " + u.getName());
        }
        ObservableList<Integer> combinedStrings = FXCollections.observableList(combinedStringList);
        cb.setItems(combinedStrings);
    }

    public static void initContactIDComboBox(ComboBox cb) {
        ArrayList combinedStringList = new ArrayList<String>(); // contact id + contact name in single string
        for(Contact c: DBContact.getAllContacts()) {
            combinedStringList.add(c.getId() + ": " + c.getName());
        }
        ObservableList<String> combinedStrings = FXCollections.observableList(combinedStringList);
        cb.setItems(combinedStrings);
    }

    /**
     * returns false if anything doesn't check out, returns true when it's safe to accept the customer being added or
     * modified. When the user clicks ok, we get the selection from the customer id combobox. There must be a selection
     * if we are trying to add to the database. If user just hit cancel, this doesn't matter.
     *
     * Takes a reference to the customer id combo box itself and the customer id combobox label as well.
     * When something is bad, we also set the text for the label as well as return false.
     * @param cb
     * @return
     */
    public static Boolean getSelectionFromComboBox(ComboBox cb, Label lbl, String lblText) {
        int valid = -1;
        if(cb.getSelectionModel().getSelectedItem() != null) {
            valid = Integer.parseInt(cb.getSelectionModel().getSelectedItem().toString().split(" ")[0].replace(":", ""));
        }
        if(valid == -1) {
            lbl.setTextFill(Color.color(1, 0, 0));
            lbl.setText("Cannot be empty");
            return false;
        } else {
            lbl.setTextFill(Color.color(0, 0, 0));
            lbl.setText(lblText);
            return true;
        }
    }

    public static int getIdNumberFromComboBox(ComboBox cb) {
        return Integer.parseInt(cb.getSelectionModel().getSelectedItem().toString().split(" ")[0].replace(":", ""));
    }

    public static String getStringFromComboBox(ComboBox cb) throws Exception {
        return cb.getSelectionModel().getSelectedItem().toString().split(" ")[1].replace(":", "");
    }

    public static void setErrorLabel(Label label) {
        label.setTextFill(Color.color(1, 0, 0));
        label.setText("Cannot be empty");
    }

    public static void initMonthComboBox(ComboBox cb) {
        ArrayList<String> m = new ArrayList<String>();
        m.add("January");
        m.add("February");
        m.add("March");
        m.add("April");
        m.add("May");
        m.add("June");
        m.add("July");
        m.add("August");
        m.add("September");
        m.add("October");
        m.add("November");
        m.add("December");
        ObservableList<String> ol = FXCollections.observableArrayList(m);
        cb.setItems(ol);
    }

    public static void initContactComboBox(ComboBox cb) {
        ObservableList<Contact> contacts = DBContact.getAllContacts();

        ArrayList<String> strings = new ArrayList<String>();
        String s;
        for (Contact contact : contacts) {
            s = contact.getId() + ": " + contact.getName();
            strings.add(s);
        }
        cb.setItems(FXCollections.observableArrayList(strings));
    }
}
