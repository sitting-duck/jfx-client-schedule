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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A Convenience class for initializing various GUI elements
 */
public class UXUtil {

    /**
     * Initializes a multi component date picker
     * @param dp - A Calendar date picker
     * @param hour - a ComboBox containing the list of hours 1-12
     * @param minute - A ComboBox containing [0, 15, 30, 45]
     * @param amPm - A ComboBox containing ["AM", "PM"]
     */
    public static void initAppointmentWidget(DatePicker dp, ComboBox hour, ComboBox minute, ComboBox amPm) {
        initDatePicker(dp);
        initHourComboBox(hour);
        initMinuteComboBox(minute);
        initAMPMComboBox(amPm);
    }

    /**
     * Initializes calendar date pickers so that the user cannot type into the text field, which can cause an
     * exception if the user enters an invalid string. For convenience, we just disable text input instead of writing
     * Error checking code for the text field.
     * @param dp - Calendar date picker
     */
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

    /**
     * Sets a ComboBox meant to allow the user to choose hours from 1 to 12
     * @param cb - ComboBox
     */
    public static void initHourComboBox(ComboBox cb) {
        ObservableList<Integer> hours = FXCollections.observableList(IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList()));
        cb.setPromptText("Hour");
        cb.setVisibleRowCount(12);
        cb.setItems(hours);
    }

    /**
     * Checks a string to see if it is a valid month name. Case-insensitive.
     * @param string - Month string
     * @return a boolean indicating if the Month is valid.
     */
    public static boolean isMonth(String string) {
        return string.equals("January") || string.equals("February") || string.equals("March") || string.equals("April") || string.equals("May") || string.equals("June") || string.equals("July") || string.equals("August") || string.equals("September") || string.equals("October") || string.equals("Novemeber") || string.equals("December");
    }

    /**
     * Initializes a combo box meant to show options for 15 minute increments for setting appointment times.
     * @param cb - ComboBox to initialize
     */
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

    /**
     * Initialize a ComboBox with the valies of "AM" and "PM" to allow a user to set a time in AM or PM
     * @param cb - ComboBox to initialize.
     */
    public static void initAMPMComboBox(ComboBox cb) {
        ArrayList amPMList = new ArrayList<String>();
        amPMList.add("AM");
        amPMList.add("PM");
        ObservableList<String> amPm = FXCollections.observableList(amPMList);
        cb.setPromptText("AM/PM");
        cb.setVisibleRowCount(2);
        cb.setItems(amPm);
    }

    /**
     * Initializes a ComboBox to contain the set of all Customers with strings showing first the id, followed by a
     * colon, followed by Customer name. eg. "1: John Doe", "2: Jane Doe" etc.
     * @param cb - ComboBox to initialize
     */
    public static void initCustomerIDComboBox(ComboBox cb) {
        ArrayList combinedStringList = new ArrayList<String>(); // customer id + customer name in single string
        for(Customer c: DBCustomer.getAllCustomers()) {
            combinedStringList.add(c.getId() + ": " + c.getName());
        }
        ObservableList<String> combinedStrings = FXCollections.observableList(combinedStringList);
        cb.setItems(combinedStrings);
    }

    /**
     * Initializes a ComboBox with the set of all countries available in the database
     * @param cb - ComboBox to initialize
     */
    public static void initCountryComboBox(ComboBox cb) {
        ArrayList combinedStringList = new ArrayList<String>();
        for(Country c: DBCountry.getAllCountries()) {
            combinedStringList.add(c.getId() + ": " + c.getName());
        }
        ObservableList<String> stringList = FXCollections.observableList(combinedStringList);
        cb.setItems(stringList);
    }

    /**
     * Initilizes a ComboBox with the set of all Divisions available in the database. This combobox can only be
     * initialized AFTER country is chosen, so that the divisions are a proper match with the country selected.
     *
     * This makes sure that the Customer cannot select country "Scotland" and Division "Texas" because Texas does
     * not exist in the country of Scotland, it exists only in the United States, and so the country "U.S" must be
     * selected before the Division "Texas" can be seen and selected in the Division ComboBox.
     *
     * @param cb - ComboBox to be initialized
     * @param countryString - Selected Country from another ComboBox.
     */
    public static void initDivisionIdComboBox(ComboBox cb, String countryString) {
        ArrayList combinedStringList = new ArrayList<String>();
        for(Division d: DBDivision.getAllDivisionsWithCountryName(countryString)) {
            combinedStringList.add(d.getId() + ": " + d.getDivision());
        }
        ObservableList<String> stringList = FXCollections.observableList(combinedStringList);
        cb.setItems(stringList);
    }

    /**
     * Initializes a ComboBox with the set of all users from the database with a string contraining first the user id,
     * followed by a colon, followed by the user name, eg. "1: myUsername1", "2: jdoe123" etc.
     *
     * @param cb - ComboBox to be initialized
     */
    public static void initUserIDComboBox(ComboBox cb) {
        ArrayList combinedStringList = new ArrayList<String>(); // customer id + customer name in single string
        for(User u: DBUser.getAllUsers()) {
            combinedStringList.add(u.getId() + ": " + u.getName());
        }
        ObservableList<Integer> combinedStrings = FXCollections.observableList(combinedStringList);
        cb.setItems(combinedStrings);
    }

    /**
     * Initilizes a ComboBox with the set of all Contacts from the database.
     * @param cb - ComboBox to be initialized
     */
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
     * @param cb - combobox we are getting the selection from
     * @param lbl - label attached to that combobox we can update with error messages
     * @param lblText - text string to set the label to if value is invalid and we need to show red error message
     * @return - true if combobox was not empty and had valid input, false if combobox was empty or had invalid input
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

    /**
     * Returns the id number of the selected value string of the ComboBox passed in. For example, if the string of the
     * selected value is "1: John Doe" this function will return the number.
     * A string value of "23: Jane Doe" would return the number 23 and so on.
     * @param cb - ComboBox to get the select value id integer from
     * @return - integer value extracted from the string of the selected value of the ComboBox passed in
     */
    public static int getIdNumberFromComboBox(ComboBox cb) {
        return Integer.parseInt(cb.getSelectionModel().getSelectedItem().toString().split(" ")[0].replace(":", ""));
    }

    /**
     * Returns the string component of the selected value string of the ComboBox passed in. For example, if the string
     * of the selected value is "1: John Doe" this function will return the string "John Doe".
     * A String value of "23: Jane Doe" would return the string "Jane Doe" and so on.
     * @param cb - ComboBox to get the string component from
     * @return - String value extracted from the string of the selected value of the ComboBox passed in
     * @throws Exception - throws exception for strings that are not formatted as such "1: Example1", "2: Example2" and
     * so on.
     *
     * A string with value "John Doe" will throw an exception because it does not start with a number followed by a
     * colon. A correct string would look like: "1: John Doe", "2: John Doe" etc.
     */
    public static String getStringFromComboBox(ComboBox cb) throws Exception {
        return cb.getSelectionModel().getSelectedItem().toString().split(" ")[1].replace(":", "");
    }

    /**
     * Sets a Label to show red text with the message "Cannot be empty"
     * @param label - Label to set to the red text and set the message "Cannot be empty"
     *
     *  Handy for informing the user that s/he/they left a text field empty
     */
    public static void setErrorLabel(Label label) {
        label.setTextFill(Color.color(1, 0, 0));
        label.setText("Cannot be empty");
    }

    /**
     * Initializes a ComboBox to contain string values for the 12 months in a year
     * @param cb - ComboBox to initialize
     */
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

    /**
     * Initializes a ComboBox with all the Contacts present in the database.
     * @param cb - ComboBox to initialize
     */
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
