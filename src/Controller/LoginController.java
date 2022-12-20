package Controller;

import DBAccess.DBUser;
import Localization.Translate;
import Model.User;
import Utils.AlertUtil;
import Utils.SceneLoader;
import Utils.TimeUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * LoginController is the controller class linked to the login.fxml view. It implements Initializable, and the Intialize()
 * function is called immediately after login.fxml is loaded. When the user clicks the login button the onLoginButton()
 * is called where the bulk of the error checking code is executed. The main function of this class is to check the database
 * for correct credentials and deny user access until s/he/they enter a valid username and password combination.
 */
public class LoginController implements Initializable {

    /**
     * A Label that prompts the user when they have made an error such as entering an unrecognized username
     * or leaving the username text field empty when clicking the login button.
     */
    public Label userErrorLabel;

    /**
     * A TextField where the user can enter their username for login
     */
    public TextField userTextField;

    /**
     * A Label that prompts the user when they have entered an incorrect password or left the password field empty when
     * click the login button
     */
    public Label passwordErrorLabel;

    /**
     * A TextField where the user can enter their password for login. All characters entered are shown as dots and cannot be
     * seen on screen.
     */
    public PasswordField passwordTextField;

    /**
     * Displays the time zone that the user is in using the operating system to detect time zone.
     */
    public Label zoneLabel;

    /**
     * A Button the user will click after they have entered their username and password. On click the username and password
     * will be checked against the database and error messages and/or warning dialogs will be shown if needed.
     */
    public Button loginButton;

    private String loginActivityFile = "login_activity.txt";

    /**
     * Called when the login.fxml view file is loaded, this function initializes item s in the view.
     * For example the prompts of the username and password TextFields are set.
     * @param url - not used
     * @param resourceBundle - not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userTextField.setPromptText(Translate.str("Username"));
        passwordTextField.setPromptText(Translate.str("Password"));

        ZoneId zoneId = TimeZone.getDefault().toZoneId(); // eg: "Zone: America/Chicago" for the central US time zone
        zoneLabel.setText(Translate.str("Zone") + " : " +zoneId);
    }

    /**
     * Called when the user clicks the Login button. If the username field and/or password field is empty the user cannot
     * proceed and will be shown a warning in red text above the appropriate text field. If the user has entered a username
     * not in the database they will be shown an error message inidicating that username does not exist. If user has
     * entered a valid username but invalid password, user will be shown a red text error message above the password
     * field indicating that the password entered is incorrect.
     *
     * On successful login the application redirects to the user the main view and loads main.fxml. As well, the
     * application checks the database to see if there are any appointments starting within the next 15 minutes.
     * @param actionEvent - not used
     * @throws IOException - throws if file can't be created for logging login activity
     */
    public void onLoginButton(ActionEvent actionEvent) throws IOException {

        // get strings from TextFields
        String username = userTextField.getText();
        String password = passwordTextField.getText();

        // prompt the user if any fields are empty. Username and password cannot be empty.
        if(username.length() == 0) { userErrorLabel.setText(Translate.str("Cannot be empty")); return; }
        if(password.length() == 0) { passwordErrorLabel.setText(Translate.str("Cannot be empty")); return; }

        // Assuming username field is not empty we check the database to see if this username exists and show a dialog
        // and exit early if username does not exist, otherwise we proceed to the rest of the checking logic
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
            System.err.println(Translate.str("Error could not find user with username ") + ": " + username);
            Alert alert = new Alert(Alert.AlertType.WARNING, Translate.str("Could not find user with username ") + ": " +username, ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Assuming we found the user in database, check the password with the user fetched from database, and if the
        // password is a match, we log that success in login_activity.txt
        boolean login_successful = password.compareTo(user.getPassword()) == 0;
        File f = new File(loginActivityFile);
        if(!f.exists()) { // if file does not exist we create one
            Files.createFile(Paths.get(loginActivityFile));
        }
        // append new login timestamp and whether the login was successful to the log file
        FileWriter myWriter = new FileWriter(loginActivityFile, true);
        myWriter.write("login: " + TimeUtils.getNowLocalTimeString() + " login_successful: " + Boolean.toString(login_successful) + "\n");
        myWriter.close();

        // Assuming login was successful, we check the database to see if any appointments are going to start within the
        // next 15 minutes
        if(login_successful) {
            SceneLoader.goToMainView(actionEvent);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, Translate.str("Error incorrect password for ") + ": " + username, ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String warning = TimeUtils.isWithin15Minute();
        AlertUtil.warning(warning, warning, warning);
    }
}
