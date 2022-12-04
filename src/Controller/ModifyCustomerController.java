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

    private static Customer customer = null;

    @FXML
    private TextField idTextField;
    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressTextField;

    @FXML
    private Label postalCodeLabel;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private Label phoneLabel;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Label countryLabel;

    @FXML
    private Label divisionIdLabel;

    @FXML
    private ComboBox countryComboBox;

    @FXML
    private ComboBox divisionIdComboBox;

    public static void setCustomer(Customer _customer) {
        customer = _customer;
    }

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

    public void onCountrySelected(ActionEvent actionEvent) throws IOException, SQLException {
        String countryString = null;
        try {
            countryString = UXUtil.getStringFromComboBox(countryComboBox);
        } catch (Exception e) {
            UXUtil.setErrorLabel(countryLabel);
        }
        divisionIdComboBox.setItems(DBDivision.getAllDivisionsWithCountryName((String) countryString));
    }
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

    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 400);
        stage.setTitle("Customer Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }

}
