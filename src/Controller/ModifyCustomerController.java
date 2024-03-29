package Controller;

import DBAccess.DBCountry;
import DBAccess.DBCustomer;
import DBAccess.DBDivision;
import Model.Country;
import Model.Customer;
import Model.Division;
import Utils.AlertUtil;
import Utils.SceneLoader;
import Utils.UXUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller class attached to the modify-customer.fxml view. Performs logic attached to loading a Customer from the
 * database and allowing the user to interact with some text fields, comboboxes and other widgets to modify the Customer
 * data and update the database on clicking the Ok Button.
 */
public class ModifyCustomerController extends CustomerController implements Initializable {

    /**
     * This Customer object is passed in from the main view and it is the Customer the user selected in the table
     * before clicking the Modify button. In this modify view, the user will be editing and modifying exactly this
     * Customer passed in, and if they click the OK button and all checks pass, the Customer will be entered into the
     * Database with the new data.
     */
    private static Customer customer = null;

    /**
     * A non-editable text field that shows the 'Customer_ID' value from the database for this Customer. All Customers
     * have a unique id created for them when they are added to the database.
     */
    @FXML
    private TextField idTextField;

    /**
     * Called when the Modify Customer view is loaded. It is meant to pass the selected Customer from the Customer table
     * in the main view to this M - the customer passed into this view for editing by the user
     * @param _customer - the Customer object to be edited in the Modify Customer view by the user
     */
    public static void setCustomer(Customer _customer) {
        customer = _customer;
    }

    /**
     * Called when the modify-customer.fxml file for the Add Customer view is loaded. This function initializes any GUI
     * elements necessary and pulls items from the database if needed.
     * @param url - not used
     * @param resourceBundle - not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(customer == null) {
            AlertUtil.customerSelectWarningModify();
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
     * @param actionEvent - for navigating scenes
     */
    public void onCountrySelected(ActionEvent actionEvent) throws IOException, SQLException {
        super.onCountrySelected(actionEvent);
    }

    /**
     * Called after the user clicks the Ok. On click the function checks all the fields in Add Customer view to make
     * sure that they are valid and that they are not empty.
     * @param actionEvent - for navigating back to the main we pass this to FXMLLoader in SceneLoader class
     * @throws IOException - throws an exception is main.fxml cannot be found or loaded when returning to the main view
     * @throws SQLException - throws an exception if there is an error inserting the new Customer into the database
     */
    public boolean onOkButton(ActionEvent actionEvent) throws IOException, SQLException {
        boolean good = super.onOkButton(actionEvent);

        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String phone = phoneTextField.getText();
        int divisionId = -1;
        if(divisionIdComboBox.isVisible() && divisionIdComboBox.getSelectionModel().getSelectedItem() != null) {
            divisionId = UXUtil.getIdNumberFromComboBox(divisionIdComboBox);
        }

        if(good) { // if input is good we update customer in database
            DBCustomer.updateCustomer(customer.getId(), name, address, postalCode, phone, divisionId);
        } else {
            return good; // allow user to try again with error message prompts
        }
        SceneLoader.goToMainView(actionEvent);
        return good;
    }
}