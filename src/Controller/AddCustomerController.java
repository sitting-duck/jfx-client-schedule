package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
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
    private Label divisionIdLabel;

   @FXML
    private TextField divisionIdTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onOkButton(ActionEvent actionEvent) throws IOException {

    }

    public void onCancelButton(ActionEvent actionEvent) throws IOException {

    }

}
