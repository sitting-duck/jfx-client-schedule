package Controller;

import DBAccess.DBUser;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public Button exitButton;
    public Label userErrorLabel;
    public TextField userTextField;
    public Label passwordErrorLabel;
    public TextField passwordTextField;
    public Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userTextField.setPromptText("Username");
        passwordTextField.setPromptText("Password");
        System.out.println("Initialized");
    }

    public void onExitButton(ActionEvent actionEvent) throws IOException {

    }

    public void onUsername(ActionEvent actionEvent) throws IOException {

    }

    public void onPassword(ActionEvent actionEvent) throws IOException {

    }

    public void onLoginButton(ActionEvent actionEvent) throws IOException {
        String username = userTextField.getText();
        String password = passwordTextField.getText();

        if(username.length() == 0) {
            userErrorLabel.setText("Cannot be empty");
            return;
        }

        if(password.length() == 0) {
            passwordErrorLabel.setText("Cannot be empty");
            return;
        }

        User user = null;
        try {
            user = DBUser.getUserByUserName(username);
            user.print();
        } catch (SQLException e) {
            System.err.println("Error: could not fine user with username: " +username);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Could not fine user with username: " +username, ButtonType.OK);
            alert.showAndWait();
            return;
        } catch (Exception e) {
            System.err.println("Error: could not fine user with username: " +username);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Could not fine user with username: " +username, ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(password.compareTo(user.getPassword()) == 0) {

        } else {
            System.err.println("Error: incorrect password for: " +username);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Error: incorrect password for: " +username, ButtonType.OK);
            alert.showAndWait();
            return;
        }

    }
}
