package Controller;

import DBAccess.DBCountry;
import DBAccess.DBCustomer;
import DBAccess.DBDivision;
import Model.Country;
import Model.Customer;
import Model.Division;
import Utils.UXUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

    /**
     * This Customer object is passed in from the main view and it is the Customer the user selected in the table
     * before clicking the Modify button. In this modify view, the user will be editing and modifying exactly this
     * Customer passed in, and if they click the OK button, the Customer will be entered into the Database with the new
     * data.
     */
    private static Customer customer = null;

    /**
     * A non-ditable text field that shows the 'Customer_ID' value from the database for this Customer. All Customers
     * have a unique id created for them when they are added to the database.
     */
    @FXML
    private TextField idTextField;

    /**
     * The label for the text field where the user enters the name of the Customer.
     */
    @FXML
    private Label nameLabel;

    /**
     * The text field where the user enters the name of the customer
     */
    @FXML
    private TextField nameTextField;

    /**
     * The label for the address text field.
     */
    @FXML
    private Label addressLabel;

    /**
     The text field where the user enters the address of the Customer
     */
    @FXML
    private TextField addressTextField;

    /**
     * The label for the postal code text field.
     */
    @FXML
    private Label postalCodeLabel;

    /**
     * The text field where the user enters the postal code of the Customer.
     */
    @FXML
    private TextField postalCodeTextField;

    /**
     * The label for the phone number text field.
     */
    @FXML
    private Label phoneLabel;

    /**
     * The text field where the user enters the Customer's phone number
     */
    @FXML
    private TextField phoneTextField;

    /**
     * A Label for the Country ComboBox
     */
    @FXML
    private Label countryLabel;

    /**
     * A Label for the DivisionId ComboBox
     */
    @FXML
    private Label divisionIdLabel;

    /**
     * The user uses this ComboBox to select the Country that the Customer resides in. On selection, the Division ID
     * ComboBox will populate with all the provinces or states in the selected country.
     */
    @FXML
    private ComboBox countryComboBox;

    /**
     * The user uses this ComboBox to select the province or state the Customer will be in after they select a Country
     * from the countryComboBox
     */
    @FXML
    private ComboBox divisionIdComboBox;

    /**
     * Called when the Modify Customer view is loaded. It is meant to pass the selected Customer from the Customer table
     * in the main view to this Modify Customer view. It is set in the variable named "customer" defined in this class.
     * @param _customer
     */
    public static void setCustomer(Customer _customer) {
        customer = _customer;
    }

    /**
     * Called when the modify-customer.fxml file for the Add Customer view is loaded. This function initializes any GUI
     * elements necessary and pulls items from the database if needed.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(customer == null) {
            System.out.println("Error: no customer selected");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No customer selected");
            alert.setHeaderText("No customer selected");
            alert.setContentText("No customer selected. Please select a customer to modify.");
            alert.showAndWait();
        }

        // ComboBox needs to be populated in case the user wants to select another
        divisionIdComboBox.setItems(DBDivision.getAllDivisions());
        ObservableList<Division> divisionList = DBDivision.lookupDivision(customer.getDivisionId());

        // set divisionID combobox to value stored for customer in database
        int divisionId = customer.getDivisionId();
        Division division = DBDivision.lookupDivision(divisionId).get(0);
        String divisionIdString = divisionId + ": " + division.getDivision();
        divisionIdComboBox.setValue(divisionIdString);

        // combobox for country needs to be populated in case the user wants to select another
        UXUtil.initCountryComboBox(countryComboBox);

        // set country combobox using division to determine what country
        int countryId = division.getCountryId();
        Country country = DBCountry.getAllCountriesWithID(countryId).get(0);
        UXUtil.initDivisionIdComboBox(divisionIdComboBox, country.getName());
        String countryString = country.getId() + ": " + country.getName();
        countryComboBox.setValue(countryString);

        // customer id is auto generated and should not be edited by the user
        idTextField.setText(Integer.toString(customer.getId()));
        idTextField.setEditable(false);
        idTextField.setDisable(true);

        nameTextField.setText(customer.getName());
        addressTextField.setText(customer.getAddress());
        postalCodeTextField.setText(customer.getPostal());
        phoneTextField.setText(customer.getPhone());

    }

    /**
     * Called when the user selects a country from the Country ComboBox. This will reset the provinces or states in the
     * Province Division combo box such that they are all within the selected country.
     * @param actionEvent
     */
    public void onCountrySelected(ActionEvent actionEvent) throws IOException, SQLException {
        String countryString = null;
        try {
            countryString = UXUtil.getStringFromComboBox(countryComboBox);
        } catch (Exception e) {
            UXUtil.setErrorLabel(countryLabel);
        }
        divisionIdComboBox.setItems(DBDivision.getAllDivisionsWithCountryName((String) countryString));
    }

    /**
     * Called after the user clicks the Ok. On click the function checks all the fields in Add Customer view to make
     * sure that they are valid and that they are not empty.
     * @param actionEvent
     * @throws IOException - throws an exception is main.fxml cannot be found or loaded when returning to the main view
     * @throws SQLException - throws an exception if there is an error inserting the new Customer into the database
     */
    public void onOkButton(ActionEvent actionEvent) throws IOException, SQLException {
        boolean good = true;
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String phone = phoneTextField.getText();

        try {
            UXUtil.getStringFromComboBox(countryComboBox);
        } catch (Exception e) {
            UXUtil.setErrorLabel(countryLabel);
            good = false;
        }

        int divisionId = -1;
        if(divisionIdComboBox.isVisible()) {
            divisionId = UXUtil.getIdNumberFromComboBox(divisionIdComboBox);
        }

        if(divisionId == -1) {
            UXUtil.setErrorLabel(divisionIdLabel);
            good = false;
        } else {
            divisionIdLabel.setText("");
        }
        if(name.compareTo("") == 0) {
            UXUtil.setErrorLabel(nameLabel);
            good = false;
        } else {
            nameLabel.setText("");
        }
        if(address.compareTo("") == 0) {
            UXUtil.setErrorLabel(addressLabel);
            good = false;
        } else {
            addressLabel.setText("");
        }
        if(postalCode.compareTo("") == 0) {
            UXUtil.setErrorLabel(postalCodeLabel);
            good = false;
        } else {
            postalCodeLabel.setText("");
        }
        if(phone.compareTo("") == 0) {
            UXUtil.setErrorLabel(phoneLabel);
            good = false;
        } else {
            phoneLabel.setText("");
        }
        if(divisionId == -1) {
            UXUtil.setErrorLabel(divisionIdLabel);
            good = false;
        }
        if(good == false) {
            System.out.println("Input was not valid, Customer NOT updated in database.");
            return;
        }

        DBCustomer.updateCustomer(customer.getId(), name, address, postalCode, phone, divisionId);

        Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 400);
        stage.setTitle("Customer Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Called after the user clicks the Cancel button. On click it closes the Add Customer view and returns to the main
     * application view with the Customers and the Appointments tables.
     * @param actionEvent - not used.
     * @throws IOException - throws an exception if main.fxml cannot be found or loaded
     */
    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 400);
        stage.setTitle("Customer Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }

}
