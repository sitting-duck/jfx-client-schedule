package Controller;

import DBAccess.DBCountry;
import DBAccess.DBCustomer;
import DBAccess.DBDivision;
import Model.Country;
import Model.Division;
import Utils.SceneLoader;
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

public class AddCustomerController extends CustomerController implements Initializable {

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
    public void onCountrySelected(ActionEvent actionEvent) throws SQLException, IOException {
        super.onCountrySelected(actionEvent);
    }

    /**
     * Called after the user clicks the Ok. On click the function checks all the fields in Add Customer view to make
     * sure that they are valid and that they are not empty.
     * @param actionEvent
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

        if(good) { // if input is good we insert new customer into database
            DBCustomer.insertCustomer(name, address, postalCode, phone, divisionId);
        } else {
            return good; // allow user to try again with error message prompts
        }
        SceneLoader.goToMainView(actionEvent);
        return good;
    }
}
