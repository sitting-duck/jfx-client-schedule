package Controller;

import Utils.SceneLoader;
import Utils.UXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Generic controller class for add customer and modify customer views
 */
public class CustomerController {

    /**
     * The label for the text field where the user enters the name of the Customer.
     */
    @FXML
    protected Label nameLabel;

    /**
     * The text field where the user enters the name of the customer
     */
    @FXML
    protected TextField nameTextField;

    /**
     * The label for the address text field.
     */
    @FXML
    protected Label addressLabel;

    /**
     The text field where the user enters the address of the Customer
     */
    @FXML
    protected TextField addressTextField;

    /**
     * The label for the postal code text field.
     */
    @FXML
    protected Label postalCodeLabel;

    /**
     * The text field where the user enters the postal code of the Customer.
     */
    @FXML
    protected TextField postalCodeTextField;

    /**
     * The label for the phone number text field.
     */
    @FXML
    protected Label phoneLabel;

    /**
     * The text field where the user enters the Customer's phone number
     */
    @FXML
    protected TextField phoneTextField;

    /**
     * A Label for the Country ComboBox
     */
    @FXML
    protected Label countryLabel;

    /**
     * A Label for the DivisionId ComboBox
     */
    @FXML
    protected Label divisionIdLabel;

    /**
     * The user uses this ComboBox to select the Country that the Customer resides in. On selection, the Division ID
     * ComboBox will populate with all the provinces or states in the selected country.
     */
    @FXML
    protected ComboBox countryComboBox;

    /**
     * The user uses this ComboBox to select the province or state the Customer will be in after they select a Country
     * from the countryComboBox
     */
    @FXML
    protected ComboBox divisionIdComboBox;

    /**
     * Called when the user selects a country from the Country ComboBox. This will reset the provinces or states in the
     * Province Division combo box such that they are all within the selected country.
     * @param actionEvent - not used
     * @throws IOException - throws if there is an issue with a string in a ComboBox
     * @throws SQLException - throws if there is an issue pulling countries from the database
     */
    public void onCountrySelected(ActionEvent actionEvent) throws IOException, SQLException {
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
     * @param actionEvent - not used
     * @throws IOException - throws an exception is main.fxml cannot be found or loaded when returning to the main view
     * @throws SQLException - throws an exception if there is an error inserting the new Customer into the database
     * @return true if user input is good, false if user input was invalid
     */
    public boolean onOkButton(ActionEvent actionEvent) throws IOException, SQLException {
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

        //Division division = (Division) divisionIdComboBox.getValue();
        int divisionId = -1;
        if(divisionIdComboBox.isVisible() && divisionIdComboBox.getSelectionModel().getSelectedItem() != null) {
            try {
                divisionId = UXUtil.getIdNumberFromComboBox(divisionIdComboBox);
            } catch (Exception e) {
                System.out.println("Division ComboBox not set");
            }
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
        }
        return good;
    }

    /**
     * Called after the user clicks the Cancel button. On click it closes the Add/Modify Customer view and returns to the main
     * application view with the Customers and the Appointments tables.
     * @param actionEvent - not used.
     * @throws IOException - throws an exception if main.fxml cannot be found or loaded
     */
    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        SceneLoader.goToMainView(actionEvent);
    }

}
