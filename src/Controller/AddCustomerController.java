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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UXUtil.initCountryComboBox(countryComboBox);
    }

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

    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 400);
        stage.setTitle("Customer Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }
}
