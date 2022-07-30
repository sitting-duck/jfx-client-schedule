package Controller;

import DBAccess.DBCustomer;
import DBAccess.DBDivision;
import Model.Customer;
import Model.Division;
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
    private Label divisionIdLabel;

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
        divisionIdComboBox.setItems(DBDivision.getAllDivisions());
        ObservableList<Division> divisionList = DBDivision.lookupDivision(customer.getDivisionId());
        divisionIdComboBox.setValue(divisionList.get(0));

        // customer id is auto generated and should not be edited by the user
        idTextField.setText(Integer.toString(customer.getId()));
        idTextField.setEditable(false);
        idTextField.setDisable(true);

        nameTextField.setText(customer.getName());
        addressTextField.setText(customer.getAddress());
        postalCodeTextField.setText(customer.getPostal());
        phoneTextField.setText(customer.getPhone());
    }

    public void onOkButton(ActionEvent actionEvent) throws IOException, SQLException {
        boolean good = true;
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String phone = phoneTextField.getText();
        Division division = (Division) divisionIdComboBox.getValue();
        int divisionId = -1;
        if(division == null) {
            divisionIdLabel.setTextFill(Color.color(1, 0, 0));
            divisionIdLabel.setText("Cannot be empty");
            good = false;
        } else {
            divisionIdLabel.setText("");
            divisionId = division.getId();
        }

        if(name.compareTo("") == 0) {
            nameLabel.setTextFill(Color.color(1, 0, 0));
            nameLabel.setText("Cannot be empty");
            good = false;
        }
        if(address.compareTo("") == 0) {
            addressLabel.setTextFill(Color.color(1, 0, 0));
            addressLabel.setText("Cannot be empty");
            good = false;
        }
        if(postalCode.compareTo("") == 0) {
            postalCodeLabel.setTextFill(Color.color(1, 0, 0));
            postalCodeLabel.setText("Cannot be empty");
            good = false;
        }
        if(phone.compareTo("") == 0) {
            phoneLabel.setTextFill(Color.color(1, 0, 0));
            phoneLabel.setText("Cannot be empty");
            good = false;
        }
        if(divisionId == -1) {
            divisionIdLabel.setTextFill(Color.color(1, 0, 0));
            divisionIdLabel.setText("Cannot be empty");
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
