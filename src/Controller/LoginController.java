package Controller;

import DBAccess.DBUser;
import Localization.Translate;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LoginController implements Initializable {
    public Label userErrorLabel;
    public TextField userTextField;
    public Label passwordErrorLabel;
    public PasswordField passwordTextField;
    public Label zoneLabel;
    public Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userTextField.setPromptText(Translate.str("Username"));
        passwordTextField.setPromptText(Translate.str("Password"));
        System.out.println(Translate.str("Initialized"));

        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        System.out.println("zoneId = " + zoneId);
        zoneLabel.setText(Translate.str("Zone") + " : " +zoneId);

        Locale french = new Locale("fr", "FR");
        Locale english = new Locale("en", "EN");

        ResourceBundle rb = ResourceBundle.getBundle("Localization/lang", french);
        //ResourceBundle rb = ResourceBundle.getBundle("Localization/lang", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
            System.out.println(rb.getString("hello") + rb.getString("world"));
            String translation = Translate.str("Cannot be empty");
            System.out.println(translation);
        }
    }

    public void onUsername(ActionEvent actionEvent) throws IOException {

    }

    public void onPassword(ActionEvent actionEvent) throws IOException {

    }

    public void onLoginButton(ActionEvent actionEvent) throws IOException {
        String username = userTextField.getText();
        String password = passwordTextField.getText();

        if(username.length() == 0) {
            userErrorLabel.setText(Translate.str("Cannot be empty"));
            return;
        }

        if(password.length() == 0) {
            passwordErrorLabel.setText(Translate.str("Cannot be empty"));
            return;
        }

        User user = null;
        try {
            user = DBUser.getUserByUserName(username);
            user.print();
        } catch (SQLException e) {
            System.err.println(Translate.str("Error: could not find user with username: ") +username);
            Alert alert = new Alert(Alert.AlertType.WARNING, Translate.str("Could not find user with username ") + ": " +username, ButtonType.OK);
            alert.showAndWait();
            return;
        } catch (Exception e) {
            System.err.println(Translate.str("Error: could not find user with username: ") +username);
            Alert alert = new Alert(Alert.AlertType.WARNING, Translate.str("Could not find user with username ") + ": " +username, ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(password.compareTo(user.getPassword()) == 0) {
            Parent root = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1400, 400);
            stage.setTitle("Customer Appointment Manager");
            stage.setScene(scene);
            stage.show();
        } else {
            //System.err.println(Translate.str("Error: incorrect password for: ") + username);
            Alert alert = new Alert(Alert.AlertType.WARNING, Translate.str("Error incorrect password for ") + ": " + username, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.WARNING, "Error : incorrect password for : " + username, ButtonType.OK);
            alert.showAndWait();
            return;
        }
    }
}
