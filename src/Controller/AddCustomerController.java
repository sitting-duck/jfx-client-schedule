package Controller;

import DBAccess.DBCountry;
import DBAccess.DBCustomer;
import DBAccess.DBDivision;
import Model.Country;
import Model.Division;
import Utils.UXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController  implements Initializable {

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
     * Called when the add-customer.fxml file for the Add Customer view is loaded. This function initializes any GUI
     * elements necessary and pulls items from the database if needed.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UXUtil.initCountryComboBox(countryComboBox);
    }

    /**
     * Called when the user selects a country from the Country ComboBox. This will reset the provinces or states in the
     * Province Division combo box such that they are all within the selected country.
     * @param actionEvent
     */
    public void onCountrySelected(ActionEvent actionEvent) {
        String countryString = null;
        try {
            countryString = UXUtil.getStringFromComboBox(countryComboBox);
        } catch (Exception e) {
            UXUtil.setErrorLabel(countryLabel);
        }
        divisionIdComboBox.setVisible(true);
        UXUtil.initDivisionIdComboBox(divisionIdComboBox, countryString);
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

        Division division = (Division) divisionIdComboBox.getValue();
        int divisionId = -1;
        if(divisionIdComboBox.isVisible()) {
            divisionId = UXUtil.getIdNumberFromComboBox(divisionIdComboBox);
        }

        if(division == null) {
            UXUtil.setErrorLabel(divisionIdLabel);
            good = false;
        } else {
            divisionIdLabel.setText("");
            divisionId = division.getId();
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
        if(divisionId == -1 && divisionIdComboBox.isVisible()) {
            UXUtil.setErrorLabel(divisionIdLabel);
            good = false;
        }
        if(good == false) {
            System.out.println("Input was not valid, Customer NOT updated in database.");
            return;
        }

        // insert new customer into database
        DBCustomer.insertCustomer(name, address, postalCode, phone, divisionId);

        // go back to main screen
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
